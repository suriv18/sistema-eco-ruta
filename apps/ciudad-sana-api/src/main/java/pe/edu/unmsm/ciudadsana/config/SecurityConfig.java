package pe.edu.unmsm.ciudadsana.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pe.edu.unmsm.ciudadsana.shared.security.filter.JwtAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private static final String[] PUBLIC_POST_PATHS = {
        "/api/v1/auth/login",
        "/api/v1/auth/refresh",
        "/api/v1/ciudadanos/alertas"
    };

    private static final String[] PUBLIC_GET_PATHS = {
        "/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/actuator/health",
        "/actuator/info"
    };

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final List<String> allowedOrigins;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            @Value("${app.cors.allowed-origins}") String allowedOriginsRaw
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.allowedOrigins = Arrays.asList(allowedOriginsRaw.split(","));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, PUBLIC_POST_PATHS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_GET_PATHS).permitAll()
                        .requestMatchers("/api/v1/auth/logout", "/api/v1/auth/me").authenticated()
                        .requestMatchers("/api/v1/usuarios/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/v1/rutas/**").hasAnyRole("ADMIN", "SUPERVISOR", "OPERADOR", "ANALISTA")
                        .requestMatchers(HttpMethod.POST, "/api/v1/rutas/**").hasAnyRole("ADMIN", "SUPERVISOR")
                        .requestMatchers("/api/v1/operacion/**").hasAnyRole("ADMIN", "SUPERVISOR")
                        .requestMatchers("/api/v1/telemetria/**").hasAnyRole("ADMIN", "SUPERVISOR", "OPERADOR", "ANALISTA")
                        .requestMatchers("/api/v1/kpi/**").hasAnyRole("ADMIN", "SUPERVISOR", "ANALISTA")
                        .requestMatchers("/api/v1/auditoria/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(allowedOrigins);
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type", "X-Tenant-ID"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}

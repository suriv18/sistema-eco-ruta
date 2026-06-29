package pe.edu.unmsm.ciudadsana.integracion.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class IntegracionInfrastructureConfig {

    @Bean
    public RestTemplate integracionRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(35000);
        return new RestTemplate(factory);
    }
}

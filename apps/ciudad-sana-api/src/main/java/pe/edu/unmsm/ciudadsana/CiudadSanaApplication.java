package pe.edu.unmsm.ciudadsana;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
public class CiudadSanaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CiudadSanaApplication.class, args);
    }
}

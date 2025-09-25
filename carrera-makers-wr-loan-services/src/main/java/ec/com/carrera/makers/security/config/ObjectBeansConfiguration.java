package ec.com.carrera.makers.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectBeansConfiguration {

    @Bean(name = "securityObjectMapper")
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

}

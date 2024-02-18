package sn.esmt.hlr_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sn.esmt.hlr_api.soam.ObjectFactory;

@Configuration
public class BeanConfig {
    @Bean
    public ObjectFactory objectFactory() {
        return new ObjectFactory();
    }
}

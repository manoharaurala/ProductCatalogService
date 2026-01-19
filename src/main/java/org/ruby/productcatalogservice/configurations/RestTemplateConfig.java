package org.ruby.productcatalogservice.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    /*
    In configurations, we tell spring to create some library object and keep it /
    manage it's lifecycle
     */

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

package org.organizerClient;

import org.organizerClient.dto.UserAuth;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfiguration {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @Bean
    public UserAuth getUserAuth(){
        return new UserAuth();
    }

}

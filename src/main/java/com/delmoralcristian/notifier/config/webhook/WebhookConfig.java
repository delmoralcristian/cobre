package com.delmoralcristian.notifier.config.webhook;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebhookConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

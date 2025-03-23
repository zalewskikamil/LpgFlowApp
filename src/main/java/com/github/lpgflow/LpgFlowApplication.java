package com.github.lpgflow;

import com.github.lpgflow.infrastructure.security.JwtConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {JwtConfigurationProperties.class})
public class LpgFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(LpgFlowApplication.class, args);
    }

}

package com.example.demo.common.config.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaAuditing
public class JpaConfig {

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new JpaTransactionManager();
    }
}

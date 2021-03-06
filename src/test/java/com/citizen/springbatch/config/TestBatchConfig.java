package com.citizen.springbatch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * author : citizen103
 */
@Configuration
@EntityScan(basePackages = "com.citizen.springbatch.domain")
@EnableJpaRepositories(basePackages = "com.citizen.springbatch.repository")
@EnableBatchProcessing
@EnableAutoConfiguration
public class TestBatchConfig {

}

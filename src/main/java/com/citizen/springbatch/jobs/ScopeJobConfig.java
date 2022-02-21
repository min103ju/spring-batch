package com.citizen.springbatch.jobs;

import com.citizen.springbatch.tasklet.SimpleJobTasklet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author : citizen103
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ScopeJobConfig {

    private final String JOB_NAME = "simpleJob";
    private final String STEP_NAME = "simpleStep";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final SimpleJobTasklet simpleJobTasklet;

    @Bean
    public Job simpleJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(simpleStep1())
            .build();
    }

    @Bean
    public Step simpleStep1() {
        return stepBuilderFactory.get(STEP_NAME)
            .tasklet(simpleJobTasklet)
            .build();
    }


}

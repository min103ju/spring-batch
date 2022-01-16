package com.citizen.springbatch.jobs;

import com.citizen.springbatch.tasklets.BatchTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration과 @Bean을 통해 Spring Container에 등록을 해야 Job과 Step이 구동한다.
@RequiredArgsConstructor
@Configuration
public class BatchConfig {

    private final String JOB_NAME = "batchJob";
    private final String STEP_NAME = "batchStep";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    // JobBuilderFactory를 통해 Job 생성
    // public으로 생성
    @Bean
    public Job batchJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(batchStep())
            .build();
    }

    // StepBuilderFactory를 통해 Step 생성
    // public으로 생성
    @Bean
    public Step batchStep() {
        return stepBuilderFactory.get(STEP_NAME)
            .tasklet(new BatchTasklet())
            .build();
    }

}

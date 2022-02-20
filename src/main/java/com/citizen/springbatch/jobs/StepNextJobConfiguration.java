package com.citizen.springbatch.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author : citizen103
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class StepNextJobConfiguration {

    private final String JOB_NAME = "stepNextJob";
    private final String STEP1_NAME = "nextStep1";
    private final String STEP2_NAME = "nextStep2";
    private final String STEP3_NAME = "nextStep3";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepNextJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(step1())
            .next(step2())
            .next(step3())
            .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get(STEP1_NAME)
            .tasklet((contribution, chunkContext) -> {
                log.info(">>>>> This is Step1");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get(STEP2_NAME)
            .tasklet((contribution, chunkContext) -> {
                log.info(">>>>> This is Step2");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get(STEP3_NAME)
            .tasklet((contribution, chunkContext) -> {
                log.info(">>>>> This is Step3");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

}

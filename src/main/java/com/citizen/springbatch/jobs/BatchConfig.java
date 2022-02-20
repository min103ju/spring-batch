package com.citizen.springbatch.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration과 @Bean을 통해 Spring Container에 Job과 Step을 등록해야한다.
@Slf4j
@RequiredArgsConstructor
@Configuration
public class BatchConfig {

    private final String JOB_NAME = "batchJob";
    private final String STEP1_NAME = "batchStep1";
    private final String STEP2_NAME = "batchStep2";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    // JobBuilderFactory를 통해 Job 생성
    // public으로 생성
    @Bean
    public Job batchJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(batchStep1(null))
            .next(batchStep2(null))
            .build();
    }

    // StepBuilderFactory를 통해 Step 생성
    // public으로 생성
    @Bean
    //@JobScope를 잊으면 안된다.
    @JobScope
    public Step batchStep1(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get(STEP1_NAME)
            // Tasklet은 Step 안에서 단일로 수행될 커스텀한 기능들을 선언할 때 사용
            .tasklet((contribution, chunkContext) -> {
                throw new IllegalArgumentException("Step1에서 실패");
            })
            .build();
    }

    @JobScope
    public Step batchStep2(@Value("#{jobParameters[requestDate]}") String requestDate) {
        return stepBuilderFactory.get(STEP2_NAME)
            .tasklet((contribution, chunkContext) -> {
                log.info(">>>> This is step 2.");
                log.info(">>>>requestDate : {}", requestDate);
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    /**
     * Job > Step > Tasklet 또는 Reader, Processor, Writer
     * Job안에 여러개의 Step이 존재
     * Step안에 단일로 수행될 Tasklet이 존재
     * 단, Tasklet은 Reader & Processor & Writer 한 묶음과 같은 레벨이다.
     * 따라서 Reader & Processor & Writer 진행 후 Tasklet으로 마무리하는 등의 작업은 만들 수 없다.
     * (Tasklet은 @Component와 비슷하다. 명확한 역할은 없지만, 개발자가 지정한 커스텀 기능을 위한 단위이다.)
     */

}

package com.citizen.springbatch.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
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
public class StepNextConditionalJobConfiguration {

    private final String JOB_NAME = "stepNextConditionalJob";
    private final String STEP1_NAME = "conditionalStep1";
    private final String STEP2_NAME = "conditionalStep2";
    private final String STEP3_NAME = "conditionalStep3";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job stepNextConditionalJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(conditionalJobStep1())
            .on(ExitStatus.FAILED.getExitCode()) // 실패(FAILED)일 경우
            .to(conditionalJobStep3()) // step3을 실행
            .on("*") // step3의 결과와 상관없이
            .end() // step3으로 이동하면 flow 종료
            .from(conditionalJobStep1()) // step1로 부터
            .on("*") // 실패(FAILED) 외의 경우
            .to(conditionalJobStep2()) // step2를 실행
            .next(conditionalJobStep3()) // step2 성공 -> step3으로 이동
            .on("*") // step3의 결과와 상관없이
            .end() // step3으로 이동하면 flow 종료
            .end()
            .build();
    }

    @Bean
    public Step conditionalJobStep1() {
        return stepBuilderFactory.get(STEP1_NAME)
            .tasklet((contribution, chunkContext) -> {
                log.info(">>>>>> This is stepNextConditionalJob Step1");

                // ExitStatus를 보고 flow가 진행
//                contribution.setExitStatus(ExitStatus.FAILED);
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Step conditionalJobStep2() {
        return stepBuilderFactory.get(STEP2_NAME)
            .tasklet((contribution, chunkContext) -> {
                log.info(">>>>>> This is stepNextConditionalJob Step2");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Step conditionalJobStep3() {
        return stepBuilderFactory.get(STEP3_NAME)
            .tasklet((contribution, chunkContext) -> {
                log.info(">>>>>> This is stepNextConditionalJob Step3");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

}

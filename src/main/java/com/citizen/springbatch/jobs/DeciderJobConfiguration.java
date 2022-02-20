package com.citizen.springbatch.jobs;

import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author : citizen103
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class DeciderJobConfiguration {

    private final String JOB_NAME = "deciderJob";
    private final String START_STEP_NAME = "startStep";
    private final String EVEN_STEP_NAME = "evenStep";
    private final String ODD_STEP_NAME = "oddStep";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    @Bean
    public Job deciderJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(startStep())
            .next(decider()) // even | odd 구분
            .from(decider()) // Decider의 상태 검증
            .on("ODD") // ODD이면
            .to(oddStep()) // oddStep으로
            .from(decider()) // Decider의 상태 검증
            .on("EVEN") // EVEN이면
            .to(evenStep()) // evenStep으로
            .end()
            .build();
    }

    @Bean
    public Step startStep() {
        return stepBuilderFactory.get(START_STEP_NAME)
            .tasklet((contribution, chunkContext) -> {
                log.info(">>>>> start");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Step evenStep() {
        return stepBuilderFactory.get(EVEN_STEP_NAME)
            .tasklet((contribution, chunkContext) -> {
                log.info(">>>>> Even Step");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public Step oddStep() {
        return stepBuilderFactory.get(ODD_STEP_NAME)
            .tasklet((contribution, chunkContext) -> {
                log.info(">>>>> Odd Step");
                return RepeatStatus.FINISHED;
            })
            .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new OddEvenDecider();
    }

    private static class OddEvenDecider implements JobExecutionDecider {

        @Override
        public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
            Random random = new Random();

            int randomNumber = random.nextInt(50) + 1;
            log.info("랜덤숫자: {}", randomNumber);

            if (randomNumber % 2 == 0) {
                return new FlowExecutionStatus("EVEN");
            } else {
                return new FlowExecutionStatus("ODD");
            }
        }
    }
}

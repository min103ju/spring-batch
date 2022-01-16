package com.citizen.springbatch.schedulers;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {

    private final Job job; // BatchJob Bean이 주입된다.
    private final JobLauncher jobLauncher;

    // 5초 마다 실행(ms 단위)
    @Scheduled(fixedDelay = 1 * 1_000L)
    public void executeJob() {
        try {
            jobLauncher.run(
                job,
                new JobParametersBuilder()
                    .addString("datetime", LocalDateTime.now().toString())
                    .toJobParameters()
            );
        } catch (JobExecutionException e) {
            log.error(e.getMessage());
        }
    }
}

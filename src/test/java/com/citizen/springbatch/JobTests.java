package com.citizen.springbatch;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * author : citizen103
 */
// TODO: 2022-02-22 테스트 정상 가동하도록 수정
@ExtendWith(SpringExtension.class)
@SpringBatchTest
//@TestPropertySource(properties = {"job.name=" + StepScopeWarningConfiguration.JOB_NAME})
public class JobTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    void Step_Scope_Warning_테스트() throws Exception {
        // given
        JobParameters parameters = new JobParametersBuilder()
            .addLong("version", 3L)
            .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

}

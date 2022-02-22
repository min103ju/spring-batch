package com.citizen.springbatch;

import static org.assertj.core.api.Assertions.assertThat;

import com.citizen.springbatch.config.TestBatchConfig;
import com.citizen.springbatch.jobs.StepScopeWarningConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * author : citizen103
 */
@ActiveProfiles("mariadb")
@RunWith(SpringRunner.class)
@SpringBatchTest
@SpringBootTest(classes = {StepScopeWarningConfiguration.class, TestBatchConfig.class})
public class JobTests {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void Step_Scope_Warning_테스트() throws Exception {
        // given
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("title", "testPost1")
            .toJobParameters();

        // when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        // then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
    }

}

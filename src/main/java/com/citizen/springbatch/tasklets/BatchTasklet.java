package com.citizen.springbatch.tasklets;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;

// Job -> Step에서 실행할 Tasklet을 만든다.
// org.springframework.batch.core.step.tasklet의 Tasklet을 구현한다.
@Slf4j
@RequiredArgsConstructor
public class BatchTasklet implements Tasklet {

    private static int TASKLET_EXECUTE_COUNT = 0;

    private final String requestDate;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
        throws Exception {
        log.info("execute tasklet!! TASKLET_EXECUTE_COUNT : {}", ++TASKLET_EXECUTE_COUNT);
        log.info("requestDate: {}", requestDate);
        return RepeatStatus.FINISHED;
    }
}

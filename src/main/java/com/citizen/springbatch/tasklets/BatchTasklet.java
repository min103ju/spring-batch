package com.citizen.springbatch.tasklets;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

// Job -> Step에서 실행할 Tasklet을 만든다.
// org.springframework.batch.core.step.tasklet의 Tasklet을 구현한다.
@Slf4j
public class BatchTasklet implements Tasklet {

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext)
        throws Exception {
            log.info("execute tasklet!!");
        return RepeatStatus.FINISHED;
    }
}

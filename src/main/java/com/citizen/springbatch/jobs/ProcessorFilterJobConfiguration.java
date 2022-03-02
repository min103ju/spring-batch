package com.citizen.springbatch.jobs;

import com.citizen.springbatch.domain.Post;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author : citizen103
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ProcessorFilterJobConfiguration {

    private static final int CHUNK_SIZE = 10;

    private static final String JOB_NAME = "processorFilterJob";
    private static final String STEP_NAME = "processorFilterStep";
    private static final String READER_NAME = "processorFilterReader";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job processorFilterJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .preventRestart()
            .start(processorFilterStep())
            .build();
    }

    @Bean
    @JobScope
    public Step processorFilterStep() {
        return stepBuilderFactory.get(STEP_NAME)
            .<Post, Post>chunk(CHUNK_SIZE)
            .reader(processorFilterReader())
            .processor(processorFilterProcessor())
            .writer(processorFilterWriter())
            .build();
    }

    @Bean
    public ItemReader<Post> processorFilterReader() {
        return new JpaPagingItemReaderBuilder<Post>()
            .name(READER_NAME)
            .entityManagerFactory(entityManagerFactory)
            .pageSize(CHUNK_SIZE)
            .queryString("SELECT p FROM Post p")
            .build();
    }

    @Bean
    public ItemProcessor<Post, Post> processorFilterProcessor() {
        return post -> {

            if (post.getId() % 2 == 0) {
                log.info(">>>>>>>>>>>> Even number Post = {}", post);
                return null;
            }
            return post;
        };
    }

    private ItemWriter<Post> processorFilterWriter() {
        return items -> {
            for (Post post : items) {
                log.info(">>>>>>>>>>>> Odd number Post = {}", post);
            }
        };
    }

}

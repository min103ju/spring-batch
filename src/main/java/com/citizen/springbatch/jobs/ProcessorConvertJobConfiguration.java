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
public class ProcessorConvertJobConfiguration {

    private static final int CHUNK_SIZE = 10;

    private static final String JOB_NAME = "processorConvertJob";
    private static final String STEP_NAME = "processorConvertStep";
    private static final String READER_NAME = "processorConvertReader";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job processorConvertJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .preventRestart()
            .start(processorConvertStep())
            .build();
    }

    @Bean
    @JobScope
    public Step processorConvertStep() {
        return stepBuilderFactory.get(STEP_NAME)
            .<Post, String>chunk(CHUNK_SIZE)
            .reader(processorConvertReader())
            .processor(processorConvertProcessor())
            .writer(processorConvertWriter())
            .build();
    }

    @Bean
    public ItemReader<Post> processorConvertReader() {
        return new JpaPagingItemReaderBuilder<Post>()
            .name(READER_NAME)
            .entityManagerFactory(entityManagerFactory)
            .pageSize(CHUNK_SIZE)
            .queryString("SELECT p FROM Post p")
            .build();
    }

    @Bean
    public ItemProcessor<Post, String> processorConvertProcessor() {
        return post -> post.getTitle();
    }

    private ItemWriter<String> processorConvertWriter() {
        return items -> {
            for (String title : items) {
                log.info("Post title = {}", title);
            }
        };
    }

}

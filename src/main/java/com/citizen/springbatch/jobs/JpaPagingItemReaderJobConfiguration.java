package com.citizen.springbatch.jobs;

import com.citizen.springbatch.domain.Post;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author : citizen103
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JpaPagingItemReaderJobConfiguration {

    private static final int CHUNK_SIZE = 10;

    private static final String JOB_NAME = "jpaPagingItemReaderJob";
    private static final String STEP_NAME = "jpaPagingItemReaderStep";
    private static final String READER_NAME = "jpaPagingItemReader";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job jpaPagingItemReaderJob() throws Exception {
        return jobBuilderFactory.get(JOB_NAME)
            .start(jpaPagingItemReaderStep())
            .build();
    }

    @Bean
    public Step jpaPagingItemReaderStep() throws Exception {
        return stepBuilderFactory.get(STEP_NAME)
            .<Post, Post>chunk(CHUNK_SIZE)
            .reader(jpaPagingItemReader())
            .writer(jpaPagingItemWriter())
            .build();
    }

    @Bean
    public JpaPagingItemReader<Post> jpaPagingItemReader() throws Exception {
        return new JpaPagingItemReaderBuilder<Post>()
            .name(READER_NAME)
            .entityManagerFactory(entityManagerFactory)
            .pageSize(CHUNK_SIZE)
            .queryString("SELECT p FROM Post p")
            .build();
    }

    public ItemWriter<Post> jpaPagingItemWriter() {
        return list -> {
            for (Post post : list) {
                log.info("Current post = {}", post);
            }
        };
    }

}

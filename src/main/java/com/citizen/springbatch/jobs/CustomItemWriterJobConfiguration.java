package com.citizen.springbatch.jobs;

import com.citizen.springbatch.domain.Post;
import com.citizen.springbatch.domain.Post2;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
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
public class CustomItemWriterJobConfiguration {

    private static final int CHUNK_SIZE = 10;

    private static final String JOB_NAME = "customItemWriterJob";
    private static final String STEP_NAME = "customItemWriterStep";
    private static final String READER_NAME = "customItemWriterReader";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job customItemWriterJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(customItemWriterStep())
            .build();
    }

    @Bean
    public Step customItemWriterStep() {
        return stepBuilderFactory.get(STEP_NAME)
            .<Post, Post2>chunk(CHUNK_SIZE)
            .reader(customItemWriterReader())
            .processor(customItemWriterProcessor())
            .writer(customItemWriter())
            .build();
    }

    @Bean
    public JpaPagingItemReader<Post> customItemWriterReader() {
        return new JpaPagingItemReaderBuilder<Post>()
            .name(READER_NAME)
            .entityManagerFactory(entityManagerFactory)
            .pageSize(CHUNK_SIZE)
            .queryString("SELECT p FROM Post p")
            .build();
    }

    @Bean
    public ItemProcessor<Post, Post2> customItemWriterProcessor() {
        return post -> new Post2(post.getId(), post.getTitle(), post.getContent());
    }

    public ItemWriter<Post2> customItemWriter() {
        return items -> {
            for (Post2 post2 : items) {
                System.out.println("Change Post -> Post2 : " + post2);
                log.info("Change Post -> Post2 : {}", post2);
            }
        };
    }

}

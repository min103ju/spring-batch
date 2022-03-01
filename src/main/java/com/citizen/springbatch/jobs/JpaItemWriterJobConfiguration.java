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
import org.springframework.batch.item.database.JpaItemWriter;
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
public class JpaItemWriterJobConfiguration {

    private static final int CHUNK_SIZE = 10;

    private static final String JOB_NAME = "jpaItemWriterJob";
    private static final String STEP_NAME = "jpaItemWriterStep";
    private static final String READER_NAME = "jpaItemWriterReader";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory; // 영속성 관리를 위해 EntityManager 주입을 위해

    @Bean
    public Job jpaItemWriterJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(jpaItemWriterStep())
            .build();
    }

    @Bean
    public Step jpaItemWriterStep() {
        return stepBuilderFactory.get(STEP_NAME)
            .<Post, Post2>chunk(CHUNK_SIZE)
            .reader(jpaItemWriterReader())
            .processor(jpaItemWriterProcessor())
            .writer(jpaItemWriter())
            .build();
    }

    @Bean
    public JpaPagingItemReader<Post> jpaItemWriterReader() {
        return new JpaPagingItemReaderBuilder<Post>()
            .name(READER_NAME)
            .entityManagerFactory(entityManagerFactory)
            .pageSize(CHUNK_SIZE)
            .queryString("SELECT p FROM Post p")
            .build();
    }

    @Bean
    public ItemProcessor<Post, Post2> jpaItemWriterProcessor() {
        return post -> new Post2(post.getId(), post.getTitle(), post.getContent());
    }

    @Bean
    public JpaItemWriter<Post2> jpaItemWriter() {
        JpaItemWriter<Post2> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

}

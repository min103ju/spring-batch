package com.citizen.springbatch.jobs;

import com.citizen.springbatch.domain.Post;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * author : citizen103
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcBatchItemWriterJobConfiguration {

    private static final int CHUNK_SIZE = 10;

    private static final String JOB_NAME = "jdbcBatchItemWriterJob";
    private static final String STEP_NAME = "jdbcBatchItemWriterStep";
    private static final String READER_NAME = "jdbcBatchItemWriterReader";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job jdbcBatchItemWriterJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(jdbcBatchItemWriterStep())
            .build();
    }

    @Bean
    public Step jdbcBatchItemWriterStep() {
        return stepBuilderFactory.get(STEP_NAME)
            .<Post, Post>chunk(CHUNK_SIZE)
            .reader(jdbcBatchItemWriterReader())
            .writer(jdbcBatchItemWriter())
            .build();
    }

    @Bean
    public JdbcCursorItemReader<Post> jdbcBatchItemWriterReader() {
        return new JdbcCursorItemReaderBuilder<Post>()
            .verifyCursorPosition(false)
            .fetchSize(CHUNK_SIZE)
            .dataSource(dataSource)
            .rowMapper(new BeanPropertyRowMapper<>(Post.class))
            .sql("SELECT id, title, content FROM post")
            .name(READER_NAME)
            .build();
    }

    @Bean
    public JdbcBatchItemWriter<Post> jdbcBatchItemWriter() {
        return new JdbcBatchItemWriterBuilder<Post>()
            .dataSource(dataSource)
            .sql("insert into post2 values (:id, :title, :content)")
            .beanMapped()
            .build();
    }
}

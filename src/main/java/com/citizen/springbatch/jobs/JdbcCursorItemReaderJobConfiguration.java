package com.citizen.springbatch.jobs;

import com.citizen.springbatch.domain.Post;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
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
public class JdbcCursorItemReaderJobConfiguration {

    private static final int CHUNK_SIZE = 10;

    private static final String JOB_NAME = "jdbcCursorItemReaderJob";
    private static final String STEP_NAME = "jdbcCursorItemReaderStep";
    private static final String READER_NAME = "jdbcCursorItemReader";
    private static final String WRITER_NAME = "jdbcCursorItemWriter";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job jdbcCursorItemReaderJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(jdbcCursorItemReaderStep())
            .build();
    }

    @Bean
    private Step jdbcCursorItemReaderStep() {
        return stepBuilderFactory.get(STEP_NAME)
            .<Post, Post>chunk(CHUNK_SIZE)
            .reader(jdbcCursorItemReader())
            .writer(jdbcCursorItemWriter())
            .build();
    }

    private JdbcCursorItemReader<Post> jdbcCursorItemReader() {

        String sql = "SELECT id, title, content From Post";

        return new JdbcCursorItemReaderBuilder<Post>()
            .fetchSize(CHUNK_SIZE)
            .dataSource(dataSource)
            .rowMapper(new BeanPropertyRowMapper<>(Post.class))
            .sql(sql)
            .name(READER_NAME)
            .build();
    }

    private ItemWriter<Post> jdbcCursorItemWriter() {
        return list -> {
            for (Post post : list) {
                log.info("Current post = {}", post);
            }
        };
    }
}

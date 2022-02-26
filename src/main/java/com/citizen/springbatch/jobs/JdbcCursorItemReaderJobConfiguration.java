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
            // 첫 번째는 Reader에서 반환할 타입, 두 번째는 Writer에 인자로 넘어올 타입
            // chunk() 메소드에서 size를 지정할 경우 reader & writer가 묶일 트랜잭션 범위이다.
            .<Post, Post>chunk(CHUNK_SIZE)
            .reader(jdbcCursorItemReader())
            .writer(jdbcCursorItemWriter())
            .build();
    }

    private JdbcCursorItemReader<Post> jdbcCursorItemReader() {

        String sql = "SELECT id, title, content From Post";

        return new JdbcCursorItemReaderBuilder<Post>()
            .fetchSize(CHUNK_SIZE) // reader에서 조회할 데이터 양
            .dataSource(dataSource)
            // 쿼리 결과를 Java 인스턴스로 매핑하기 위한 Mapper
            // 커스텀하게 생성해서 할 수도 있지만, 매번 Mapper 클래스를 사용하는 것보다
            // 보편적으로 사용하는 BeanPropertyRowMapper를 사용하자
            .rowMapper(new BeanPropertyRowMapper<>(Post.class))
            .sql(sql)
            // reader의 이름을 지정
            // Bean이 아니며, Spring Batch의 ExecutionContext에 저장될 이름
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

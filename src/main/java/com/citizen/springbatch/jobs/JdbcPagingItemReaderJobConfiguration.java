package com.citizen.springbatch.jobs;

import com.citizen.springbatch.domain.Post;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

/**
 * author : citizen103
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class JdbcPagingItemReaderJobConfiguration {

    private static final int CHUNK_SIZE = 10;

    private static final String JOB_NAME = "jdbcPagingItemReaderJob";
    private static final String STEP_NAME = "jdbcPagingItemReaderStep";
    private static final String READER_NAME = "jdbcPagingItemReader";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    @Bean
    public Job jdbcPagingItemReaderJob() throws Exception {
        return jobBuilderFactory.get(JOB_NAME)
            .start(jdbcPagingItemReaderStep())
            .build();
    }

    @Bean
    public Step jdbcPagingItemReaderStep() throws Exception {
        return stepBuilderFactory.get(STEP_NAME)
            // 첫 번째는 Reader에서 반환할 타입, 두 번째는 Writer에 인자로 넘어올 타입
            // chunk() 메소드에서 size를 지정할 경우 reader & writer가 묶일 트랜잭션 범위이다.
            .<Post, Post>chunk(CHUNK_SIZE)
            .reader(jdbcPagingItemReader())
            .writer(jdbcPagingItemWriter())
            .build();
    }

    @Bean
    public JdbcPagingItemReader<Post> jdbcPagingItemReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<Post>()
            .pageSize(CHUNK_SIZE)
            .fetchSize(CHUNK_SIZE)
            .dataSource(dataSource)
            .rowMapper(new BeanPropertyRowMapper<>(Post.class))
            // PagingItemReader에서는 PagingQueryProvider를 통해 쿼리를 생성한다.
            .queryProvider(createQueryProvider())
            .name(READER_NAME)
            .build();

    }

    @Bean
    public PagingQueryProvider createQueryProvider() throws Exception {

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);

        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("id, title, content");
        queryProvider.setFromClause("from Post");
        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }

    public ItemWriter<Post> jdbcPagingItemWriter() {
        return list -> {
            for (Post post : list) {
                log.info("Current post = {}", post);
            }
        };
    }
}

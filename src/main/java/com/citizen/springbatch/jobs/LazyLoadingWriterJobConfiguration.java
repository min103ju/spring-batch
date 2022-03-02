package com.citizen.springbatch.jobs;

import com.citizen.springbatch.domain.Post;
import com.citizen.springbatch.domain.PurchaseOrder;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
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
public class LazyLoadingWriterJobConfiguration {

    private static final int CHUNK_SIZE = 10;

    private static final String JOB_NAME = "LazyLoadingWriterJob";
    private static final String STEP_NAME = "LazyLoadingWriterStep";
    private static final String READER_NAME = "LazyLoadingWriterReader";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job lazyLoadingWriterJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .preventRestart()
            .start(lazyLoadingWriterStep())
            .build();
    }

    @Bean
    @JobScope
    public Step lazyLoadingWriterStep() {
        return stepBuilderFactory.get(STEP_NAME)
            .<PurchaseOrder, Post>chunk(CHUNK_SIZE)
            .reader(lazyLoadingWriterReader())
            .writer(lazyLoadingWriter())
            .build();
    }

    @Bean
    public JpaPagingItemReader<PurchaseOrder> lazyLoadingWriterReader() {
        return new JpaPagingItemReaderBuilder<PurchaseOrder>()
            .name(READER_NAME)
            .entityManagerFactory(entityManagerFactory)
            .pageSize(CHUNK_SIZE)
            .queryString("SELECT p FROM PurchaseOrder p")
            .build();
    }

    private ItemWriter<Post> lazyLoadingWriter() {
        return items -> {
            log.info(">>>>>>>>> Items : {}", items);
            for (Post post : items) {
                log.info(">>>>>>>>> Post : {}", post);
            }
        };
    }

}

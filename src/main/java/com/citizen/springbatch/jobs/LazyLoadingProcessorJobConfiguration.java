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
public class LazyLoadingProcessorJobConfiguration {

    private static final int CHUNK_SIZE = 10;

    private static final String JOB_NAME = "lazyLoadingProcessorJob";
    private static final String STEP_NAME = "lazyLoadingProcessorStep";
    private static final String READER_NAME = "lazyLoadingProcessorReader";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job lazyLoadingProcessorJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .preventRestart()
            .start(lazyLoadingProcessorStep())
            .build();
    }

    @Bean
    @JobScope
    public Step lazyLoadingProcessorStep() {
        return stepBuilderFactory.get(STEP_NAME)
            .<PurchaseOrder, Post>chunk(CHUNK_SIZE)
            .reader(lazyLoadingProcessorReader())
            .processor(lazyLoadingProcessor())
            .writer(lazyLoadingProcessorWriter())
            .build();
    }

    @Bean
    public JpaPagingItemReader<PurchaseOrder> lazyLoadingProcessorReader() {
        return new JpaPagingItemReaderBuilder<PurchaseOrder>()
            .name(READER_NAME)
            .entityManagerFactory(entityManagerFactory)
            .pageSize(CHUNK_SIZE)
            .queryString("SELECT p FROM PurchaseOrder p")
            .build();
    }

    public ItemProcessor<PurchaseOrder, Post> lazyLoadingProcessor() {
        log.info("Lazy Loading Check");
        return purchaseOrder -> Post
            .of(purchaseOrder.getMemo(), String.valueOf(purchaseOrder.getProductList().size()));
    }

    private ItemWriter<Post> lazyLoadingProcessorWriter() {
        return items -> {
            log.info(">>>>>>>>> Items : {}", items);
            for (Post post : items) {
                log.info(">>>>>>>>> Post : {}", post);
            }
        };
    }

}

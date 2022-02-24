package com.citizen.springbatch.jobs;

import com.citizen.springbatch.domain.History;
import com.citizen.springbatch.domain.PurchaseOrder;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author : citizen103
 */
@RequiredArgsConstructor
@Configuration
public class EntityContextConfiguration {

    private final String JOB_NAME = "entityContextJob";
    private final String STEP_NAME = "entityContextStep";

    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(step())
            .build();
    }

    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
            .<PurchaseOrder, History>chunk(100)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build();
    }

    public JpaPagingItemReader<PurchaseOrder> reader() {
        JpaPagingItemReader<PurchaseOrder> reader = new JpaPagingItemReader<>();
        reader.setQueryString("select o from PurchaseOrder o");
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setPageSize(100);

        return reader;
    }

    public ItemProcessor<PurchaseOrder, History> processor() {
        return item -> History.builder()
            .purchaseOrderId(item.getId())
            .productList(item.getProductList())
            .build();
    }

    public JpaItemWriter<History> writer() {
        JpaItemWriter<History> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

}


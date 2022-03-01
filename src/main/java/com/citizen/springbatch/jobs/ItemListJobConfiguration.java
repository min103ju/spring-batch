package com.citizen.springbatch.jobs;

import com.citizen.springbatch.domain.Post;
import com.citizen.springbatch.domain.Post2;
import com.citizen.springbatch.tasklet.ItemListProcessor;
import java.util.List;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author : citizen103
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ItemListJobConfiguration {

    private static final int CHUNK_SIZE = 10;

    private static final String JOB_NAME = "itemListJob";
    private static final String STEP_NAME = "itemListStep";
    private static final String READER_NAME = "itemListReader";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job itemListJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(itemListStep())
            .build();
    }

    @Bean
    public Step itemListStep() {
        return stepBuilderFactory.get(STEP_NAME)
            .<Post, List<Post2>>chunk(CHUNK_SIZE)
            .reader(itemListReader())
            .processor(itemListProcessor())
            .writer(itemListWriter())
            .build();
    }

    public ItemProcessor<Post, List<Post2>> itemListProcessor() {
        return new ItemListProcessor();
    }

    public JpaPagingItemReader<Post> itemListReader() {
        JpaPagingItemReader<Post> reader = new JpaPagingItemReader<>();
        reader.setPageSize(CHUNK_SIZE);
        reader.setQueryString("SELECT p From Post p");
        reader.setEntityManagerFactory(entityManagerFactory);
        return reader;
    }

    public JpaItemWriter<List<Post2>> itemListWriter() {
        JpaItemWriter<List<Post2>> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

}

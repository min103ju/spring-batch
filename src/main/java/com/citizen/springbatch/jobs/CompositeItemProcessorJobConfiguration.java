package com.citizen.springbatch.jobs;

import com.citizen.springbatch.domain.Post;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author : citizen103
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class CompositeItemProcessorJobConfiguration {

    private static final int CHUNK_SIZE = 10;

    private static final String JOB_NAME = "compositeItemProcessorJob";
    private static final String STEP_NAME = "compositeItemProcessorStep";
    private static final String READER_NAME = "compositeItemProcessorReader";

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public Job compositeItemProcessorJob() {
        return jobBuilderFactory.get(JOB_NAME)
            .preventRestart()
            .start(compositeItemProcessorStep())
            .build();
    }

    @Bean
    @JobScope
    public Step compositeItemProcessorStep() {
        return stepBuilderFactory.get(STEP_NAME)
            .<Post, String>chunk(CHUNK_SIZE)
            .reader(compositeItemProcessorReader())
            .processor(compositeItemProcessor())
            .writer(compositeItemProcessorWriter())
            .build();
    }

    @Bean
    public JpaPagingItemReader<Post> compositeItemProcessorReader() {
        return new JpaPagingItemReaderBuilder<Post>()
            .name(READER_NAME)
            .entityManagerFactory(entityManagerFactory)
            .pageSize(CHUNK_SIZE)
            .queryString("SELECT p FROM Post p")
            .build();
    }

    @Bean
    public CompositeItemProcessor<Post, String> compositeItemProcessor() {
        List<ItemProcessor> delegates = new ArrayList<>(2);
        delegates.add(processor1());
        delegates.add(processor2());

        CompositeItemProcessor processor = new CompositeItemProcessor();

        processor.setDelegates(delegates);

        return processor;
    }

    public ItemProcessor<Post, String> processor1() {
        return post -> post.getTitle();
    }

    public ItemProcessor<String, String> processor2() {
        return title -> "Post의 제목은 " + title;
    }

    private ItemWriter<String> compositeItemProcessorWriter() {
        return items -> {
            for (String message : items) {
                log.info("message = {}", message);
            }
        };
    }
}

package com.citizen.springbatch.jobs;

import com.citizen.springbatch.domain.Post;
import com.citizen.springbatch.tasklet.PostItemProcessor;
import java.util.HashMap;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author : citizen103
 */
@Slf4j
@RequiredArgsConstructor
@Configuration
public class StepScopeWarningConfiguration {

    private final String JOB_NAME = "stepScopeWarningJob";
    private final String STEP_NAME = "stepScopeWarningStep";

    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
            .incrementer(new RunIdIncrementer())
            .start(step())
            .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
            .<Post, Post>chunk(1)
            .reader(reader(null))
            .processor(processor())
            .writer(writer())
            .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Post> reader(@Value("#{jobParameters[title]}") String title) {

        String sql = "SELECT p FROM Post p WHERE p.title=:title";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("title", title);

        JpaPagingItemReader<Post> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(sql);
        reader.setParameterValues(paramMap);
        reader.setPageSize(10);

        return reader;
    }

    @Bean
    public ItemProcessor<Post, Post> processor() {
        return new PostItemProcessor();
    }

    @Bean
    public ItemWriter<Post> writer() {
        JpaItemWriter<Post> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

}

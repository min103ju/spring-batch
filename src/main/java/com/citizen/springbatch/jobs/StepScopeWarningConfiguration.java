package com.citizen.springbatch.jobs;

import static com.citizen.springbatch.jobs.StepScopeWarningConfiguration.JOB_NAME;

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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * author : citizen103
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "job.name", havingValue = JOB_NAME)
@Configuration
public class StepScopeWarningConfiguration {

    public static final String JOB_NAME = "stepScopeWarningJob";
    private static final String STEP_NAME = "stepScopeWarningStep";

    private final EntityManagerFactory entityManagerFactory;
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get(JOB_NAME)
            .start(step())
            .build();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get(STEP_NAME)
            .<Post, Post>chunk(1)
            .reader(reader())
            .processor(processor())
            .writer(writer())
            .build();
    }

    private ItemReader<Post> reader() {

        String sql = "SELECT p FROM Post p WHERE p.title=:title";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("title", "testPost");

        JpaPagingItemReader<Post> reader = new JpaPagingItemReader<>();
        reader.setEntityManagerFactory(entityManagerFactory);
        reader.setQueryString(sql);
        reader.setParameterValues(paramMap);
        reader.setPageSize(10);

        return reader;
    }

    private ItemProcessor<Post, Post> processor() {
        return new PostItemProcessor();
    }

    private ItemWriter<Post> writer() {
        JpaItemWriter<Post> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

}

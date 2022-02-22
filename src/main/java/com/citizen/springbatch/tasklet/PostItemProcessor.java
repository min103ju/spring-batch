package com.citizen.springbatch.tasklet;

import com.citizen.springbatch.domain.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

/**
 * author : citizen103
 */
@Slf4j
public class PostItemProcessor implements ItemProcessor<Post, Post> {

    @Override
    public Post process(Post post) throws Exception {

        log.info("title = {}, content = {}", post.getTitle(), post.getContent());

        return post;
    }
}

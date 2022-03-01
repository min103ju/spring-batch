package com.citizen.springbatch.tasklet;

import com.citizen.springbatch.domain.Post;
import com.citizen.springbatch.domain.Post2;
import java.util.Arrays;
import java.util.List;
import org.springframework.batch.item.ItemProcessor;

/**
 * author : citizen103
 */
public class ItemListProcessor implements ItemProcessor<Post, List<Post2>> {

    @Override
    public List<Post2> process(Post item) throws Exception {
        return Arrays.asList(
            new Post2(item.getId(), item.getTitle(), item.getContent()),
            new Post2(item.getId() + 1L, item.getTitle() + "1", item.getContent() + "1"),
            new Post2(item.getId() + 2L, item.getTitle() + "2", item.getContent() + "2")
        );
    }
}

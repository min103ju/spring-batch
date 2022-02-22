package com.citizen.springbatch.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;

/**
 * author : citizen103
 */
@Getter
@Entity
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    public static Post of(String title, String content) {
        Post post = new Post();
        post.title = title;
        post.content = content;
        return post;
    }

}

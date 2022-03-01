package com.citizen.springbatch.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * author : citizen103
 */
@ToString
@Setter
@Getter
@NoArgsConstructor
@Entity
public class Post2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    public Post2(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public static Post2 of(String title, String content) {
        Post2 post = new Post2();
        post.title = title;
        post.content = content;
        return post;
    }

}

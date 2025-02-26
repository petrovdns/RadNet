package com.petrovdns.radnet.entity;

import com.petrovdns.radnet.entity.abstractEntity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Comment extends AbstractEntity {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    @JoinColumn(name = "user_id")
    private Long userId;

    @Column(columnDefinition = "text", nullable = false)
    private String message;
}

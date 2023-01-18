package com.rom.domain.comment.domain;

import com.rom.domain.common.BaseEntity;
import com.rom.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.Id;

@Entity
@Getter
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}

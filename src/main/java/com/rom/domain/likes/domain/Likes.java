package com.rom.domain.likes.domain;

import com.rom.domain.common.BaseEntity;
import com.rom.domain.common.Status;
import com.rom.domain.record.domain.Record;
import com.rom.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Likes extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    @Builder
    public Likes(Long id, User user, Record record) {
        this.id = id;
        this.user = user;
        this.record = record;
    }
}
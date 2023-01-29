package com.rom.domain.comment.domain;

import com.rom.domain.common.BaseEntity;
import com.rom.domain.common.Status;
import com.rom.domain.diary.domain.Diary;
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
@Where(clause = "status = 'ACTIVE'")
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id")
    private Record record;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public void updateStatus(Status status) {
        this.status = status;
    }

    @Builder
    public Comment(Long id, String content, User user, Record record) {
        this.id = id;
        this.content = content;
        this.user = user;
        this.record = record;
    }
}

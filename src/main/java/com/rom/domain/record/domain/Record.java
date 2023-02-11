package com.rom.domain.record.domain;

import com.rom.domain.common.BaseEntity;
import com.rom.domain.common.Status;
import com.rom.domain.diary.domain.Diary;
import com.rom.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Setter
@Where(clause = "status = 'ACTIVE'")
public class Record extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date date;

    private String title;

    private String content;

    @Column(name="img_url")
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @Enumerated(EnumType.STRING)
    private Status status = Status.valueOf("ACTIVE");

    @Builder
    public Record(User user, Diary diary, Date date, String title, String content, String imgUrl){
        this.user = user;
        this.diary = diary;
        this.date = date;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
    }

    public void updateDate(Date date) {
        this.date = date;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateImg(String imgUrl) {
        this.imgUrl = imgUrl;
    }

}

package com.rom.domain.diary.domain;

import com.rom.domain.common.BaseEntity;
import com.rom.domain.record.domain.Record;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Where(clause = "status = 'ACTIVE'")
@Entity
public class Diary extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private DiaryType diaryType;

    @OneToMany(mappedBy = "diary")
    private List<Record> records = new ArrayList<>();

    @Builder
    public Diary(Long id, String name, DiaryType diaryType) {
        this.id = id;
        this.name = name;
        this.diaryType = diaryType;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateDiaryType(DiaryType diaryType){ this.diaryType = diaryType; }

}

package com.rom.domain.diary.dto;

import com.rom.domain.diary.domain.DiaryType;
import lombok.Builder;
import lombok.Data;

@Data
public class DiaryDetailRes {

    private Long id;

    private String name;

    private String diaryType;

    @Builder
    public DiaryDetailRes(Long id, String name, String diaryType) {
        this.id = id;
        this.name = name;
        this.diaryType = diaryType;
    }

}

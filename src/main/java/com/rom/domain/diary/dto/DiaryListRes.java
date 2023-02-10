package com.rom.domain.diary.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class DiaryListRes {

    private Long id;

    private String name;

    private String nickname;

    private String diaryType;

    @Builder
    public DiaryListRes(Long id, String name, String nickname, String diaryType) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.diaryType = diaryType;
    }

}

package com.rom.domain.diary.dto;

import com.rom.domain.user.dto.UserDetailRes;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class DiaryDetailRes {

    private Long id;

    private String name;

    private String diaryType;

    private List<UserDetailRes> users;

    private List<DiaryRecordDetailRes> records;

    @Builder
    public DiaryDetailRes(Long id, String name, String diaryType, List<UserDetailRes> users, List<DiaryRecordDetailRes> records) {
        this.id = id;
        this.name = name;
        this.diaryType = diaryType;
        this.users = users;
        this.records = records;
    }

}

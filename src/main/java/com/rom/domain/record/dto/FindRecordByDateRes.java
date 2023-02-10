package com.rom.domain.record.dto;

import com.rom.domain.diary.dto.DiaryRecordDetailRes;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FindRecordByDateRes {

    private Long diaryId;

    private String diaryName;

    private List<DiaryRecordDetailRes> records;

    @Builder
    public FindRecordByDateRes(Long diaryId, String diaryName, List<DiaryRecordDetailRes> records) {
        this.diaryId = diaryId;
        this.diaryName = diaryName;
        this.records = records;
    }

}

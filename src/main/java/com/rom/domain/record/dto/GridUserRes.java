package com.rom.domain.record.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class GridUserRes {

    private String nickname;

    private String imgUrl;

    private List<GridRecordRes> records;

    @Builder
    public GridUserRes(String nickname, String imgUrl, List<GridRecordRes> records) {
        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.records = records;
    }


}

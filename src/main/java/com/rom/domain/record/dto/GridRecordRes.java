package com.rom.domain.record.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class GridRecordRes {

    private Long id;

    private String title;

    private String imgUrl;  //일기 사진

    @Builder
    public GridRecordRes(Long id, String title, String imgUrl) {
        this.id = id;
        this.title = title;
        this.imgUrl = imgUrl;
    }
}

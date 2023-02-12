package com.rom.domain.record.dto;

import lombok.Builder;
import lombok.Data;


//그리드뷰 -> 상세페이지에서 사용하는 DTO
@Data
public class GridResultDetailRes {
    private Long id;  //다이어리 id

    private String name; //다이어리 이름

    private GridUserRes user;

    @Builder
    public GridResultDetailRes(Long id, String name, GridUserRes user) {
        this.id = id;
        this.name = name;
        this.user = user;
    }
}

package com.rom.domain.record.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

//그리드뷰에서 사용하는 DTO
@Data
public class GridResultRes {

    private Long id;  //다이어리 id

    private String name; //다이어리 이름

    private List<GridUserRes> users;

    @Builder
    public GridResultRes(Long id, String name, List<GridUserRes> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }
}

package com.rom.domain.likes.dto;

import com.rom.domain.likes.domain.Likes;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
public class LikeRes {

    @Schema(type = "int", example = "3", description = "일기에 달린 좋아요의 개수입니다.")
    private int count;    //좋아요 개수

    @Builder
    public LikeRes(int count) {
        this.count = count;
    }
}

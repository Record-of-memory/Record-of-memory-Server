package com.rom.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ResultCommentRes {

    @Schema(type = "int", example = "1", description = "일기에 달린 댓글의 개수입니다.")
    private int count;    //댓글 개수

    private List<FindCommentRes> data;

    @Builder
    public ResultCommentRes(int count, List<FindCommentRes> data) {
        this.count = count;
        this.data = data;
    }

}

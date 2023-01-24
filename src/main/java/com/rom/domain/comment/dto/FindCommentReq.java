package com.rom.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FindCommentReq {

    @Schema(type = "Long", example = "1", description = "조회할 댓글이 속해있는 일기의 ID입니다.")
    @NotNull
    private Long recordId;
}

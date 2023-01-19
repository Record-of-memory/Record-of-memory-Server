package com.rom.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WriteCommentReq {

    @Schema(type = "string", example = "첫 댓글", description = "댓글 내용")
    @NotNull
    private String content;

    @Schema(type = "int", example = "1", description = "댓글을 작성할 일기의 ID")
    @NotNull
    private Long recordId;

}

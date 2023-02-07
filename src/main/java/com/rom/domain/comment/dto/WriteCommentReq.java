package com.rom.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class WriteCommentReq {

    @Schema(type = "int", example = "1", description = "댓글을 작성할 일기의 ID")
    @NotNull
    private Long recordId;

    @Schema(type = "string", example = "이렇게 일기에 댓글을 달 수 있구나!", description = "댓글 내용")
    @NotEmpty
    private String content;


}

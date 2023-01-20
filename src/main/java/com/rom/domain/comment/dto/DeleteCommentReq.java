package com.rom.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteCommentReq {

    @Schema(type = "Long", example = "1", description = "삭제할 댓글의 ID입니다.")
    @NotNull
    private Long commentId;
}

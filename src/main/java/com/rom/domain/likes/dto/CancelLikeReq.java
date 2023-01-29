package com.rom.domain.likes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CancelLikeReq {

    @Schema(type = "Long", example = "1", description = "취소할 좋아요의 ID입니다.")
    @NotNull
    private Long likeId;

}

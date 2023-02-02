package com.rom.domain.likes.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeReq {

    @Schema(type = "Long", example = "1", description = "좋아요를 누를 일기의 ID입니다.")
    @NotNull
    private Long recordId;
}

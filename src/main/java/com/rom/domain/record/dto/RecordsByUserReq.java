package com.rom.domain.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class RecordsByUserReq {

    @Schema(type = "Long", example = "1", description = "다이어리의 ID입니다.")
    @NotNull
    private Long diaryId;

    @Schema(type = "Long", example = "1", description = "유저의 ID입니다.")
    @NotNull
    private Long userId;

}

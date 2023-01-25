package com.rom.domain.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LeaveDiaryReq {

    @Schema(type = "int", example = "2", description = "다이어리 ID")
    @NotNull
    private Long diaryId;

}

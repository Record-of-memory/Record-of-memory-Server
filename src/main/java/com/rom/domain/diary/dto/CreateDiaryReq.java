package com.rom.domain.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateDiaryReq {

    @Schema(type = "string", example = "첫 다이어리", description = "다이어리 이름입니다.")
    @NotNull
    private String name;

    @Schema(type = "string", example = "ALONE / WITH", description = "ALONE, WITH / 다이어리 타입입니다.")
    @NotNull
    private String diaryType;
}

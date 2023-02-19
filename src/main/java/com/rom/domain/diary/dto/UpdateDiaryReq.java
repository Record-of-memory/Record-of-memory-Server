package com.rom.domain.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateDiaryReq {

    @Schema(type = "Long", example = "1", description = "변경할 다이어리의 ID입니다.")
    @NotNull
    private Long diaryId;

    @Schema(type = "string", example = "두 번째 다이어리", description = "변경할 다이어리 이름입니다.")
    @NotBlank
    private String name;
}

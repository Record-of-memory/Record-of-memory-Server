package com.rom.domain.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecordsByDiaryReq {
    @Schema(type = "Long", example = "1", description = "일기 목록을 불러올 다이어리의 ID입니다.")
    @NotNull
    private Long DiaryId;
}

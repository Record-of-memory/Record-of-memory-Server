package com.rom.domain.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class RecordDateReq {

    @Schema(type = "Long", example = "1", description = "다이어리의 ID입니다.")
    @NotNull
    private Long diaryId;

    @Schema(type = "Date", example = "2023-01-24", description = "일기가 작성된 날짜입니다.")
    @NotNull
    private Date date;
}

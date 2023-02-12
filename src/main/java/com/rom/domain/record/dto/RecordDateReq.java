package com.rom.domain.record.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class RecordDateReq {

    @Schema(type = "Long", example = "1", description = "다이어리의 ID입니다.")
    @NotNull
    private Long diaryId;

    @Schema(type = "string", example = "2023-01-24", description = "일기가 작성된 날짜입니다.")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private Date date;
}

package com.rom.domain.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RecordDetailReq {
    @Schema(type = "Long", example = "1", description = "상세정보를 확인할 일기의 ID입니다.")
    @NotNull
    private Long recordId;
}

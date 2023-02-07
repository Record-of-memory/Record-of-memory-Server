package com.rom.domain.record.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DeleteRecordReq {
    @Schema(type = "Long", example = "1", description = "삭제할 일기의 ID입니다.")
    @NotNull
    private Long recordId;
}

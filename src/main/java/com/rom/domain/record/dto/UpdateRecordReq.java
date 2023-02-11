package com.rom.domain.record.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class UpdateRecordReq {
    @Schema(type = "Long", example = "1", description = "수정할 일기의 ID입니다.")
    @NotNull
    private Long recordId;

    @Schema(type = "string", example = "2023-01-26", description = "일기의 날짜 수정")
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private Date date;

    @Schema(type = "string", example = "핑크뮬리가 예뻤다", description = "수정할 일기 제목")
    private String title;

    @Schema(type = "string", example = "심지어 날씨도 엄청 좋은거야 적당한 햇빛과 적당한 바람까지 완벽한 날씨였다.", description = "수정할 일기 내용")
    private String content;
}

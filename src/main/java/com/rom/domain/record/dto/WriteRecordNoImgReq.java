package com.rom.domain.record.dto;

import com.rom.domain.record.domain.Record;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data   // 이미지 포함 X -> content 필수
public class WriteRecordNoImgReq {

    @Schema(type = "Long", example = "1", description = "일기를 작성할 다이어리의 ID")
    @NotNull
    private Long diaryId;

    @Schema(type = "date", example = "2023-01-21", description = "일기가 작성될 날짜")
    @NotNull
    private Date date;

    @Schema(type = "string", example = "핑크뮬리가 예뻤던 날", description = "일기 제목")
    @NotNull
    private String title;

    @Schema(type = "string", example = "하남 갈 일이 있어서 가족들이랑 하남 갔다가 짱 예쁜 핑크뮬리 보고왔다.", description = "일기 내용")
    @NotNull
    private String content;

}

package com.rom.domain.diary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InviteUserReq {

    @Schema(type = "string", example = "jinpark99@naver.com", description = "이메일")
    @NotBlank
    @Email
    private String email;

    @Schema(type = "int", example = "1", description = "다이어리 ID")
    @NotNull
    private Long diaryId;

}

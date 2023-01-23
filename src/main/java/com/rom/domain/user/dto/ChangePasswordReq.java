package com.rom.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChangePasswordReq {

    @Schema(type = "string", example = "string12", description = "기존 비밀번호 입니다.")
    @NotBlank
    private String oldPassword;

    @Schema(type = "string", example = "string123", description = "새로운 비밀번호 입니다.")
    @NotBlank
    private String newPassword;

}

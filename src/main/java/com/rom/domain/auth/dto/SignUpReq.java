package com.rom.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data
public class SignUpReq {

    @Schema( type = "string", example = "string", description="닉네임 입니다.")
    @NotBlank
    private String nickname;

    @Schema( type = "string", example = "string@aa.bb", description="계정 이메일 입니다.")
    @NotBlank
    @Email
    private String email;

    @Schema( type = "string", example = "string", description="계정 비밀번호 입니다.")
    @NotBlank
    private String password;

}

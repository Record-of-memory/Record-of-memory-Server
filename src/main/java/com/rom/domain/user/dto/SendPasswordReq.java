package com.rom.domain.user.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SendPasswordReq {

    @Schema(type = "string", example = "nana@gmail.com", description = "가입 시 작성한 이메일 주소입니다.")
    private String email;

    @Schema(type = "string", example = "나나", description = "가입 시 작성한 닉네임입니다.")
    private String nickname;
}

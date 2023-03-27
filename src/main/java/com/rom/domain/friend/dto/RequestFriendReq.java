package com.rom.domain.friend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestFriendReq {

    @Schema(type = "string", example = "example@gmail.com", description = "친구 추가 요청을 보내는 이메일 입니다.")
    @NotBlank
    @Email
    private String email;

}

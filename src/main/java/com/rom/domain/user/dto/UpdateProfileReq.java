package com.rom.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileReq {

    @Schema(type = "string", example = "nickname", description = "닉네임")
    @NotBlank
    private String nickname;

    @Schema(type = "file", description = "이미지 파일")
    private MultipartFile profileImg;

}

package com.rom.domain.comment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FindCommentRes {

    @Schema(type = "String", example = "보라", description = "댓글 작성자의 닉네임입니다.")
    private String nickname;

    @Schema(type = "String", example = "", description = "댓글 작성자의 프로필 사진 url입니다.")
    private String imageUrl;

    @Schema(type = "String", example = "이렇게 일기에 댓글을 달 수 있구나!", description = "댓글의 내용입니다.")
    private String content;

    @Schema(type = "LocalDateTime", example = "22.11.25", description = "댓글을 작성한 날짜입니다.")
    private LocalDateTime createdAt;

    @Builder
    public FindCommentRes(String nickname, String imageUrl, String content, LocalDateTime createdAt) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.content = content;
        this.createdAt = createdAt;
    }

}

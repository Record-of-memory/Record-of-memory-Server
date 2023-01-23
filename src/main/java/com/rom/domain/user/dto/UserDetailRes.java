package com.rom.domain.user.dto;

import com.rom.domain.user.domain.Role;
import lombok.Builder;
import lombok.Data;

@Data
public class UserDetailRes {

    private String email;
    private String nickname;
    private String imageUrl;
    private Role role;

    @Builder
    public UserDetailRes(String email, String nickname, String imageUrl, Role role) {
        this.email = email;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.role = role;
    }

}

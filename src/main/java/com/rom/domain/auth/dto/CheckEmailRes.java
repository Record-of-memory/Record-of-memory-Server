package com.rom.domain.auth.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class CheckEmailRes {

    private boolean isEmail;

    @Builder
    public CheckEmailRes(boolean isEmail) {
        this.isEmail = isEmail;
    }
}

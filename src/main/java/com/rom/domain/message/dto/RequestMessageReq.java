package com.rom.domain.message.dto;

import lombok.Data;

@Data
public class RequestMessageReq {

    private String targetToken;

    private String title;

    private String body;
}

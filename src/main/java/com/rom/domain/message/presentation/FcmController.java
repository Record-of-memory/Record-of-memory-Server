package com.rom.domain.message.presentation;

import com.rom.domain.message.application.FcmService;
import com.rom.domain.message.dto.RequestMessageReq;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/api/fcm")
    public ResponseEntity<?> pushMessage(@RequestBody RequestMessageReq messageReq) throws IOException {

        return fcmService.sendMessageTo(messageReq);
    }
}

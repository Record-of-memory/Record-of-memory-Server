package com.rom.domain.friend.presentation;

import com.rom.domain.friend.application.FriendService;
import com.rom.domain.friend.dto.RequestFriendReq;
import com.rom.domain.user.dto.UserDetailRes;
import com.rom.global.config.security.token.CurrentUser;
import com.rom.global.config.security.token.UserPrincipal;
import com.rom.global.payload.ErrorResponse;
import com.rom.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Friend", description = "Friend API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/friend")
public class FriendController {

    private final FriendService friendService;

    @Operation(summary = "이메일로 친구 요청", description = "이메일로 친구 요청 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 요청 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "친구 요청 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/request")
    public ResponseEntity<?> requestFriendShip(
            @Parameter(description = "AccessToken을 Authorization 헤더로 보내주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 RequestFriendReq를 참고해주세요.", required = true) @RequestBody RequestFriendReq requestFriendReq
    ) {
        return friendService.requestFriendShip(requestFriendReq, userPrincipal);
    }

}

package com.rom.domain.user.presentation;

import com.rom.domain.user.application.UserService;
import com.rom.domain.user.dto.ChangePasswordReq;
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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "Users API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "ID로 유저 정보 조회", description = "ID로 유저 정보를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ID로 유저 정보 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "ID로 유저 정보 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findUserById(
            @Parameter(description = "유저 ID 입니다,", required = true) @PathVariable Long id
    ) {
        return userService.findUserById(id);
    }

    @Operation(summary = "AccessToken 으로 유저 정보 조회", description = "AccessToken 으로 유저 정보를 조회합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "AccessToken 으로 유저 정보 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "AccessToken 으로 유저 정보 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/me")
    public ResponseEntity<?> findUserByAccessToken(
            @Parameter(description = "AccessToken을 Authorization 헤더로 보내주세요.", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.findUserByAccessToken(userPrincipal);
    }

    @Operation(summary = "비밀번호 변경", description = "비밀번호를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호가 변경되었습니다.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "비밀번호 변경을 실패했습니다.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/me/password")
    public ResponseEntity<?> changePassword(
            @Parameter(description = "AccessToken을 Authorization 헤더로 보내주세요.", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas에 ChangePasswordReq 를 참고해주세요", required = true) @Valid @RequestBody ChangePasswordReq changePasswordReq
    ) {
        return userService.changePassword(userPrincipal, changePasswordReq);
    }
}

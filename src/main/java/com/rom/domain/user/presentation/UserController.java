package com.rom.domain.user.presentation;

import com.rom.domain.user.application.UserService;
import com.rom.domain.user.dto.ChangePasswordReq;
import com.rom.domain.user.dto.SendPasswordReq;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Tag(name = "Users", description = "Users API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    @Operation(summary = "Email로 유저 조회", description = "Email로 유저를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = UserDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "유저 정보 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping
    public ResponseEntity<?> findUsers(
            @Parameter(description = "유저 Email 입니다.", required = true) @RequestParam String email
    ) {
        return userService.findUsers(email);
    }

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

    @Operation(summary = "이름, 프로필 사진 변경")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이름, 프로필이 업데이트 되었습니다.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "이름, 프로필 업데이트에 실패했습니다.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProfile(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "닉네임을 입력해주세요.", required = true) @Valid @RequestPart String nickname,
            @Parameter(description = "이미지를 업로드해주세요.") @RequestPart(required = false) MultipartFile profileImg

    ) throws IOException {
        return userService.updateProfile(userPrincipal, nickname, profileImg);
    }

    @Operation(summary = "유저 탈퇴")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저가 탈퇴되었습니다.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "유저 탈퇴에 실패했습니다.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return userService.deleteUser(userPrincipal);
    }

    @Operation(summary = "이메일로 임시 비밀번호 전송")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "이메일로 임시 번호를 전송했습니다.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "이메일로 임시 번호를 전송하지 못했습니다..", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/me/send")
    public ResponseEntity<?> sendTemporaryPassword(
            @Parameter(description = "SendPasswordReq를 참고해주세요.", required = true) @Valid @RequestBody SendPasswordReq sendPasswordReq
    ){
        return userService.sendTemporaryPassword(sendPasswordReq);
    }
}

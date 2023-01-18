package com.rom.domain.user.presentation;

import com.rom.domain.user.application.UserService;
import com.rom.domain.user.dto.UserDetailRes;
import com.rom.global.payload.ErrorResponse;
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

@Tag(name = "Users", description = "Users API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
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

}

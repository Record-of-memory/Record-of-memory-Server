package com.rom.domain.likes.presentation;

import com.rom.domain.likes.application.LikesService;
import com.rom.domain.likes.dto.LikeReq;
import com.rom.domain.likes.dto.LikeRes;
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

@Tag(name = "Likes", description = "Likes API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/likes")
public class LikesController {

    private final LikesService likesService;

    //좋아요
    @Operation(summary = "좋아요", description = "좋아요를 누릅니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "좋아요 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "좋아요 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping
    public ResponseEntity<?> pressLike(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 LikeReq를 참고해주세요.", required = true) @Valid @RequestBody LikeReq likeReq
            ){
        return likesService.pressLike(userPrincipal, likeReq);
    }


    //좋아요 취소
    @Operation(summary = "좋아요 취소", description = "좋아요를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "좋아요 취소 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "좋아요 취소 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelLike(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "좋아요의 ID입니다.", required = true) @Valid @PathVariable(value = "id") Long likeId
    ){
        return likesService.cancelLike(userPrincipal, likeId);
    }


    //좋아요 개수 조회
    @Operation(summary = "좋아요 클릭 여부", description = "일기에 달린 좋아요의 개수를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "좋아요 개수 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LikeRes.class))}),
            @ApiResponse(responseCode = "400", description = "좋아요 개수 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> findLikesCount(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "일기의 id입니다.", required = true) @Valid @PathVariable("id") Long recordId
    ){
        return likesService.isLikeClicked(userPrincipal, recordId);
    }
}

package com.rom.domain.diary.presentation;

import com.rom.domain.diary.application.DiaryService;
import com.rom.domain.diary.dto.*;
import com.rom.global.config.security.token.CurrentUser;
import com.rom.global.config.security.token.UserPrincipal;
import com.rom.global.payload.ErrorResponse;
import com.rom.global.payload.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Diaries", description = "Diaries API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @Operation(summary = "다이어리 생성", description = "다이어리를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "다이어리 생성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "다이어리 생성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping
    public ResponseEntity<?> createDiary(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 CreateDiaryReq를 참고해주세요.", required = true) @Valid @RequestBody CreateDiaryReq createDiaryReq
    ) {
        return diaryService.createDiary(userPrincipal, createDiaryReq);
    }

    @Operation(summary = "유저의 다이어리 목록 조회", description = "유저의 다이어리 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저의 다이어리 목록 조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DiaryListRes.class)))}),
            @ApiResponse(responseCode = "400", description = "유저의 다이어리 목록 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping
    public ResponseEntity<?> findDiariesByUserAccessToken(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal
    ) {
        return diaryService.findDiariesByUserId(userPrincipal);
    }

    @Operation(summary = "이메일로 유저 다이어리 추가", description = "이메일로 유저를 다이어리에 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "이메일로 유저 다이어리에 추가 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "이메일로 유저 다이어리에 추가 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping("/invite")
    public ResponseEntity<?> inviteUser(
            @Parameter(description = "Schemas의 InviteUserReq를 참고해주세요.", required = true) @Valid @RequestBody InviteUserReq inviteUserReq
    ) {
        return diaryService.inviteUser(inviteUserReq);
    }

    @Operation(summary = "다이어리 나가기", description = "다이어리를 나갑니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다이어리 나가기 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "다이어리 나가기 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @DeleteMapping("{id}")
    public ResponseEntity<?> leaveDiary(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "{id}에 다이어리 아이디를 입력해주세요.", required = true) @PathVariable(value = "id") Long diaryId
    ) {
        return diaryService.leaveDiary(userPrincipal, diaryId);
    }

    @Operation(summary = "다이어리 정보 조회", description = "정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다이어리 정보 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = DiaryDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "다이어리 정보 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("{id}")
    public ResponseEntity<?> seeDiaryDetail(
            @Parameter(description = "{id}에 다이어리 아이디를 입력해주세요.", required = true) @PathVariable(value = "id") Long diaryId
    ) {
        return diaryService.seeDiaryDetail(diaryId);
    }

    @Operation(summary = "다이어리 검색어에 해당하는 제목으로 일기 조회", description = "검색어에 해당하는 제목으로 일기를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "검색어로 일기 정보 조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = DiaryRecordDetailRes.class)))}),
            @ApiResponse(responseCode = "400", description = "검색어로 일기 정보 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("{id}/search")
    public ResponseEntity<?> seeDiaryDetail(
            @Parameter(description = "{id}에 다이어리 아이디를 입력해주세요.", required = true) @PathVariable(value = "id") Long diaryId,
            @Parameter(description = "검색어를 입력해주세요.", required = true) @RequestParam String search
    ) {
        return diaryService.searchRecord(diaryId, search);
    }

    @Operation(summary = "다이어리 이름 변경", description = "다이어리 이름을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "다이어리 이름 변경 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "다이어리 이름 변경 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PatchMapping
    public ResponseEntity<?> updateDiary(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "UpdateDiaryReq를 참고해주세요.", required = true) @Valid @RequestBody UpdateDiaryReq updateDiaryReq
    ){
        return diaryService.updateDiary(userPrincipal, updateDiaryReq);
    }

}

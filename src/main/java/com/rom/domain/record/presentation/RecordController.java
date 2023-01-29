package com.rom.domain.record.presentation;

import com.rom.domain.record.application.RecordService;
import com.rom.domain.record.dto.*;
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

@Tag(name="Records", description = "Records API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    /* TO-DO: 사용자 프로필 이미지, 일기 이미지 */

    // 다이어리별 일기 조회
    @Operation(summary = "다이어리별 일기 조회", description = "요청받은 다이어리의 일기를 모두 읽어옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다이어리별 일기 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "다이어리별 일기 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping
    public ResponseEntity<?> getRecordsOfDiary(
            @Parameter(description = "Schemas의 RecordsByDiaryReq를 참고해주세요.", required = true) @Valid @RequestBody RecordsByDiaryReq recordsByDiaryReq
    ){
        return recordService.getRecordsOfDiary(recordsByDiaryReq);
    }

    // 일기 상세 조회
    @Operation(summary = "일기 상세 조회", description = "원하는 일기의 내용을 상세하게 읽어옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 상세 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "일기 상세 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/detail")
    public ResponseEntity<?> getRecordDetail(
            @Parameter(description = "Schemas의 RecordDetailReq를 참고해주세요.", required = true) @Valid @RequestBody RecordDetailReq recordDetailReq
            ){
        return recordService.getRecordDetail(recordDetailReq);
    }


    // 일기 작성
    @Operation(summary = "일기 작성", description = "일기룰 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "일기 작성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "일기 작성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping
    public ResponseEntity<?> writeRecord(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 WriteRecordReq를 참고해주세요.") @Valid @RequestBody WriteRecordReq writeRecordReq
            ){
        return recordService.writeRecord(userPrincipal, writeRecordReq);
    }

    // 일기 삭제
    @Operation(summary = "일기 삭제", description = "일기를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 삭제 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "일기 삭제 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PatchMapping
    public ResponseEntity<?> deleteRecord(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 DeleteRecordReq를 참고해주세요.", required = true) @Valid @RequestBody DeleteRecordReq deleteRecordReq
    ){
        return recordService.deleteRecord(userPrincipal, deleteRecordReq);
    }

    @Operation(summary = "일기 수정", description = "일기를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "일기 수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PatchMapping("/edit")
    public ResponseEntity<?> updateRecord(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 UpdateRecordReq를 참고해주세요.", required = true) @Valid @RequestBody UpdateRecordReq updateRecordReq
    ){
        return recordService.updateRecord(userPrincipal, updateRecordReq);
    }

}

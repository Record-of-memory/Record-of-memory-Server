package com.rom.domain.record.presentation;

import com.rom.domain.record.application.RecordService;
import com.rom.domain.record.dto.*;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Tag(name = "Records", description = "Records API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/records")
public class RecordController {

    private final RecordService recordService;

    // 다이어리별 일기 조회
    @Operation(summary = "다이어리별 일기 조회", description = "요청받은 다이어리의 일기를 모두 읽어옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다이어리별 일기 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecordDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "다이어리별 일기 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/{diaryId}")
    public ResponseEntity<?> getRecordsOfDiary(
            @Parameter(description = "다이어리의 id입니다.", required = true) @Valid @PathVariable("diaryId") Long diaryId) {
        return recordService.getRecordsOfDiary(diaryId);
    }

    // 다이어리 내 유저별 일기 조회
    @Operation(summary = "유저별 일기 조회", description = "다이어리 내 해당 유저의 일기를 모두 읽어옵니다. ex./api/records/user?diaryId=1&userId=2")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다이어리별 일기 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecordDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "다이어리별 일기 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/user")
    public ResponseEntity<?> getRecordsOfDiaryByUser(
            @Parameter(description = "Parameter - 다이어리ID(diaryId)", required = true) @Valid @RequestParam(value = "diaryId", required = true) Long diaryId,
            @Parameter(description = "Parameter - 유저pk(userId)", required = true) @RequestParam(value = "userId", required = true) Long userId) {
        return recordService.getRecordsOfDiaryByUser(diaryId, userId);
    }

    // 다이어리 내 날짜별 일기 조회
    @Operation(summary = "날짜별 일기 조회", description = "다이어리 내 해당 일자의 일기를 모두 읽어옵니다. ex./api/records/date?diaryId=1&date=2023-01-24")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "다이어리별 일기 조회 성공", content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = FindRecordByDateRes.class)))}),
            @ApiResponse(responseCode = "400", description = "다이어리별 일기 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/date")
    public ResponseEntity<?> getRecordsOfDiaryByDate(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Parameter - 날짜(date) ex.2023-02-11", required = true) @RequestParam(value = "date", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date date) {
        return recordService.getRecordsOfDiaryByDate(userPrincipal, date);
    }

    //그리드뷰 전용 일기 조회
    @Operation(summary = "그리드뷰 일기 조회", description = "다이어리 내 이미지가 있는 일기만 읽어옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그리드뷰 일기 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GridResultRes.class))}),
            @ApiResponse(responseCode = "400", description = "그리드뷰 일기 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/grid/{diaryId}")
    public ResponseEntity<?> getGridRecords(
            @Parameter(description = "다이어리의 ID입니다.", required = true) @Valid @PathVariable("diaryId") Long diaryId) {
        return recordService.getGridRecords(diaryId);
    }

    //그리드뷰 전용 일기 조회 (상세 페이지)
    @Operation(summary = "그리드뷰 상세 페이지 일기 조회", description = "다이어리 내 이미지가 있는 일기만 읽어옵니다 (유저 선택)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "그리드뷰 일기 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = GridResultDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "그리드뷰 일기 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/grid/{diaryId}/{userId}")
    public ResponseEntity<?> getGridRecordsDetail(
            @Parameter(description = "다이어리의 ID입니다.", required = true) @Valid @PathVariable("diaryId") Long diaryId,
            @Parameter(description = "조회할 유저의 ID입니다.", required = true) @Valid @PathVariable("userId") Long userId) {
        return recordService.getGridRecordsDetail(diaryId, userId);
    }


    // 일기 상세 조회
    @Operation(summary = "일기 상세 조회", description = "원하는 일기의 내용을 상세하게 읽어옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 상세 조회 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = RecordDetailRes.class))}),
            @ApiResponse(responseCode = "400", description = "일기 상세 조회 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @GetMapping("/detail/{recordId}")
    public ResponseEntity<?> getRecordDetail(
            @Parameter(description = "일기의 id입니다.", required = true) @Valid @PathVariable("recordId") Long recordId) {
        return recordService.getRecordDetail(recordId);
    }

    /*
     * 이미지 포함된 다이어리 작성
     * */
    @Operation(summary = "일기 작성(이미지 포함)", description = "일기를 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "일기 작성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "일기 작성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> writeRecord(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 WriteRecordReq를 참고해주세요.") @Valid @RequestPart("writeRecordReq") WriteRecordReq writeRecordReq,  // @RequestBody -> @RequestPart
            @Parameter(description = "img의 url") @RequestPart(value = "img", required = false) MultipartFile img
    ) throws IOException {
        // img 파라미터 추가
        return recordService.writeRecordWithImg(userPrincipal, writeRecordReq, img);
    }

    /*
     * 이미지 없이 업로드하는 컨트롤러
     * */
//    @Operation(summary = "일기 작성(이미지 없는, content 만 있는)", description = "일기를 작성합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "일기 작성 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
//            @ApiResponse(responseCode = "400", description = "일기 작성 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
//    })
//    @PostMapping("/no-img")
//    public ResponseEntity<?> writeRecord(
//            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
//            @Parameter(description = "Schemas의 WriteRecordReq를 참고해주세요.") @Valid @RequestBody WriteRecordNoImgReq writeRecordReq
//    ){
//        return recordService.writeRecord(userPrincipal, writeRecordReq);
//    }

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
    ) {
        return recordService.deleteRecord(userPrincipal, deleteRecordReq);
    }

    //일기 수정
    @Operation(summary = "일기 수정", description = "일기를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 수정 성공", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Message.class))}),
            @ApiResponse(responseCode = "400", description = "일기 수정 실패", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))})
    })
    @PatchMapping(value = "/edit", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateRecord(
            @Parameter(description = "AccessToken을 입력해주세요", required = true) @CurrentUser UserPrincipal userPrincipal,
            @Parameter(description = "Schemas의 updateRecordReq를 참고해주세요.") @Valid @RequestPart("updateRecordReq") UpdateRecordReq updateRecordReq,
            @Parameter(description = "img의 url") @RequestPart(value = "img", required = false) MultipartFile img
    ) throws IOException {
        return recordService.updateRecord(userPrincipal, updateRecordReq, img);
    }

}

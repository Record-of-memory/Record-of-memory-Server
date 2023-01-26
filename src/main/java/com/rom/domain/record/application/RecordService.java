package com.rom.domain.record.application;

import com.rom.domain.common.Status;
import com.rom.domain.diary.domain.Diary;
import com.rom.domain.diary.domain.repository.DiaryRepository;
import com.rom.domain.record.domain.Record;
import com.rom.domain.record.domain.repository.RecordRepository;
import com.rom.domain.record.dto.DeleteRecordReq;
import com.rom.domain.record.dto.RecordDetailRes;
import com.rom.domain.record.dto.WriteRecordReq;
import com.rom.domain.user.domain.User;
import com.rom.domain.user.domain.repository.UserRepository;
import com.rom.global.DefaultAssert;
import com.rom.global.config.security.token.UserPrincipal;
import com.rom.global.payload.ApiResponse;
import com.rom.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecordService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final RecordRepository recordRepository;

    // 일기 작성
    @Transactional
    public ResponseEntity<?> writeRecord(UserPrincipal userPrincipal, WriteRecordReq writeRecordReq) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "올바른 유저가 아닙니다.");

        Optional<Diary> diary = diaryRepository.findById(writeRecordReq.getDiaryId());
        DefaultAssert.isTrue(diary.isPresent(), "다이어리가 올바르지 않습니다.");

        Record record = Record.builder()
                .diary(diary.get())
                .date(writeRecordReq.getDate())
                .title(writeRecordReq.getTitle())
                .content(writeRecordReq.getContent())
                .user(user.get())
                .build();

        recordRepository.save(record);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("일기가 작성되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 일기 삭제
    @Transactional
    public ResponseEntity<?> deleteRecord(UserPrincipal userPrincipal, DeleteRecordReq deleteRecordReq) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "올바른 유저가 아닙니다.");

        Optional<Record> record = recordRepository.findById(deleteRecordReq.getRecordId());
        DefaultAssert.isTrue(record.isPresent(), "일기가 올바르지 않습니다.");

        record.get().setStatus(Status.DELETE);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("일기가 삭제되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // [개발용] 전체 일기 조회
    @Transactional
    public ResponseEntity<?> getAllRecords(UserPrincipal userPrincipal) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());

        List<Record> records = recordRepository.findAll();

        List<RecordDetailRes> recordDetailRes = records.stream().map(
                record -> RecordDetailRes.builder()
                        .id(record.getId())
                        .user(record.getUser().getNickname())
                        .diary(record.getDiary().getName())
                        .date(record.getDate())
                        .content(record.getContent())
                        .title(record.getTitle())
                        .status(record.getStatus())
                        .build()
        ).toList();

        ApiResponse apiResponse = ApiResponse.builder()
            .check(true)
            .information(recordDetailRes)
            .build();

        return ResponseEntity.ok(apiResponse);
    }



    // 다이어리별 일기 조회
    @Transactional
    public ResponseEntity<?> getRecord() {

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("해당 다이어리의 일기를 모두 읽어옵니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }



}

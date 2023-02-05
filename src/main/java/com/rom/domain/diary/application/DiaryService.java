package com.rom.domain.diary.application;

import com.rom.domain.common.Status;
import com.rom.domain.diary.domain.Diary;
import com.rom.domain.diary.domain.DiaryType;
import com.rom.domain.diary.domain.UserDiary;
import com.rom.domain.diary.domain.repository.DiaryRepository;
import com.rom.domain.diary.domain.repository.UserDiaryRepository;
import com.rom.domain.diary.dto.*;
import com.rom.domain.record.domain.Record;
import com.rom.domain.record.dto.RecordDetailRes;
import com.rom.domain.user.domain.Role;
import com.rom.domain.user.domain.User;
import com.rom.domain.user.domain.repository.UserRepository;
import com.rom.domain.user.dto.UserDetailRes;
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
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final UserRepository userRepository;
    private final UserDiaryRepository userDiaryRepository;

    @Transactional
    public ResponseEntity<?> createDiary(UserPrincipal userPrincipal, CreateDiaryReq createDiaryReq) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Diary diary = Diary.builder()
                .name(createDiaryReq.getName())
                .diaryType(DiaryType.valueOf(createDiaryReq.getDiaryType()))
                .build();

        UserDiary userDiary = UserDiary.builder()
                .user(user.get())
                .diary(diary)
                .build();

        diaryRepository.save(diary);
        userDiaryRepository.save(userDiary);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("다이어리가 생성되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> findDiariesByUserId(UserPrincipal userPrincipal) {
        List<Diary> diaries = userDiaryRepository.findAllByUserId(userPrincipal.getId()).stream()
                .map(UserDiary::getDiary)
                .toList();

        List<DiaryListRes> diaryListRes = diaries.stream()
                .map(diary -> DiaryListRes.builder()
                        .id(diary.getId())
                        .name(diary.getName())
                        .diaryType(diary.getDiaryType().toString())
                        .build())
                .toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(diaryListRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> inviteUser(InviteUserReq inviteUserReq) {
        Optional<User> inviteUser = userRepository.findByEmail(inviteUserReq.getEmail());
        DefaultAssert.isTrue(inviteUser.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Diary> diary = diaryRepository.findById(inviteUserReq.getDiaryId());
        DefaultAssert.isTrue(diary.isPresent(), "다이어리가 올바르지 않습니다");

        if (userDiaryRepository.existsUserDiaryByUserAndDiary(inviteUser.get(), diary.get())) {
            ApiResponse apiResponse = ApiResponse.builder()
                    .check(false)
                    .information(Message.builder().message("이미 존재하는 유저입니다.").build())
                    .build();
            return ResponseEntity.ok(apiResponse);
        }

        UserDiary userDiary = UserDiary.builder()
                .diary(diary.get())
                .user(inviteUser.get())
                .build();

        userDiaryRepository.save(userDiary);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("유저 초대가 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> leaveDiary(UserPrincipal userPrincipal, Long diaryId) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다");

        Optional<Diary> diary = diaryRepository.findById(diaryId);
        DefaultAssert.isTrue(diary.isPresent(), "다이어리가 올바르지 않습니다.");

        Optional<UserDiary> userDiary = userDiaryRepository.findUserDiaryByUserAndDiary(user.get(), diary.get());
        DefaultAssert.isTrue(userDiary.isPresent(), "UserDiary가 올바르지 않습니다");

        userDiaryRepository.delete(userDiary.get());

        if (!userDiaryRepository.existsUserDiaryByDiary(diary.get())) {
            diary.get().updateStatus(Status.DELETE);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("다이어리 나가기가 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> seeDiaryDetail(Long diaryId) {
        Optional<Diary> diary = diaryRepository.findById(diaryId);
        DefaultAssert.isTrue(diary.isPresent(), "다이어리가 올바르지 않습니다.");

        Diary findDiary = diary.get();
        List<User> users = userDiaryRepository.findAllByDiaryId(findDiary.getId()).stream()
                .map(UserDiary::getUser)
                .toList();
        List<Record> records = findDiary.getRecords();

        List<UserDetailRes> usersDetailRes = users.stream()
                .map(user -> UserDetailRes.builder()
                        .email(user.getEmail())
                        .nickname(user.getNickname())
                        .imageUrl(user.getImageUrl())
                        .role(Role.USER)
                        .build())
                .toList();

        List<DiaryRecordDetailRes> recordsDetailRes = records.stream()
                .map(record -> DiaryRecordDetailRes.builder()
                        .id(record.getId())
                        .title(record.getTitle())
                        .content(record.getContent())
                        .date(record.getDate())
                        .user(UserDetailRes.builder()
                                .email(record.getUser().getEmail())
                                .nickname(record.getUser().getNickname())
                                .imageUrl(record.getUser().getImageUrl())
                                .role(record.getUser().getRole())
                                .build())
                        .build())
                .toList();

        DiaryDetailRes diaryDetailRes = DiaryDetailRes.builder()
                .id(findDiary.getId())
                .name(findDiary.getName())
                .diaryType(findDiary.getDiaryType().toString())
                .users(usersDetailRes)
                .records(recordsDetailRes)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(diaryDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

}

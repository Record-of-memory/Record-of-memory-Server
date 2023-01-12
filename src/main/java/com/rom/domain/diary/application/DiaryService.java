package com.rom.domain.diary.application;

import com.rom.domain.diary.domain.Diary;
import com.rom.domain.diary.domain.DiaryType;
import com.rom.domain.diary.domain.UserDiary;
import com.rom.domain.diary.domain.repository.DiaryRepository;
import com.rom.domain.diary.domain.repository.UserDiaryRepository;
import com.rom.domain.diary.dto.CreateDiaryReq;
import com.rom.domain.diary.dto.DiaryDetailRes;
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

        List<DiaryDetailRes> diaryDetailRes = diaries.stream()
                .map(diary -> DiaryDetailRes.builder().id(diary.getId()).name(diary.getName()).diaryType(diary.getDiaryType().toString()).build())
                .toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(diaryDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}

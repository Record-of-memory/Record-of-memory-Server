package com.rom.domain.record.application;

import com.amazonaws.Response;
import com.rom.domain.comment.domain.Comment;
import com.rom.domain.comment.domain.repository.CommentRepository;
import com.rom.domain.common.Status;
import com.rom.domain.diary.domain.Diary;
import com.rom.domain.diary.domain.UserDiary;
import com.rom.domain.diary.domain.repository.DiaryRepository;
import com.rom.domain.diary.domain.repository.UserDiaryRepository;
import com.rom.domain.diary.dto.DiaryDetailRes;
import com.rom.domain.diary.dto.DiaryRecordDetailRes;
import com.rom.domain.likes.domain.Likes;
import com.rom.domain.likes.domain.repository.LikesRepository;
import com.rom.domain.record.domain.Record;
import com.rom.domain.record.domain.repository.RecordRepository;
import com.rom.domain.record.dto.*;
import com.rom.domain.user.domain.User;
import com.rom.domain.user.domain.repository.UserRepository;
import com.rom.domain.user.dto.UserDetailRes;
import com.rom.global.DefaultAssert;
import com.rom.global.config.security.token.UserPrincipal;
import com.rom.global.infrastructure.S3Uploader;
import com.rom.global.payload.ApiResponse;
import com.rom.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class RecordService {

    // 의존성 주입
    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final RecordRepository recordRepository;
    private final UserDiaryRepository userDiaryRepository;
    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final S3Uploader s3Uploader;

    // 일기 작성
    // img 업로드 추가
    @Transactional
    public ResponseEntity<?> writeRecordWithImg(UserPrincipal userPrincipal, WriteRecordReq writeRecordReq, MultipartFile img) throws IOException {

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

        // img가 비어있는지 체크
        // 업로드할 디렉토리 이름 설정 (record의 이미지는 record_img, 프로필의 이미지는 profile_img
        if (img != null) {
            String storedFileName = s3Uploader.upload(img, "record_img");
            record.setImgUrl(storedFileName);
        }

        recordRepository.save(record);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("일기가 작성되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 일기 작성
    @Transactional
    public ResponseEntity<?> writeRecord(UserPrincipal userPrincipal, WriteRecordNoImgReq writeRecordReq) {

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

    // 다이어리별 일기 조회
    public ResponseEntity<?> getRecordsOfDiary(Long diaryId) {

        Optional<Diary> diary = diaryRepository.findById(diaryId);
        List<Record> records = recordRepository.findAllByDiary(diary.get());

        List<RecordDetailRes> recordDetailRes = records.stream().map(
                record -> RecordDetailRes.builder()
                        .id(record.getId())
                        .user(record.getUser().getNickname())
                        .diary(record.getDiary().getName())
                        .date(record.getDate())
                        .content(record.getContent())
                        .title(record.getTitle())
                        .status(record.getStatus())
                        .imgUrl(record.getImgUrl())
                        .likeCnt(likeCount(record.getId()))
                        .cmtCnt(commentCount(record.getId()))
                        .build()
        ).toList();


        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(recordDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 다이어리별 일기 조회(유저별)
    public ResponseEntity<?> getRecordsOfDiaryByUser(Long diaryId, Long userId) {

        Optional<Diary> diary = diaryRepository.findById(diaryId);
        DefaultAssert.isTrue(diary.isPresent(), "올바르지 않은 다이어리입니다.");

        Optional<User> user = userRepository.findById(userId);
        DefaultAssert.isTrue(user.isPresent(), "올바르지 않은 유저입니다.");

        List<Record> records = recordRepository.findAllByDiaryAndUser(diary.get(), user);

        List<RecordDetailRes> recordDetailRes = records.stream().map(
                record -> RecordDetailRes.builder()
                        .id(record.getId())
                        .user(record.getUser().getNickname())
                        .diary(record.getDiary().getName())
                        .date(record.getDate())
                        .content(record.getContent())
                        .title(record.getTitle())
                        .status(record.getStatus())
                        .imgUrl(record.getImgUrl())
                        .likeCnt(likeCount(record.getId()))
                        .cmtCnt(commentCount(record.getId()))
                        .build()
        ).toList();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(recordDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 다이어리별 일기 조회(날짜별)
    public ResponseEntity<?> getRecordsOfDiaryByDate(UserPrincipal userPrincipal, Date date) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        List<Record> records = recordRepository.findAllByDateAndUser(date, user.get());
        DefaultAssert.isTrue(!records.isEmpty(), "날짜에 해당하는 일기가 존재하지 않습니다.");

        List<FindRecordByDateRes> diaryRecordDetailRes = new ArrayList<>();
        for (Record record : records) {
            Optional<FindRecordByDateRes> findRecordByDateRes = diaryRecordDetailRes.stream()
                    .filter(filter -> Objects.equals(filter.getDiaryId(), record.getDiary().getId()))
                    .findAny();
            if (findRecordByDateRes.isPresent()) {
                findRecordByDateRes.get().getRecords().add(DiaryRecordDetailRes.builder()
                        .id(record.getId())
                        .title(record.getTitle())
                        .content(record.getContent())
                        .imgUrl(record.getImgUrl())
                        .date(record.getDate())
                        .user(UserDetailRes.builder()
                                .email(record.getUser().getEmail())
                                .nickname(record.getUser().getNickname())
                                .imageUrl(record.getUser().getImageUrl())
                                .role(record.getUser().getRole())
                                .build())
                        .likeCount(likesRepository.findAllByRecordId(record.getId()).size())
                        .commentCount(commentRepository.findAllByRecordId(record.getId()).size())
                        .build());
                continue;
            }
            List<DiaryRecordDetailRes> detailRes = new ArrayList<>();
            DiaryRecordDetailRes drdr = DiaryRecordDetailRes.builder()
                    .id(record.getId())
                    .title(record.getTitle())
                    .content(record.getContent())
                    .imgUrl(record.getImgUrl())
                    .date(record.getDate())
                    .user(UserDetailRes.builder()
                            .email(record.getUser().getEmail())
                            .nickname(record.getUser().getNickname())
                            .imageUrl(record.getUser().getImageUrl())
                            .role(record.getUser().getRole())
                            .build())
                    .likeCount(likesRepository.findAllByRecordId(record.getId()).size())
                    .commentCount(commentRepository.findAllByRecordId(record.getId()).size())
                    .build();
            detailRes.add(drdr);
            diaryRecordDetailRes.add(FindRecordByDateRes.builder()
                    .diaryId(record.getDiary().getId())
                    .diaryName(record.getDiary().getName())
                    .records(detailRes)
                    .build());
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(diaryRecordDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    // 개별 일기 상세 조회
    public ResponseEntity<?> getRecordDetail(Long recordId) {

        Optional<Record> record = recordRepository.findById(recordId);

        RecordDetailRes recordDetailRes = RecordDetailRes.builder()
                .id(record.get().getId())
                .user(record.get().getUser().getNickname())
                .diary(record.get().getDiary().getName())
                .date(record.get().getDate())
                .content(record.get().getContent())
                .title(record.get().getTitle())
                .status(record.get().getStatus())
                .imgUrl(record.get().getImgUrl())
                .likeCnt(likeCount(record.get().getId()))
                .cmtCnt(commentCount(record.get().getId()))
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(recordDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    // 다이어리 수정
    @Transactional
    public ResponseEntity<?> updateRecord(UserPrincipal userPrincipal, UpdateRecordReq updateRecordReq, MultipartFile img) throws IOException {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "올바른 유저가 아닙니다.");

        Optional<Record> record = recordRepository.findById(updateRecordReq.getRecordId());
        DefaultAssert.isTrue(record.isPresent(), "일기가 올바르지 않습니다.");

        Record findRecord = record.get();


        if (updateRecordReq.getDate() != null) {
            findRecord.updateDate(updateRecordReq.getDate());
        }
        if (updateRecordReq.getTitle() != null) {
            findRecord.updateTitle(updateRecordReq.getTitle());
        }
        if (updateRecordReq.getContent() != null) {
            findRecord.updateContent(updateRecordReq.getContent());
        }

        if (img != null) {
            String storedFileName = s3Uploader.upload(img, "record_img");
            findRecord.updateImg(storedFileName);
        } else {
            findRecord.updateImg(null);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("일기가 수정되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //그리드뷰 전용 유저별 일기 조회
    public ResponseEntity<?> getGridRecords(Long diaryId) {

        Optional<Diary> diary = diaryRepository.findById(diaryId);
        DefaultAssert.isTrue(diary.isPresent(), "올바른 일기가 아닙니다.");

        List<User> findUser = userDiaryRepository.findAllByDiaryId(diary.get().getId()).stream()
                .map(UserDiary::getUser)
                .toList();

        List<GridUserRes> gridUserResList = new ArrayList<>();

        for (User value : findUser) {
            List<Record> findRecords = recordRepository.findAllByDiaryAndUserAndImgUrlNotNull(diary.get(), value);

            List<GridRecordRes> gridRecordRes = findRecords.stream()
                    .map(record -> GridRecordRes.builder()
                            .id(record.getId())
                            .title(record.getTitle())
                            .imgUrl(record.getImgUrl())
                            .build())
                    .toList();

            GridUserRes gridUserRes = GridUserRes.builder()
                    .id(value.getId())
                    .nickname(value.getNickname())
                    .imgUrl(value.getImageUrl())
                    .records(gridRecordRes)
                    .build();

            if (gridRecordRes.size() != 0) {
                gridUserResList.add(gridUserRes);
            }
        }

        GridResultRes gridResultRes = GridResultRes.builder()
                .id(diary.get().getId())
                .name(diary.get().getName())
                .users(gridUserResList)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(gridResultRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //그리드뷰 전용 유저별 일기 조회 (상세페이지)
    public ResponseEntity<?> getGridRecordsDetail(Long diaryId, Long userId) {

        Optional<Diary> diary = diaryRepository.findById(diaryId);
        DefaultAssert.isTrue(diary.isPresent(), "올바른 일기가 아닙니다.");

        Optional<User> findUser = userRepository.findById(userId);
        DefaultAssert.isTrue(findUser.isPresent(), "올바른 유저가 아닙니다.");

        List<Record> findRecords = recordRepository.findAllByDiaryAndUserAndImgUrlNotNull(diary.get(), findUser.get());

        List<GridRecordRes> gridRecordRes = findRecords.stream()
                .map(record -> GridRecordRes.builder()
                        .id(record.getId())
                        .title(record.getTitle())
                        .imgUrl(record.getImgUrl())
                        .build())
                .toList();

        GridUserRes gridUserRes = GridUserRes.builder()
                .id(findUser.get().getId())
                .nickname(findUser.get().getNickname())
                .imgUrl(findUser.get().getImageUrl())
                .records(gridRecordRes)
                .build();

        GridResultDetailRes gridResultDetailRes = GridResultDetailRes.builder()
                .id(diary.get().getId())
                .name(diary.get().getName())
                .user(gridUserRes)
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(gridResultDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public int likeCount(Long id) {
        List<Likes> likes = likesRepository.findAllByRecordId(id);
        return likes.size();
    }

    public int commentCount(Long id) {
        List<Comment> comments = commentRepository.findAllByRecordId(id);
        return comments.size();
    }

}

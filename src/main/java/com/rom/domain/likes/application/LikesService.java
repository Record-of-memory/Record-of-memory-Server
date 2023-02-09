package com.rom.domain.likes.application;

import com.rom.domain.likes.domain.Likes;
import com.rom.domain.likes.domain.repository.LikesRepository;
import com.rom.domain.likes.dto.LikeClickedRes;
import com.rom.domain.likes.dto.LikeReq;
import com.rom.domain.likes.dto.LikeRes;
import com.rom.domain.record.domain.Record;
import com.rom.domain.record.domain.repository.RecordRepository;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final RecordRepository recordRepository;

    //좋아요
    @Transactional
    public ResponseEntity<?> pressLike(UserPrincipal userPrincipal, LikeReq likeReq) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Record> record = recordRepository.findById(likeReq.getRecordId());
        DefaultAssert.isTrue(record.isPresent(), "일기가 올바르지 않습니다.");

        Likes likes = Likes.builder()
                .user(user.get())
                .record(record.get())
                .build();

        likesRepository.save(likes);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("좋아요").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //좋아요 취소
    @Transactional
    public ResponseEntity<?> cancelLike(UserPrincipal userPrincipal, Long likeId) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Likes> like = likesRepository.findById(likeId);
        DefaultAssert.isTrue(like.isPresent(), "좋아요가 올바르지 않습니다.");

        likesRepository.delete(like.get());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("좋아요 취소").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //좋아요 개수 조회
    public ResponseEntity<?> findLikesCount(UserPrincipal userPrincipal, Long recordId) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Record> record = recordRepository.findById(recordId);
        DefaultAssert.isTrue(record.isPresent(), "일기가 올바르지 않습니다.");

        List<Likes> likes = likesRepository.findAllByRecordId(recordId);

        LikeRes likeRes = LikeRes.builder()
                .count(likes.size())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(likeRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> isLikeClicked(UserPrincipal userPrincipal, Long recordId) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다");

        Optional<Record> record = recordRepository.findById(recordId);
        DefaultAssert.isTrue(record.isPresent(), "일기가 올바르지 않습니다");

        boolean isClicked = likesRepository.existsLikesByUserAndRecord(user.get(), record.get());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(LikeClickedRes.builder()
                        .isLikeClicked(isClicked)
                        .build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}

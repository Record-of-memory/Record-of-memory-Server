package com.rom.domain.comment.application;

import com.rom.domain.comment.domain.Comment;
import com.rom.domain.comment.domain.repository.CommentRepository;
import com.rom.domain.comment.dto.DeleteCommentReq;
import com.rom.domain.comment.dto.FindCommentReq;
import com.rom.domain.comment.dto.FindCommentRes;
import com.rom.domain.comment.dto.WriteCommentReq;
import com.rom.domain.common.Status;
import com.rom.domain.record.domain.Record;
import com.rom.domain.record.domain.repository.RecordRepository;
import com.rom.domain.user.domain.User;
import com.rom.domain.user.domain.repository.UserRepository;
import com.rom.global.DefaultAssert;
import com.rom.global.config.security.token.UserPrincipal;
import com.rom.global.payload.ApiResponse;
import com.rom.global.payload.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final RecordRepository recordRepository;

    //댓글 작성
    @Transactional
    public ResponseEntity<?> writeComment(UserPrincipal userPrincipal, WriteCommentReq writeCommentReq) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Record> record = recordRepository.findById(writeCommentReq.getRecordId());
        DefaultAssert.isTrue(record.isPresent(), "일기가 올바르지 않습니다.");

        Comment comment = Comment.builder()
                .content(writeCommentReq.getContent())
                .user(user.get())
                .record(record.get())
                .build();

        commentRepository.save(comment);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("댓글이 작성되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    //댓글 삭제
    @Transactional
    public ResponseEntity<?> deleteComment(UserPrincipal userPrincipal, DeleteCommentReq deleteCommentReq) {

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<Comment> comment = commentRepository.findById(deleteCommentReq.getCommentId());
        DefaultAssert.isTrue(comment.isPresent(), "댓글이 올바르지 않습니다");

        comment.get().updateStatus(Status.DELETE);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("댓글이 삭제되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }


    //댓글 전체 조회
    public ResponseEntity<?> findAllComments(UserPrincipal userPrincipal, FindCommentReq findCommentReq){

        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        List<Comment> comments = commentRepository.findAllByRecordIdOrderByCreatedAtDesc(findCommentReq.getRecordId());
        List<FindCommentRes> findCommentRes = comments.stream()
                .map(comment -> FindCommentRes.builder()
                        .nickname(user.get().getNickname())
                        .imageUrl(user.get().getImageUrl())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        //댓글 조회할 때 댓글의 개수도 같이 넘겨주기 위해 클래스로 한 번 감싸서 데이터를 넣어줌
        Result result = new Result(findCommentRes.size(), findCommentRes);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(result)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Data
    @AllArgsConstructor
    static class Result {
        private int count;    //댓글 개수
        private List<FindCommentRes> data;
    }


}

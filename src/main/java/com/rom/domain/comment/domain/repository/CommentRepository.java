package com.rom.domain.comment.domain.repository;

import com.rom.domain.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByRecordId(Long id);

    //작성 시간 기준 내림차순 조회
    List<Comment> findAllByRecordIdOrderByCreatedAtDesc(Long id);


}

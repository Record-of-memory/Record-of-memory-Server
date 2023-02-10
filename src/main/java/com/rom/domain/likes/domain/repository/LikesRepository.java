package com.rom.domain.likes.domain.repository;

import com.rom.domain.likes.domain.Likes;
import com.rom.domain.record.domain.Record;
import com.rom.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    List<Likes> findAllByRecordId(Long id);

    Optional<Likes> findByUserAndRecord(User user, Record record);

}

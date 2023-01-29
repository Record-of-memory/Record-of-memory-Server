package com.rom.domain.likes.domain.repository;

import com.rom.domain.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    List<Likes> findAllByRecordId(Long id);

}

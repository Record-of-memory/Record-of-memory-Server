package com.rom.domain.diary.domain.repository;

import com.rom.domain.diary.domain.UserDiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDiaryRepository extends JpaRepository<UserDiary, Long> {

    List<UserDiary> findAllByUserId(Long id);

    List<UserDiary> findALlByDiaryId(Long id);
}

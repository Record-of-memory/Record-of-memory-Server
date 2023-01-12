package com.rom.domain.diary.domain.repository;

import com.rom.domain.diary.domain.UserDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDiaryRepository extends JpaRepository<UserDiary, Long> {
}

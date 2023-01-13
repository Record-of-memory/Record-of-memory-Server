package com.rom.domain.diary.domain.repository;

import com.rom.domain.diary.domain.Diary;
import com.rom.domain.diary.domain.UserDiary;
import com.rom.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserDiaryRepository extends JpaRepository<UserDiary, Long> {

    Optional<UserDiary> findUserDiaryByUserAndDiary(User user, Diary diary);

    List<UserDiary> findAllByUserId(Long id);

    List<UserDiary> findALlByDiaryId(Long id);

    boolean existsUserDiaryByUserAndDiary(User user, Diary diary);

}

package com.rom.domain.record.domain.repository;

import com.rom.domain.diary.domain.Diary;
import com.rom.domain.record.domain.Record;
import com.rom.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByDiary(Diary diary);
    List<Record> findAllByDiaryAndDate(Diary diary, Date date);
    List<Record> findAllByDiaryAndUser(Diary diary, Optional<User> user);

}

package com.rom.domain.record.domain.repository;

import com.rom.domain.diary.domain.Diary;
import com.rom.domain.record.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
    List<Record> findAllByDiary(Diary diary);
}

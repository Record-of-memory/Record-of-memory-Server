package com.rom.domain.record.domain.repository;

import com.rom.domain.record.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}

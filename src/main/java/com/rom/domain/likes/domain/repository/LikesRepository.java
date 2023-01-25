package com.rom.domain.likes.domain.repository;

import com.rom.domain.user.domain.User;
import com.rom.domain.record.domain.Record;
import com.rom.domain.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    void deleteLikesByPost(Record record);

    @Modifying
    @Query(value = "INSERT INTO likes(post_id, user_id) VALUES(:recordId, :userId)", nativeQuery = true)
    void likes(long recordId, long userId);

    @Modifying
    @Query(value = "DELETE FROM likes WHERE record_id = :recordId AND user_id = :userId", nativeQuery = true)
    void unlikes(long recordId, long userId);
}

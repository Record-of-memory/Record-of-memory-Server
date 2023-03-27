package com.rom.domain.friend.domain.repository;

import com.rom.domain.friend.domain.FriendShip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendShipRepository extends JpaRepository<FriendShip, Long> {
}

package com.rom.domain.user.domain.repository;

import java.util.Optional;

import com.rom.domain.user.domain.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long>{
    
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    Optional<User> findByEmailAndNickname(String email, String nickname);
}

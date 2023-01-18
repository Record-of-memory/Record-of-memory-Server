package com.rom.domain.user.application;

import java.util.Optional;

import com.rom.domain.auth.domain.repository.TokenRepository;
import com.rom.domain.user.dto.UserDetailRes;
import com.rom.global.DefaultAssert;
import com.rom.domain.user.domain.User;
import com.rom.global.payload.ApiResponse;
import com.rom.domain.user.domain.repository.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;


    public ResponseEntity<?> findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다");

        User findUser = user.get();
        UserDetailRes userDetailRes = UserDetailRes.builder()
                .email(findUser.getEmail())
                .nickname(findUser.getNickname())
                .imageUrl(findUser.getImageUrl())
                .role(findUser.getRole())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(userDetailRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }


}

package com.rom.domain.user.application;

import java.io.IOException;
import java.util.Optional;

import com.rom.domain.auth.domain.Token;
import com.rom.domain.auth.domain.repository.TokenRepository;
import com.rom.domain.common.Status;
import com.rom.domain.user.dto.ChangePasswordReq;
import com.rom.domain.user.dto.UserDetailRes;
import com.rom.global.DefaultAssert;
import com.rom.domain.user.domain.User;
import com.rom.global.config.security.token.UserPrincipal;
import com.rom.global.infrastructure.S3Uploader;
import com.rom.global.payload.ApiResponse;
import com.rom.domain.user.domain.repository.UserRepository;

import com.rom.global.payload.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

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

    public ResponseEntity<?> findUserByAccessToken(UserPrincipal userPrincipal) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());
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

    @Transactional
    public ResponseEntity<?> changePassword(UserPrincipal userPrincipal, ChangePasswordReq changePasswordReq) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        User findUser = user.get();
        boolean passwordCheck = passwordEncoder.matches(changePasswordReq.getOldPassword(),findUser.getPassword());
        DefaultAssert.isTrue(passwordCheck, "비밀번호가 일치하지 않습니다.");

        findUser.updatePassword(passwordEncoder.encode(changePasswordReq.getNewPassword()));

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("비밀번호 변경이 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> updateProfile(UserPrincipal userPrincipal, String nickname, MultipartFile profileImg) throws IOException {
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        User findUser = user.get();

        findUser.updateName(nickname);

        if (profileImg != null) {
            String storedFileName = s3Uploader.upload(profileImg, "profile_img");
            findUser.updateImageUrl(storedFileName);
        }

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("프로필이 업데이트 되었습니다").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> deleteUser(UserPrincipal userPrincipal) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다");

        User findUser = user.get();
        findUser.updateStatus(Status.DELETE);

        Optional<Token> token = tokenRepository.findByUserEmail(findUser.getEmail());
        token.ifPresent(tokenRepository::delete);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("유저 탈퇴가 완료되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    public ResponseEntity<?> findUsers(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        DefaultAssert.isTrue(user.isPresent(), "유저가 존재하지 않습니다");

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

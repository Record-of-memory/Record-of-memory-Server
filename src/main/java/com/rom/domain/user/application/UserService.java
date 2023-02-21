package com.rom.domain.user.application;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.rom.domain.auth.domain.Token;
import com.rom.domain.auth.domain.repository.TokenRepository;
import com.rom.domain.common.Status;
import com.rom.domain.user.dto.ChangePasswordReq;
import com.rom.domain.user.dto.SendPasswordReq;
import com.rom.domain.user.dto.UserDetailRes;
import com.rom.global.DefaultAssert;
import com.rom.domain.user.domain.User;
import com.rom.global.config.security.token.UserPrincipal;
import com.rom.global.infrastructure.S3Uploader;
import com.rom.global.payload.ApiResponse;
import com.rom.domain.user.domain.repository.UserRepository;

import com.rom.global.payload.Message;
import lombok.Builder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
    private final JavaMailSender mailSender;


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

        findUser.updateNickName(nickname);

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

    //이메일을 통한 임시 비밀번호 발급
    @Transactional
    public ResponseEntity<?> sendTemporaryPassword(SendPasswordReq sendPasswordReq) {

        Optional<User> findUser = userRepository.findByEmailAndNickname(sendPasswordReq.getEmail(), sendPasswordReq.getNickname());
        DefaultAssert.isTrue(findUser.isPresent(), "이메일 주소를 다시 한 번 확인해주세요.");

        String temporaryPassword = RandomStringUtils.randomAlphanumeric(8);

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(sendPasswordReq.getEmail());
        message.setFrom("woori.memory@gmail.com");
        message.setSubject("[우리기억] 임시 비밀번호 안내입니다.");
        message.setText(String.format("안녕하세요.\n요청하신 임시 비밀번호가 발급되었습니다.\n임시 비밀번호로 로그인 후, 마이페이지에서 비밀번호를 변경해주세요!\n\n%s\n\n감사합니다.",temporaryPassword));

        mailSender.send(message);

        findUser.get().updatePassword(passwordEncoder.encode(temporaryPassword));

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("임시 비밀번호가 발송되었습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}

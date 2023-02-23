package com.rom.domain.auth.application;

import java.net.URI;
import java.util.Optional;

import com.rom.domain.auth.dto.*;
import com.rom.domain.auth.dto.SendPasswordReq;
import com.rom.global.DefaultAssert;

import com.rom.domain.user.domain.Role;
import com.rom.domain.auth.domain.Token;
import com.rom.domain.user.domain.User;
import com.rom.global.payload.ApiResponse;
import com.rom.global.payload.Message;
import com.rom.domain.auth.domain.repository.TokenRepository;
import com.rom.domain.user.domain.repository.UserRepository;

import io.jsonwebtoken.ExpiredJwtException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.mail.javamail.JavaMailSender;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomTokenProviderService customTokenProviderService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JavaMailSender mailSender;

    @Transactional
    public ResponseEntity<?> signIn(SignInReq signInRequest) {
        Optional<User> user = userRepository.findByEmail(signInRequest.getEmail());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다");

        User findUser = user.get();
        boolean passwordCheck = passwordEncoder.matches(signInRequest.getPassword(), findUser.getPassword());
        DefaultAssert.isTrue(passwordCheck, "비밀번호가 일치하지 않습니다");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signInRequest.getEmail(),
                        signInRequest.getPassword()
                )
        );

        TokenMapping tokenMapping = customTokenProviderService.createToken(authentication);
        Token token = Token.builder()
                .refreshToken(tokenMapping.getRefreshToken())
                .userEmail(tokenMapping.getUserEmail())
                .build();

        tokenRepository.save(token);
        AuthRes authResponse = AuthRes.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(token.getRefreshToken()).build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(authResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> signUp(SignUpReq signUpRequest) {
        DefaultAssert.isTrue(!userRepository.existsByEmail(signUpRequest.getEmail()), "해당 이메일이 존재합니다.");

        User user = User.builder()
                .nickname(signUpRequest.getNickname())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/")
                .buildAndExpand(user.getId()).toUri();
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("회원가입에 성공했습니다.").build())
                .build();

        return ResponseEntity.created(location).body(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> refresh(RefreshTokenReq tokenRefreshRequest) {

        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isTrue(token.isPresent(), "다시 로그인 해주세요.");
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());

        TokenMapping tokenMapping;

        try {
            Long expirationTime = customTokenProviderService.getExpiration(tokenRefreshRequest.getRefreshToken());
            tokenMapping = customTokenProviderService.refreshToken(authentication, token.get().getRefreshToken());
        } catch (ExpiredJwtException ex) {
            tokenMapping = customTokenProviderService.createToken(authentication);
            token.get().updateRefreshToken(tokenMapping.getRefreshToken());
        }

        Token updateToken = token.get().updateRefreshToken(tokenMapping.getRefreshToken());

        AuthRes authResponse = AuthRes.builder().accessToken(tokenMapping.getAccessToken()).refreshToken(updateToken.getRefreshToken()).build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(authResponse)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @Transactional
    public ResponseEntity<?> signOut(RefreshTokenReq tokenRefreshRequest) {

        Optional<Token> token = tokenRepository.findByRefreshToken(tokenRefreshRequest.getRefreshToken());
        DefaultAssert.isTrue(token.isPresent(), "이미 로그아웃 되었습니다.");

        tokenRepository.delete(token.get());
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true).information(Message.builder().message("로그아웃 하였습니다.").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    private boolean valid(String refreshToken) {

        //1. 토큰 형식 물리적 검증
        boolean validateCheck = customTokenProviderService.validateToken(refreshToken);
        DefaultAssert.isTrue(validateCheck, "Token 검증에 실패하였습니다.");

        //2. refresh token 값을 불러온다.
        Optional<Token> token = tokenRepository.findByRefreshToken(refreshToken);
        DefaultAssert.isTrue(token.isPresent(), "로그아웃 처리된 회원입니다.");

        //3. email 값을 통해 인증값을 불러온다
        Authentication authentication = customTokenProviderService.getAuthenticationByEmail(token.get().getUserEmail());
        DefaultAssert.isTrue(token.get().getUserEmail().equals(authentication.getName()), "사용자 인증에 실패하였습니다.");

        return true;
    }

    //이메일 중복 검사 (회원가입 시)
    public ResponseEntity<?> checkEmail(String email){

        Boolean isEmail = userRepository.existsByEmail(email);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(CheckEmailRes.builder()
                        .isEmail(isEmail)
                        .build())
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

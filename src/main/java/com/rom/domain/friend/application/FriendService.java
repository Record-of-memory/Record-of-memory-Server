package com.rom.domain.friend.application;

import com.rom.domain.friend.domain.FriendRequest;
import com.rom.domain.friend.domain.repository.FriendRequestRepository;
import com.rom.domain.friend.domain.repository.FriendShipRepository;
import com.rom.domain.friend.dto.RequestFriendReq;
import com.rom.domain.user.domain.User;
import com.rom.domain.user.domain.repository.UserRepository;
import com.rom.global.DefaultAssert;
import com.rom.global.config.security.token.UserPrincipal;
import com.rom.global.payload.ApiResponse;
import com.rom.global.payload.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendShipRepository friendShipRepository;

    public ResponseEntity<?> requestFriendShip(RequestFriendReq requestFriendReq, UserPrincipal userPrincipal) {
        Optional<User> user = userRepository.findById(userPrincipal.getId());
        DefaultAssert.isTrue(user.isPresent(), "유저가 올바르지 않습니다.");

        Optional<User> reqFriendUser = userRepository.findByEmail(requestFriendReq.getEmail());
        DefaultAssert.isTrue(reqFriendUser.isPresent(), "이메일에 해당하는 유저가 존재하지 않습니다.");

        FriendRequest friendRequest = FriendRequest.builder()
                .fromUser(user.get())
                .toUser(reqFriendUser.get())
                .build();

        friendRequestRepository.save(friendRequest);

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(Message.builder().message("친구 요청이 완료되었습니다").build())
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}

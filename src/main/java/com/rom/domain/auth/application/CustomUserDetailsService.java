package com.rom.domain.auth.application;

import java.util.Optional;

import com.rom.global.DefaultAssert;
import com.rom.global.config.security.token.UserPrincipal;
import com.rom.domain.user.domain.User;
import com.rom.domain.user.domain.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CustomUserDetailsService implements UserDetailsService{

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("유저 정보를 찾을 수 없습니다.")
        );

        return UserPrincipal.create(user);
    }

    public UserDetails loadUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        DefaultAssert.isOptionalPresent(user);

        return UserPrincipal.create(user.get());
    }
    
}

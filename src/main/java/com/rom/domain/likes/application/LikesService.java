package com.rom.domain.likes.application;

import com.rom.domain.likes.domain.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class LikesService {
    private final LikesRepository likesRepository;

    @Transactional
    public void likes(long recordId, long sessionId) {
        likesRepository.likes(recordId, sessionId);
    }

    @Transactional
    public void unLikes(long recordId, long sessionId) {
        likesRepository.unlikes(recordId, sessionId);
    }
}

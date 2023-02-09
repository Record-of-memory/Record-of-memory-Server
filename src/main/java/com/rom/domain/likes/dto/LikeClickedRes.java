package com.rom.domain.likes.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class LikeClickedRes {

    boolean isLikeClicked;

    @Builder
    public LikeClickedRes(boolean isLikeClicked) {
        this.isLikeClicked = isLikeClicked;
    }

}

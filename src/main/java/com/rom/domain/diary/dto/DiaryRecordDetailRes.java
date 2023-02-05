package com.rom.domain.diary.dto;

import com.rom.domain.user.domain.User;
import com.rom.domain.user.dto.UserDetailRes;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class DiaryRecordDetailRes {

    private Long id;

    private String title;

    private String content;

    private String imgUrl;

    private Date date;

    private UserDetailRes user;

    private Integer likeCount;

    private Integer commentCount;

    @Builder
    public DiaryRecordDetailRes(Long id, String title, String content, String imgUrl, Date date, UserDetailRes user, Integer likeCount, Integer commentCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.date = date;
        this.user = user;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }

}

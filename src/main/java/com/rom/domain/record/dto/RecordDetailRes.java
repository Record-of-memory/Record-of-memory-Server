package com.rom.domain.record.dto;

import com.rom.domain.common.Status;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class RecordDetailRes {

    private Long id;

    private Date date;

    private String title;

    private String content;

    private String user; // 사용자 닉네임 반환

    private Status status;

    private String diary; // 다이어리 이름 반환

    private int likeCnt; // 좋아요 수
    private int cmtCnt; // 댓글수

    @Builder
    public RecordDetailRes(Long id, String user, String diary, Date date ,String title, String content, Status status, int likeCnt, int cmtCnt) {
        this.id = id;
        this.user = user;
        this.diary = diary;
        this.date = date;
        this.title = title;
        this.content = content;
        this.status = status;
        this.likeCnt = likeCnt;
        this.cmtCnt = cmtCnt;
    }

}

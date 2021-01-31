package com.spring.kkaemiGG.entity;

import org.joda.time.DateTime;

public class Board {
    /*
    * 게시판이 갖춰야할 것들
    * 제목
    * 내용
    * 작성시간
    * 글쓴이
    * 조회수
    * 댓글수
    * 추천수 = 좋아요 - 싫어
    * */

    private int id;
    private String title;
    private String content;
    private DateTime writeTime;
    private String writer;
    private int views;
    private int comments;
    private int recommendedNumber;
    private int like;
    private int dislike;
}

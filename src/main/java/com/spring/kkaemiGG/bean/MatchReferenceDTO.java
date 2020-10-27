package com.spring.kkaemiGG.bean;

import lombok.Data;

@Data
public class MatchReferenceDTO {

    private long gameId;
    private String role;
    private int season;
    private String platformId;
    private int champion;
    private int queue;
    private String lane;
    private long timestamp;

}

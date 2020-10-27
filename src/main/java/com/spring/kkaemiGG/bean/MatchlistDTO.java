package com.spring.kkaemiGG.bean;

import lombok.Data;

import java.util.List;

@Data
public class MatchlistDTO {
    private int startIndex;
    private int totalGames;
    private int endIndex;
    private List<MatchReferenceDTO> matches;
}
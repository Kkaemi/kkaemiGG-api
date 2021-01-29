package com.spring.kkaemiGG.service;

import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;

public class YoutubeServiceTest {

    @Test
    public void testYoutubeService() {

        List<SearchResult> searchResultList = new YoutubeService().getSearchList();

        prettyPrint(searchResultList.iterator());

    }

    private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults) {

        System.out.println("\n=============================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            System.out.println(" Video Id : " + rId.getVideoId());

            // Double checks the kind is video.
//            if (rId.getKind().equals("youtube#video")) {
//                Thumbnail defaultThumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
//
//                System.out.println(" Video Id : " + rId.getVideoId());
//                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
//                System.out.println(" Channel Title: " + singleVideo.getSnippet().getChannelTitle());
//                System.out.println(" Thumbnail: " + defaultThumbnail.getUrl());
//                System.out.println("\n-------------------------------------------------------------\n");
//            }
        }
        System.out.println("\n-------------------------------------------------------------\n");
    }
}

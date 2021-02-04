package com.spring.kkaemiGG.service.youtube;
/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.spring.kkaemiGG.Development;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Prints a list of videos based on a search term.
 *
 * @author Jeremy Walker
 */
@Service
public class YoutubeService {

    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = new GsonFactory();

    /** Global instance of the max number of videos we want returned (50 = upper limit per page). */
    private static final long NUMBER_OF_VIDEOS_RETURNED = 12;

    public List<SearchResult> getSearchList() {

        List<SearchResult> searchResultList = new ArrayList<>();

        try {
            YouTube youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, request -> {})
                    .setApplicationName("youtube-cmdline-search-sample").build();

            // 검색 할 검색어
            String queryTerm = "롤";

            YouTube.Search.List search = youtube.search().list(Collections.singletonList("id"));

            SearchListResponse searchResponse = search.setKey(Development.YOUTUBE.getApiKey())
                    .setQ(queryTerm)
                    .setType(Collections.singletonList("video"))
                    .setOrder("relevance")
                    .setMaxResults(NUMBER_OF_VIDEOS_RETURNED)
                    .setFields("items(id/videoId)")
                    .execute();

            return searchResponse.getItems();

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return searchResultList;
    }
}
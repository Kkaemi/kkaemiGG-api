package com.spring.kkaemiGG.service;
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
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * Prints a list of videos based on a search term.
 *
 * @author Jeremy Walker
 */
@Service
public class YoutubeService {

    /** Global instance properties filename. */
    private static String PROPERTIES_FILENAME = "youtube.properties";

    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = new GsonFactory();

    /** Global instance of the max number of videos we want returned (50 = upper limit per page). */
    private static final long NUMBER_OF_VIDEOS_RETURNED = 12;

    /** Global instance of Youtube object to make all API requests. */
    private static YouTube youtube;


    /**
     * Initializes YouTube object to search for videos on YouTube (Youtube.Search.List). The program
     * then prints the names and thumbnails of each of the videos (only first 50 videos).
     *
     * @param args command line args.
     */
    public List<SearchResult> getSearchList() {
        // Read the developer key from youtube.properties
        Properties properties = new Properties();
        try {
            InputStream in = YoutubeService.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
            properties.load(in);

        } catch (IOException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }

        try {
            /*
             * The YouTube object is used to make all API requests. The last argument is required, but
             * because we don't need anything initialized when the HttpRequest is initialized, we override
             * the interface and provide a no-op function.
             */
            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {}
            }).setApplicationName("youtube-cmdline-search-sample").build();

            // Get query term from user.
            String queryTerm = "롤";

            YouTube.Search.List search = youtube.search().list(Arrays.asList("id"));

            String apiKey = properties.getProperty("youtube.apikey");

//            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");

            SearchListResponse searchResponse = search.setKey(apiKey)
                    .setQ(queryTerm)
                    .setType(Arrays.asList("video"))
                    .setOrder("relevance")
                    .setMaxResults(NUMBER_OF_VIDEOS_RETURNED)
                    .setFields("items(id/videoId)")
                    .execute();

            List<SearchResult> searchResultList = searchResponse.getItems();

            return searchResultList;

        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }
}
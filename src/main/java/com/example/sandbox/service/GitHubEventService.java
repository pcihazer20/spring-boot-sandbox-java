package com.example.sandbox.service;

import com.example.sandbox.dto.GitHubEventScoreResponse;
import com.example.sandbox.enums.GitHubEvent;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GitHubEventService {

    private static final String GITHUB_EVENTS_URL = "https://api.github.com/users/{username}/events";

    private final RestTemplate restTemplate;

    public GitHubEventScoreResponse calculateScore(String username) {
        JsonNode[] events = restTemplate.getForObject(GITHUB_EVENTS_URL, JsonNode[].class, username);

        int totalPoints = 0;
        Map<String, Integer> breakdown = new LinkedHashMap<>();

        if (events != null) {
            for (JsonNode event : events) {
                String type = event.get("type").asText();
                GitHubEvent gitHubEvent = resolve(type);
                int points = gitHubEvent.getPoints();

                totalPoints += points;
                breakdown.merge(type, points, Integer::sum);
            }
        }

        return new GitHubEventScoreResponse(username, totalPoints, breakdown);
    }

    private GitHubEvent resolve(String type) {
        try {
            return GitHubEvent.valueOf(type);
        } catch (IllegalArgumentException e) {
            return GitHubEvent.EverythingElse;
        }
    }
}

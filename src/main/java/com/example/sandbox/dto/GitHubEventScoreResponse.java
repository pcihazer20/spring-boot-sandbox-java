package com.example.sandbox.dto;

import java.util.Map;

public record GitHubEventScoreResponse(
        String username,
        int totalPoints,
        Map<String, Integer> eventBreakdown
) {}

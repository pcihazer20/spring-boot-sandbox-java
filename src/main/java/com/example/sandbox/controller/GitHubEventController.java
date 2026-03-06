package com.example.sandbox.controller;

import com.example.sandbox.dto.GitHubEventScoreResponse;
import com.example.sandbox.service.GitHubEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/github-events/")
@RequiredArgsConstructor
public class GitHubEventController {

    private final GitHubEventService gitHubEventService;

    @GetMapping("/{username}")
    public GitHubEventScoreResponse getScore(@PathVariable String username) {
        return gitHubEventService.calculateScore(username);
    }
}

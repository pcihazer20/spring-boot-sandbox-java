package com.example.sandbox.enums;

public enum GitHubEvent {

    PullRequestEvent(5),
    ForkEvent(4),
    IssueCommentEvent(3),
    PushEvent(2),
    EverythingElse(1);  //TODO may not need it

    private final int points;

    GitHubEvent(int points) {
        this.points = points;
    }

    public int getPoints() {
        return points;
    }
}

package com.mar.zur.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageSolvingResponse extends Response {

    private boolean success;
    private int score;
    private int highScore;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageSolvingResponse{" +
                "success=" + success +
                ", lives=" + getLives() +
                ", gold=" + getGold() +
                ", score=" + score +
                ", highScore=" + highScore +
                ", turn=" + getTurn() +
                ", message='" + message + '\'' +
                '}';
    }
}

package br.com.springstudiouslabaws.exceptions;

import java.time.LocalDateTime;

public class ApiErrorMessage {

    private String title;
    private String message;
    private String description;
    private int statusCode;
    private LocalDateTime timestamp;

    public ApiErrorMessage() {
    }

    public ApiErrorMessage(String title, String description, int statusCode, LocalDateTime timestamp) {
        this.title = title;
        this.description = description;
        this.statusCode = statusCode;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}


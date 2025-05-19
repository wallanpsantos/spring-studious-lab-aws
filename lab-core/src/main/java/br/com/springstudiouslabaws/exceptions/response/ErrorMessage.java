package br.com.springstudiouslabaws.exceptions.response;

import java.util.List;

public record ErrorMessage(String status,
                           Integer statusCode,
                           String title,
                           String message,
                           String timestamp,
                           List<ErrorDetailsMessage> details) {
}

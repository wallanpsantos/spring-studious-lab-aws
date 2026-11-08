package br.com.springstudiouslabaws.exceptions;

import java.util.List;

public record ApiErrorResponse(ErrorBody error) {

    public record ErrorBody(String code, String message, List<FieldDetail> details) {
    }

    public record FieldDetail(String field, String message) {
    }

    public static ApiErrorResponse of(String code, String message, List<FieldDetail> details) {
        return new ApiErrorResponse(new ErrorBody(code, message, details));
    }
}

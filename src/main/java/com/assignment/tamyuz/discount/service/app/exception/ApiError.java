package com.assignment.tamyuz.discount.service.app.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private int code;        // e.g., "BAD_REQUEST", "NOT_FOUND"
    private String message;     // Human-readable message
    private Object details;     // Optional: stack trace, field errors
    private String href;        // Optional: link to documentation or help page
    private Instant timestamp;  // Error occurrence time

    public ApiError(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = Instant.now();
    }

    public ApiError(int code, String message, Object details) {
        this.code = code;
        this.message = message;
        this.details = details;
        this.timestamp = Instant.now();
    }
}


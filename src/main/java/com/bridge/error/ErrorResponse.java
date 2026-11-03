package com.bridge.error;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class ErrorResponse {

    private String message;

    public static ErrorResponse of(String message) {
        return new ErrorResponse(message);
    }

}

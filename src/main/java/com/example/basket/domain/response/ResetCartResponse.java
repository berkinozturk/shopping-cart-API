package com.example.basket.domain.response;

public class ResetCartResponse {
    private boolean result;
    private String message;

    public ResetCartResponse(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

}

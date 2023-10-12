package com.example.basket.domain.response;

public class RemoveItemResponse {
    private boolean result;
    private String message;

    public RemoveItemResponse(boolean result, String message) {
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

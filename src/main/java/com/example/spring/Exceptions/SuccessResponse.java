package com.example.spring.Exceptions;

import lombok.Data;

@Data
public class SuccessResponse {
    private String status = "success";
    private String message;
    private int statusCode;
    private Object data;

    public SuccessResponse(String message, int statusCode, Object data) {
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
    }
}

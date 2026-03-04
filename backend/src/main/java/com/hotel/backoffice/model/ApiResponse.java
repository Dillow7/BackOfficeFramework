package com.hotel.backoffice.model;

public class ApiResponse<T> {
    private String status;
    private int code;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(String status, int code, T data) {
        this.status = status;
        this.code = code;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

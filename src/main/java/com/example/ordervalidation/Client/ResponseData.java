package com.example.ordervalidation.Client;


public class ResponseData {
    private String name;
    private Object data;
    private String status;
    private int code;

    public ResponseData(String name, Object data) {
        this.name = name;
        this.data = data;
    }

    public ResponseData() {
    }

    public ResponseData(String name, Object data, int code) {
        this.name = name;
        this.data = data;
        this.code = code;
    }

    public ResponseData(String name, Object data, String status, int code) {
        this.name = name;
        this.data = data;
        this.status = status;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
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
}

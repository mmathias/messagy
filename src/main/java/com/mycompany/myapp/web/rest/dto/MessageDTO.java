package com.mycompany.myapp.web.rest.dto;

import java.util.List;

public class MessageDTO {

    private List<List<Object>> data;

    private List<Header> headers;

    public List<List<Object>> getData() {
        return data;
    }

    public void setData(List<List<Object>> data) {
        this.data = data;
    }

    public List<Header> getHeaders() {
        return headers;
    }

    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }
}

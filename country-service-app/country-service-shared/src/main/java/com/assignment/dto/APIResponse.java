package com.assignment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class APIResponse {

    private boolean success = true;
    private Object data;
    private String message;

    public APIResponse(final Object data) {
        super();
        this.data = data;
    }

    public APIResponse(final boolean success, final String message) {
        super();
        this.success = success;
        this.message = message;
    }

}

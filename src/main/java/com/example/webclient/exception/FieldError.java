package com.example.webclient.exception;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class FieldError implements Serializable {

    private String name;
    private String field;
    private String message;
    private String messageCode;
}

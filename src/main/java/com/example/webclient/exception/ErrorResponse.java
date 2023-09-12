package com.example.webclient.exception;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ErrorResponse implements Serializable {

	private Long timestamp;
	private String path;
	private Integer status;
	private String errorCode;
	private String message;
	private String errorMessage;
	private String traceId;
	private List<FieldError> fieldErrors;
}

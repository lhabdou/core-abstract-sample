package com.example.demo.errors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class VersionException extends Exception {

	private static final long serialVersionUID = 4688654117397550L;

	private String message;

	private Integer code;

}

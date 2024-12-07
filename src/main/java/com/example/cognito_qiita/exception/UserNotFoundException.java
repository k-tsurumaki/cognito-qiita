package com.example.cognito_qiita.exception;

public class UserNotFoundException extends RuntimeException {
	public UserNotFoundException(Long userId) {
		super("User ID:" + userId + "is not found.");
	}
}

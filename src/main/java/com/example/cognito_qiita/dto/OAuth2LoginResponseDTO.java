package com.example.cognito_qiita.dto;

public class OAuth2LoginResponseDTO {
	private String idToken;
	private Long userId;

	public OAuth2LoginResponseDTO(String idToken, Long userId) {
		this.idToken = idToken;
		this.userId = userId;
	}

	public String getIdToken() {
		return idToken;
	}

	public Long getUserId() {
		return userId;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public static class Builder {
		private String idToken;
		private Long userId;

		public Builder idToken(String idToken) {
			this.idToken = idToken;
			return this;
		}

		public Builder userId(Long userId) {
			this.userId = userId;
			return this;
		}

		public OAuth2LoginResponseDTO build() {
			return new OAuth2LoginResponseDTO(idToken, userId);
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	@Override
	public String toString() {
		return "OAuth2LoginResponseDTO{" +
				"idToken='" + idToken + '\'' +
				", userId=" + userId +
				'}';
	}
}

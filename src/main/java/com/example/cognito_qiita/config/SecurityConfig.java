package com.example.cognito_qiita.config;

import com.example.cognito_qiita.dto.OAuth2LoginResponseDTO;
import com.example.cognito_qiita.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
	private UserService userService;

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(Customizer.withDefaults())
				.cors(c -> c.configurationSource(corsConfigurationSource()))
				.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests((authorize) -> authorize
						.requestMatchers("/auth/**").permitAll()
						.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.anyRequest().authenticated()
				)
				.oauth2Login(oauth2 -> oauth2.successHandler(this::handleOAuth2LoginSuccess))
				.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

		return http.build();
	}

	private void handleOAuth2LoginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
		try {
			DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
			String idToken = oidcUser.getIdToken().getTokenValue();
			Map<String, Object> attributes = oidcUser.getClaims();
			String username = (String) attributes.get("name");
			String email = (String) attributes.get("email");

			// ユーザーIDをDBに保存
			Long userId = userService.createUser(username, email);

			// DTOを使ってレスポンスを作成
			OAuth2LoginResponseDTO responseDTO = new OAuth2LoginResponseDTO(idToken, userId);

			// JSON形式でレスポンスボディに書き込む
			writeJsonResponse(response, responseDTO);

			// ステータスコードを設定
			response.setStatus(HttpServletResponse.SC_OK);
		} catch (Exception e) {
			// エラーハンドリング: 失敗した場合は適切なレスポンスを返す
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write("{\"error\": \"Authentication failed\"}");
		}
	}

	private void writeJsonResponse(HttpServletResponse response, OAuth2LoginResponseDTO responseDTO) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		String jsonResponse = String.format("{\"idToken\": \"%s\", \"userId\": %d}", responseDTO.getIdToken(), responseDTO.getUserId());
		response.getWriter().write(jsonResponse);
	}
}

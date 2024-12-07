package com.example.cognito_qiita.service;

import com.example.cognito_qiita.dto.UserDTO;
import com.example.cognito_qiita.entity.User;
import com.example.cognito_qiita.exception.UserNotFoundException;
import com.example.cognito_qiita.repository.UserRepository;
import org.hibernate.service.UnknownServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;


	public Long createUser(String username, String email) {
		// 同じemailのユーザーが存在するかをチェック
		Optional<User> existingUser = userRepository.findByEmailAndDeletedFlgFalse(email);

		if (existingUser.isPresent()) {
			// 既存のユーザーが存在すれば、そのユーザーのuserIdを返す
			return existingUser.get().getUserId();
		} else {
			// 存在しなければ新規ユーザーを登録
			User user = User.builder()
					.username(username)
					.email(email)
					.role("user")
					.build();
			user = userRepository.save(user);
			return user.getUserId();
		}
	}


	public List<UserDTO> getAllUsers() {
		List<User> users = userRepository.findByDeletedFlgFalse();
		List<UserDTO> userDTOs = new ArrayList<>();
		for (User user: users) {
			UserDTO userDTO = UserDTO.builder()
					.userId(user.getUserId())
					.username(user.getUsername())
					.email(user.getEmail())
					.role(user.getRole())
					.build();
			userDTOs.add(userDTO);
		}
		return userDTOs;
	}

	public UserDTO getUser(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(()->new UserNotFoundException(userId));
		UserDTO userDTO = UserDTO.builder()
				.userId(user.getUserId())
				.username(user.getUsername())
				.email(user.getEmail())
				.role(user.getRole())
				.build();
		return userDTO;
	}
}

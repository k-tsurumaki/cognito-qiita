package com.example.cognito_qiita.controller;

import com.example.cognito_qiita.dto.UserDTO;
import com.example.cognito_qiita.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping
	public ResponseEntity<List<UserDTO>> getAllUsers() {
		List<UserDTO> userDTOs = userService.getAllUsers();
		return ResponseEntity.ok().body(userDTOs);
	}

	@GetMapping("{userId}")
	public ResponseEntity<UserDTO> getUser(@PathVariable("userId") Long userId){
		UserDTO userDTO = userService.getUser(userId);
		return ResponseEntity.ok().body(userDTO);
	}
}

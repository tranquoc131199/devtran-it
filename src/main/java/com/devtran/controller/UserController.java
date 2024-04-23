/**
 * 
 */
package com.devtran.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.devtran.dto.request.ApiReponse;
import com.devtran.dto.request.UserCreationRequest;
import com.devtran.dto.request.UserUpdateRequest;
import com.devtran.dto.response.UserResponse;
import com.devtran.entity.User;
import com.devtran.mapper.UserMapper;
import com.devtran.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * @author pc
 *
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {

	UserService userService;
	UserMapper userMapper;

	@PostMapping
	ApiReponse<User> createUser(@RequestBody @Valid UserCreationRequest request) {

		ApiReponse<User> respose = new ApiReponse<>();
		respose.setResult(userService.createRequest(request));

		return respose;
	}

	@GetMapping
	List<User> getUsers() {
		return userService.getUsers();
	}

	@GetMapping("/{userId}")
	UserResponse getUserById(@PathVariable("userId") String userId) {
		return userMapper.toUserResponse(userService.getUserById(userId));
	}

	@PutMapping("/{userId}")
	UserResponse putMethodName(@PathVariable String userId, @RequestBody UserUpdateRequest request) {

		return userMapper.toUserResponse(userService.updateRequest(userId, request));
	}

}

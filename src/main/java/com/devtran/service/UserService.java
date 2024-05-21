/**
 * 
 */
package com.devtran.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.devtran.dto.request.UserCreationRequest;
import com.devtran.dto.request.UserUpdateRequest;
import com.devtran.dto.response.UserResponse;
import com.devtran.entity.User;
import com.devtran.enums.Role;
import com.devtran.exception.AppException;
import com.devtran.exception.ErrorCode;
import com.devtran.mapper.UserMapper;
import com.devtran.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author pc
 *
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

	UserRepository repository;
	UserMapper userMapper;
	PasswordEncoder passwordEncoder;

	public User createRequest(UserCreationRequest request) {
		if (repository.existsByUsername(request.getUsername())) {
			throw new AppException(ErrorCode.USER_EXISTED);
		}
		User user = userMapper.toUser(request);

		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		HashSet<String> roles = new HashSet<>();
		roles.add(Role.USER.name());
		user.setRoles(roles);
		
		return repository.save(user);
	}

	public List<UserResponse> getUsers(){
        return repository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

	public User getUserById(String userId) {
		return repository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
	}

	public User updateRequest(String userId, UserUpdateRequest request) {
		User user = getUserById(userId);
		userMapper.updateUser(user, request);
		return repository.save(user);
	}

}

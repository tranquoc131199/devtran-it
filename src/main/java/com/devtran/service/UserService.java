/**
 * 
 */
package com.devtran.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.devtran.repository.RoleRepository;
import com.devtran.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pc
 *
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

	UserRepository repository;
	UserMapper userMapper;
	PasswordEncoder passwordEncoder;
	RoleRepository roleRepository;

	public UserResponse createRequest(UserCreationRequest request) {
		log.info("Service: createUser");
		if (repository.existsByUsername(request.getUsername())) {
			throw new AppException(ErrorCode.USER_EXISTED);
		}
		User user = userMapper.toUser(request);

		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		HashSet<String> roles = new HashSet<>();
		roles.add(Role.USER.name());
		//user.setRoles(roles);
		
		return userMapper.toUserResponse(repository.save(user));
	}

	//@PreAuthorize("hasRole('ADMIN')")
	@PreAuthorize("hasAuthority('UPDATE_DATA')")
	public List<UserResponse> getUsers(){
		log.info("In method get Users");
        return repository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

	@PostAuthorize("returnObject.username == authentication.name")
	public User getUserById(String userId) {
		return repository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
	}

	public User updateRequest(String userId, UserUpdateRequest request) {
		User user = getUserById(userId);
		userMapper.updateUser(user, request);
		
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		var roles = roleRepository.findAllById(request.getRoles());
		user.setRoles(new HashSet<>(roles));
		
		return repository.save(user);
	}
	
	@PostAuthorize("returnObject.username == authentication.name")
	public UserResponse getMyInfo() {
		var context = SecurityContextHolder.getContext();
		String name = context.getAuthentication().getName();
		User user =  repository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
		
		return userMapper.toUserResponse(user);
	}

}

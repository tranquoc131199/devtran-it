/**
 * 
 */
package com.devtran.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.devtran.dto.request.UserCreationRequest;
import com.devtran.dto.request.UserUpdateRequest;
import com.devtran.dto.response.UserResponse;
import com.devtran.entity.User;

/**
 * @author pc
 *
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

	User toUser(UserCreationRequest request);

	void updateUser(@MappingTarget User user, UserUpdateRequest updateRequest);

	UserResponse toUserResponse(User user);

}

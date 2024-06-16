/**
 * 
 */
package com.devtran.service;

import java.time.LocalDate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.devtran.dto.request.UserCreationRequest;
import com.devtran.dto.response.UserResponse;
import com.devtran.entity.User;
import com.devtran.exception.AppException;
import com.devtran.repository.UserRepository;

import lombok.var;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author pc
 *
 */

@SpringBootTest
public class UserServiceTest {
	@Autowired
	private UserService userService;
	
	@MockBean
	private UserRepository userRepository;
	
	private UserCreationRequest request;
    private UserResponse userResponse;
    private User user;
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(1990, 1, 1);

        request = UserCreationRequest.builder()
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .password("12345678")
                .lod(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("cf0600f538b3")
                .username("john")
                .firstName("John")
                .lastName("Doe")
                .lod(dob)
                .build();
        
        user = User.builder()
        		 .id("cf0600f538b3")
                 .username("john")
                 .firstName("John")
                 .lastName("Doe")
                 .lod(dob)
        		 .build();
    }
    
    @Test
    void createUser_validRequest_success() {
    	// GIVE
    	when(userRepository.existsByUsername(anyString())).thenReturn(false);
    	when(userRepository.save(any())).thenReturn(user);
    	
    	// THEN
    	var reponse = userService.createRequest(request);
    	
    	// WHEN
    	Assertions.assertThat(reponse.getId()).isEqualTo("cf0600f538b3");
    	Assertions.assertThat(reponse.getUsername()).isEqualTo("john");
    	
    }
    
    @Test
    void createUser_userExisted_fail() {
    	// GIVE
    	when(userRepository.existsByUsername(anyString())).thenReturn(true);
    	when(userRepository.save(any())).thenReturn(user);
    	
    	// THEN
    	//var reponse = userService.createRequest(request);    	
    	
    	// WHEN
    	var exeption =  assertThrows(AppException.class, () -> userService.createRequest(request));
    	
    	assertThat(exeption.getErrorCode().getCode()).isEqualTo(1005);
    	
    	
    }

}

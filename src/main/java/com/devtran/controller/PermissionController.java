/**
 * 
 */
package com.devtran.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devtran.dto.request.ApiReponse;
import com.devtran.dto.request.PermissionRequest;
import com.devtran.dto.response.PermissionResponse;
import com.devtran.mapper.UserMapper;
import com.devtran.service.PermissionService;
import com.devtran.service.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pc
 *
 */
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionController {
	PermissionService permissionService;
	
	@PostMapping
	ApiReponse<PermissionResponse> create(@RequestBody PermissionRequest request){
		return ApiReponse.<PermissionResponse>builder()
				.result(permissionService.create(request))
				.build();
	}
	
	@GetMapping
	ApiReponse<List<PermissionResponse>> getAll(){
		return ApiReponse.<List<PermissionResponse>>builder()
				.result(permissionService.getAll())
				.build();
	}
	
	@DeleteMapping("/{permission}")
	ApiReponse<Void> delete(@PathVariable String permission){
		permissionService.delete(permission);
		return ApiReponse.<Void>builder().build();
	}
}

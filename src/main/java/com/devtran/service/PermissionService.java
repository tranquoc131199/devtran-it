/**
 * 
 */
package com.devtran.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.devtran.dto.request.PermissionRequest;
import com.devtran.dto.response.PermissionResponse;
import com.devtran.entity.Permission;
import com.devtran.mapper.PermissionMapper;
import com.devtran.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.var;
import lombok.experimental.FieldDefaults;

/**
 * @author pc
 *
 */

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {

	PermissionRepository permissionRepository;
	PermissionMapper permissionMapper;

	public PermissionResponse create(PermissionRequest request) {
		Permission permission = permissionMapper.toPermission(request);
		permission = permissionRepository.save(permission);

		return permissionMapper.topPermissionResponse(permission);
	}

	public List<PermissionResponse> getAll() {
		var permission = permissionRepository.findAll();
		return permission.stream().map(permissionMapper::topPermissionResponse).toList();
	}
	
	public void delete(String permissionName) {
		permissionRepository.deleteById(permissionName);
	}
}

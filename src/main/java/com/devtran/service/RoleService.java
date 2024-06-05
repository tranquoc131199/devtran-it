/**
 * 
 */
package com.devtran.service;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.devtran.dto.request.RoleRequest;
import com.devtran.dto.response.RoleResponse;
import com.devtran.entity.Role;
import com.devtran.mapper.RoleMapper;
import com.devtran.repository.PermissionRepository;
import com.devtran.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.var;
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
public class RoleService {

	RoleRepository roleRepository;
	RoleMapper roleMapper;
	PermissionRepository permissionRepository;

	public RoleResponse create(RoleRequest request) {
		var role = roleMapper.toRole(request);
		var permissions = permissionRepository.findAllById(request.getPermissions());

		role.setPermissions(new HashSet<>(permissions));
		role = roleRepository.save(role);

		return roleMapper.toRoleResponse(role);
	}

	public List<RoleResponse> getAll() {
		return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
	}

	public void delete(String role) {
		roleRepository.deleteById(role);
	}

}

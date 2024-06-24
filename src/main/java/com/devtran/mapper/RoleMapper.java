/**
 *
 */
package com.devtran.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.devtran.dto.request.RoleRequest;
import com.devtran.dto.response.RoleResponse;
import com.devtran.entity.Role;

/**
 * @author pc
 *
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}

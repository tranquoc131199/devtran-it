/**
 *
 */
package com.devtran.mapper;

import org.mapstruct.Mapper;

import com.devtran.dto.request.PermissionRequest;
import com.devtran.dto.response.PermissionResponse;
import com.devtran.entity.Permission;

/**
 * @author pc
 *
 */
@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse topPermissionResponse(Permission permission);
}

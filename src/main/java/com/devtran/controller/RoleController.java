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
import com.devtran.dto.request.RoleRequest;
import com.devtran.dto.response.RoleResponse;
import com.devtran.service.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author pc
 *
 */
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @PostMapping
    ApiReponse<RoleResponse> create(@RequestBody RoleRequest request) {
        return ApiReponse.<RoleResponse>builder()
                .result(roleService.create(request))
                .build();
    }

    @GetMapping
    ApiReponse<List<RoleResponse>> getAll() {
        return ApiReponse.<List<RoleResponse>>builder()
                .result(roleService.getAll())
                .build();
    }

    @DeleteMapping("/{role}")
    ApiReponse<Void> delete(@PathVariable String role) {
        roleService.delete(role);
        return ApiReponse.<Void>builder().build();
    }
}

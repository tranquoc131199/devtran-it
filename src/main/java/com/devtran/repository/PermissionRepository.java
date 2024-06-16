/**
 * 
 */
package com.devtran.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.devtran.entity.Permission;

/**
 * @author pc
 *
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

}

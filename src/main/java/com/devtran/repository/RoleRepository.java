/**
 * 
 */
package com.devtran.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.devtran.entity.Role;

/**
 * @author pc
 *
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

}

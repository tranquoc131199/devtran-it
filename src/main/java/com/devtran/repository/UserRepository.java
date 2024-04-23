/**
 * 
 */
package com.devtran.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devtran.entity.User;

/**
 * @author pc
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
	boolean existsByUsername(String username);

}

/**
 * 
 */
package com.devtran.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devtran.entity.User;

/**
 * @author pc
 *
 */
@Repository
public interface AuthenticationRepository extends JpaRepository<User, String> {
	//boolean existByUsername(String username);

	Optional<User> findByUsername(String username);

}

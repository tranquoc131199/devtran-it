/**
 *
 */
package com.devtran.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devtran.entity.InvalidatedToken;

/**
 * @author pc
 *
 */
@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {}

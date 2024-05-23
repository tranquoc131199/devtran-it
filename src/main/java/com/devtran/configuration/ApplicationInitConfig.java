/**
 * 
 */
package com.devtran.configuration;

import java.util.HashSet;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.devtran.entity.User;
import com.devtran.enums.Role;
import com.devtran.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

/**
 * @author pc
 *
 */

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

	PasswordEncoder passwordEncoder;

	@Bean
	ApplicationRunner applicationRunner(UserRepository userRepository) {
		log.info("Init application.....");
		return args -> {
			if (userRepository.findByUsername("admin").isEmpty()) {
				HashSet<String> roles = new HashSet<>();
				roles.add(Role.ADMIN.name());

				User user = User.builder()
						.username("admin")
						.password(passwordEncoder.encode("admin"))
						.roles(roles)
						.build();
				
				userRepository.save(user);
				log.warn("admin user has been cerated with default pass");
			}
		};
	}
}

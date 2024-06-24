/**
 *
 */
package com.devtran.dto.request;

import java.time.LocalDate;
import java.util.List;

import com.devtran.validator.DobConstraint;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author pc
 *
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {

    String password;
    String firstName;
    String lastName;

    @DobConstraint(min = 2, message = "INVALID_DOB")
    LocalDate dob;

    List<String> roles;
}

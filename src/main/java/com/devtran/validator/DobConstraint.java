/**
 * 
 */
package com.devtran.validator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * @author pc
 *
 */

@Target({ FIELD })
@Retention(RUNTIME)
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {
	
	int min();
	
	String message() default "invalid date of birthdate";

	Class<?>[] groups() default { };

	Class<? extends Payload>[] payload() default { };

}

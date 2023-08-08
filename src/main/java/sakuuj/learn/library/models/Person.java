package sakuuj.learn.library.models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import sakuuj.learn.library.constants.ValidationErrorMessages;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Person {
    private int id;

    @NotBlank(message = ValidationErrorMessages.ON_BLANK)
    @Length.List({
            @Length(min = 1, message = ValidationErrorMessages.ON_SMALL_LENGTH),
            @Length(max = 150, message = ValidationErrorMessages.ON_LARGE_LENGTH)
    })
    private String name;

    @Min(value = 1900, message = ValidationErrorMessages.ON_SMALL_VALUE)
    private int yearOfBirth;
}

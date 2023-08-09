package sakuuj.learn.library.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
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
public class Book {
    private int id;

    @NotBlank
    @Length.List({
            @Length(min=1, message = ValidationErrorMessages.ON_SMALL_LENGTH),
            @Length(max=200, message = ValidationErrorMessages.ON_LARGE_LENGTH)
    })
    private String name;

    @NotBlank
    @Length.List({
            @Length(min=1, message = ValidationErrorMessages.ON_SMALL_LENGTH),
            @Length(max=150, message = ValidationErrorMessages.ON_LARGE_LENGTH)
    })
    private String authorName;

    @Positive
    private int yearOfPublishing;

    private Integer personId;
}

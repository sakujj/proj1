package sakuuj.learn.library.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.CollectionId;
import org.hibernate.validator.constraints.Length;
import sakuuj.learn.library.constants.ValidationErrorMessages;

@Entity
@Table(name="Book")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Book {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @NotBlank
    @Length.List({
            @Length(min=1, message = ValidationErrorMessages.ON_SMALL_LENGTH),
            @Length(max=200, message = ValidationErrorMessages.ON_LARGE_LENGTH)
    })
    @Column(name = "name")
    private String name;

    @NotBlank
    @Length.List({
            @Length(min=1, message = ValidationErrorMessages.ON_SMALL_LENGTH),
            @Length(max=150, message = ValidationErrorMessages.ON_LARGE_LENGTH)
    })
    @Column(name="authorName")
    private String authorName;

    @Positive
    @Column(name="yearOfPublishing")
    private int yearOfPublishing;

    @ManyToOne
    @JoinColumn(name = "personId", referencedColumnName = "id")
    private Person owner;
}

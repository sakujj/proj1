package sakuuj.learn.library.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import sakuuj.learn.library.constants.ValidationErrorMessages;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Person")
public class Person {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = ValidationErrorMessages.ON_BLANK)
    @Length.List({
            @Length(min = 1, message = ValidationErrorMessages.ON_SMALL_LENGTH),
            @Length(max = 150, message = ValidationErrorMessages.ON_LARGE_LENGTH)
    })
    @Column(name = "name")
    private String name;

    @Min(value = 1900, message = ValidationErrorMessages.ON_SMALL_VALUE)
    @Column(name = "yearOfBirth")
    private int yearOfBirth;

    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", yearOfBirth=" + yearOfBirth +
                '}';
    }
}

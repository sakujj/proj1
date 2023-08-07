package sakuuj.learn.library.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {
    private int id;
    private String name;
    private String authorName;
    private int yearOfPublishing;
    private int personId;
}

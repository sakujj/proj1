package sakuuj.learn.library.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import sakuuj.learn.library.models.Book;
import sakuuj.learn.library.services.BookService;
import sakuuj.learn.library.services.PersonService;

@Component
public class BookValidator implements Validator {
    PersonService personService;
    BookService bookService;

    @Autowired
    public BookValidator(PersonService personService,
                         BookService bookService) {
        this.personService = personService;
        this.bookService = bookService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
    }
}

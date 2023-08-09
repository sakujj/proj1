package sakuuj.learn.library.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sakuuj.learn.library.constants.ValidationErrorMessages;
import sakuuj.learn.library.dao.BookDao;
import sakuuj.learn.library.dao.PersonDao;
import sakuuj.learn.library.models.Book;

@Component
public class BookValidator implements Validator {
    PersonDao personDao;
    BookDao bookDao;

    @Autowired
    public BookValidator(PersonDao personDao, BookDao bookDao) {
        this.personDao = personDao;
        this.bookDao = bookDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;
        if (book.getPersonId() != null
        && personDao.selectById(book.getPersonId()).isEmpty())
            errors.rejectValue("personId", "",
                    ValidationErrorMessages.ON_NO_SUCH_OWNER_EXIST);
    }
}

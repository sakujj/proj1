package sakuuj.learn.library.validators;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sakuuj.learn.library.constants.ValidationErrorMessages;
import sakuuj.learn.library.dao.PersonDao;
import sakuuj.learn.library.models.Person;

@Component
public class PersonValidator implements Validator {
    private final PersonDao personDao;

    public PersonValidator(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if (personDao.selectByName(person.getName()).isPresent())
            errors.rejectValue("name","", ValidationErrorMessages.ON_PERSON_NAME_ALREADY_TAKEN);
    }
}

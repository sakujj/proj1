package sakuuj.learn.library.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sakuuj.learn.library.constants.ValidationErrorMessages;
import sakuuj.learn.library.models.Person;
import sakuuj.learn.library.repositories.PersonRepository;
import sakuuj.learn.library.services.PersonService;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> personWithSameName = personService.findByName(person.getName());
        if (personWithSameName.isPresent()) {
            Person pWithSameName = personWithSameName.get();
            if (pWithSameName.getId() != person.getId())
                errors.rejectValue("name","", ValidationErrorMessages.ON_PERSON_NAME_ALREADY_TAKEN);
        }
    }
}

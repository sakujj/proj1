package sakuuj.learn.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sakuuj.learn.library.dao.PersonDao;
import sakuuj.learn.library.models.Person;
import sakuuj.learn.library.validators.PersonValidator;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonDao personDao;
    private final PersonValidator validator;

    @Autowired
    public PeopleController(PersonDao personDao, PersonValidator validator) {
        this.personDao = personDao;
        this.validator = validator;
    }

    @GetMapping()
    public String getAll(Model model) {
        List<Person> people = personDao.selectAll();
        model.addAttribute("people", people);
        return "people/all";
    }

    @GetMapping("/{id}")
    public String getSpecified(@PathVariable("id") int id,
                               @ModelAttribute("person") Person person) {
        Optional<Person> p = personDao.selectById(id);
        if (p.isPresent()) {
            initModelAttributePerson(person, p.get());

            return "people/one";
        } else {
            return "redirect:/people";
        }
    }

    @GetMapping("/{id}/edit")
    public String getEditingPage(@PathVariable("id") int id,
                                 @ModelAttribute("person") Person person) {
        Optional<Person> p = personDao.selectById(id);
        if (p.isEmpty())
            return "redirect:/people";

        initModelAttributePerson(person, p.get());

        return "people/edit";
    }

    @GetMapping("/new")
    public String getCreationPage(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @DeleteMapping("/{id}")
    public String deleteSpecified(@PathVariable("id") int id) {
        if (personDao.selectById(id).isPresent())
            personDao.deleteById(id);
        return "redirect:/people";
    }

    @PatchMapping("/{id}")
    public String updateSpecified(@PathVariable("id") int id,
                                  @ModelAttribute("person") @Valid Person person,
                                  BindingResult bindingResult) {
        validator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "/people/edit";
        if (personDao.selectById(id).isPresent()) {
            person.setId(id);
            personDao.updateById(person);
        }

        return "redirect:/people";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        validator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "/people/new";
        personDao.insert(person);
        return "redirect:/people";
    }

    private static void initModelAttributePerson(Person modelAttribute,
                                                 Person existingPerson) {
        modelAttribute.setId(existingPerson.getId());
        modelAttribute.setName(existingPerson.getName());
        modelAttribute.setYearOfBirth(existingPerson.getYearOfBirth());
    }

}

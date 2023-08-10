package sakuuj.learn.library.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sakuuj.learn.library.models.Book;
import sakuuj.learn.library.models.Person;
import sakuuj.learn.library.repositories.PersonRepository;
import sakuuj.learn.library.services.BookService;
import sakuuj.learn.library.services.PersonService;
import sakuuj.learn.library.validators.PersonValidator;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonService personService;
    private final BookService bookService;
    private final PersonValidator validator;

    @Autowired
    public PeopleController(PersonService personService,
                            BookService bookService,
                            PersonValidator validator) {
        this.personService = personService;
        this.bookService = bookService;
        this.validator = validator;
    }

    @GetMapping()
    public String getAll(Model model) {
        List<Person> people = personService.findAll();
        model.addAttribute("people", people);
        return "people/all";
    }

    @GetMapping("/{id}")
    public String getSpecified(@PathVariable("id") int id,
                               @ModelAttribute("person") Person person,
                               Model model) {
        Optional<Person> p = personService.findById(id);
        if (p.isPresent()) {
            copyPerson(person, p.get());

            List<Book> books = bookService.findByOwner(person);
            model.addAttribute("books", books);
            return "people/one";
        }

        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String getEditingPage(@PathVariable("id") int id,
                                 @ModelAttribute("person") Person person) {
        Optional<Person> p = personService.findById(id);
        if (p.isEmpty())
            return "redirect:/people";

        copyPerson(person, p.get());

        return "people/edit";
    }

    @GetMapping("/new")
    public String getCreationPage(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @DeleteMapping("/{id}")
    public String deleteSpecified(@PathVariable("id") int id) {
        if (personService.findById(id).isPresent())
            personService.deleteById(id);
        return "redirect:/people";
    }

    @PatchMapping("/{id}")
    public String updateSpecified(@PathVariable("id") int id,
                                  @ModelAttribute("person") @Valid Person person,
                                  BindingResult bindingResult) {
        person.setId(id);
        validator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "/people/edit";
        Optional<Person> selected = personService.findById(id);
        if (selected.isPresent()) {
            person.setId(id);
            personService.save(person);

           return "redirect:/people/" + id;
        }

        return "redirect:/people";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        validator.validate(person, bindingResult);

        if (bindingResult.hasErrors())
            return "/people/new";
        personService.save(person);
        return "redirect:/people";
    }

    private static void copyPerson(Person copyTo,
                                   Person copyFrom) {
        copyTo.setId(copyFrom.getId());
        copyTo.setName(copyFrom.getName());
        copyTo.setYearOfBirth(copyFrom.getYearOfBirth());
    }

}

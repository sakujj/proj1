package sakuuj.learn.library.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sakuuj.learn.library.dao.PersonDao;
import sakuuj.learn.library.models.Person;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PersonDao personDao;

    @Autowired
    public PeopleController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @GetMapping()
    public String getAll(Model model) {
        List<Person> people = personDao.selectAll();
        model.addAttribute("people", people);
        return "people/all";
    }

    @GetMapping("/{id}")
    public String getSpecified(@PathVariable("id") int id, Model model) {
        Optional<Person> person = personDao.selectById(id);
        if (person.isPresent()) {
            model.addAttribute("person", person.get());
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

        person = p.get();
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
                                  @ModelAttribute("person") Person person) {
        if (personDao.selectById(id).isPresent()) {
            person.setId(id);
            personDao.updateById(person);
        }

        return "redirect:/people";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") Person person) {
        personDao.insert(person);
        return "redirect:/people";
    }

}

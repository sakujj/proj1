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
import sakuuj.learn.library.validators.BookValidator;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private BookService bookService;
    private PersonService personService;
    private BookValidator validator;

    @Autowired
    public BooksController(BookService bookService,
                           PersonService personService,
                           BookValidator validator) {
        this.bookService = bookService;
        this.personService = personService;
        this.validator = validator;
    }


    @GetMapping()
    public String getAll(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books/all";
    }

    @GetMapping("/{id}")
    public String getSpecified(@PathVariable("id") int id,
                               @ModelAttribute("book") Book book,
                               @ModelAttribute("person") Person person,
                               Model model) {
        Optional<Book> b = bookService.findById(id);
     //   System.out.println("XXXXXXXXXXXXXXXXXXXxx");
        if (b.isPresent()) {
            copyBook(book, b.get());

            if (book.getOwner() != null) {
                Person owner = personService.findById(book.getOwner().getId()).get();
                model.addAttribute("owner", owner);
            } else {
                List<Person> possibleOwners = personService.findAll();
                model.addAttribute("people", possibleOwners);
            }

            return "books/one";
        }

        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String getEditingPage(@PathVariable("id") int id,
                                 @ModelAttribute("book") Book book) {
        Optional<Book> b = bookService.findById(id);
        if (b.isPresent()) {
            copyBook(book, b.get());
            return "books/edit";
        }

        return "redirect:/books";
    }

    @GetMapping("/new")
    public String getCreationPage(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PatchMapping("/{id}")
    public String updateSpecified(@PathVariable("id") int id,
                                  @ModelAttribute("book") @Valid Book book,
                                  BindingResult bindingResult) {
       // System.out.println("PPPPPPPPPPPPPPPPPPPPPPP");
        Optional<Book> selected = bookService.findById(id);
        if (selected.isPresent()) {
            book.setId(id);
            book.setOwner(selected.get().getOwner());
            bookService.save(book);
        }

        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assignOwner(@PathVariable("id") int id,
                              @ModelAttribute("owner") Person owner) {
        Optional<Book> book = bookService.findById(id);

        if (book.isPresent()) {
            Book selected = book.get();
            //System.out.println(owner);

            selected.setOwner(owner);

            bookService.save(selected);
        }
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String releaseOwner(@PathVariable("id") int id) {
        Optional<Book> book = bookService.findById(id);
        if (book.isPresent()) {
            Book selected = book.get();
            selected.setOwner(null);

            bookService.save(selected);
        }
        return "redirect:/books/" + id;
    }

    @PostMapping()
    public String create(@ModelAttribute @Valid Book book,
                         BindingResult bindingResult) {
        validator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "/books/new";

        bookService.save(book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteSpecified(@PathVariable("id") int id) {
        if (bookService.findById(id).isPresent()) {
            bookService.deleteById(id);
        }

        return "redirect:/books";
    }

    private static void copyBook(Book copyTo, Book copyFrom) {
        copyTo.setId(copyFrom.getId());
        copyTo.setName(copyFrom.getName());
        copyTo.setAuthorName(copyFrom.getAuthorName());
        copyTo.setYearOfPublishing(copyFrom.getYearOfPublishing());
        copyTo.setOwner(copyFrom.getOwner());
    }
}

package sakuuj.learn.library.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sakuuj.learn.library.dao.BookDao;
import sakuuj.learn.library.dao.PersonDao;
import sakuuj.learn.library.models.Book;
import sakuuj.learn.library.models.Person;
import sakuuj.learn.library.validators.BookValidator;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private BookDao bookDao;
    private PersonDao personDao;
    private BookValidator validator;

    @Autowired
    public BooksController(BookDao bookDao, PersonDao personDao, BookValidator validator) {
        this.bookDao = bookDao;
        this.personDao = personDao;
        this.validator = validator;
    }

    @GetMapping()
    public String getAll(Model model) {
        List<Book> books = bookDao.selectAll();
        model.addAttribute("books", books);
        return "books/all";
    }

    @GetMapping("/{id}")
    public String getSpecified(@PathVariable("id")int id,
                               @ModelAttribute("book") Book book,
    Model model) {
        Optional<Book> b = bookDao.selectById(id);
        if (b.isPresent()) {
            copyBook(book, b.get());

            if (book.getPersonId() != null) {
                model.addAttribute("isFree", false);
                Person owner = personDao.selectById(book.getPersonId()).get();
                model.addAttribute("owner", owner);
            } else {
                List<Person> possibleOwners = personDao.selectAll();
                model.addAttribute("people", possibleOwners);
                model.addAttribute("isFree", true);
            }

            return "books/one";
        }

        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String getEditingPage(@PathVariable("id")int id,
                                 @ModelAttribute("book") Book book) {
        Optional<Book> b = bookDao.selectById(id);
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
    public String updateSpecified(@PathVariable("id")int id,
                                  @ModelAttribute("book") @Valid Book book,
                                  BindingResult bindingResult) {

        Optional<Book> selected = bookDao.selectById(id);
        validator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "/books/edit";

        if (selected.isPresent()) {
            book.setId(id);
            bookDao.updateById(book);
        }

        return "redirect:/books";
    }

    @PostMapping()
    public String create(@ModelAttribute @Valid Book book,
    BindingResult bindingResult) {
        validator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "/books/new";

        bookDao.insert(book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteSpecified(@PathVariable("id")int id) {
        if (bookDao.selectById(id).isPresent()) {
            bookDao.deleteById(id);
        }

        return "redirect:/books";
    }

    private static void copyBook(Book copyTo, Book copyFrom) {
        copyTo.setId(copyFrom.getId());
        copyTo.setName(copyFrom.getName());
        copyTo.setAuthorName(copyFrom.getAuthorName());
        copyTo.setYearOfPublishing(copyFrom.getYearOfPublishing());
        copyTo.setPersonId(copyFrom.getPersonId());
    }
}

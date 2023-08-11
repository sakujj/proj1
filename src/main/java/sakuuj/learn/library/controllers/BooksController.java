package sakuuj.learn.library.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sakuuj.learn.library.models.Book;
import sakuuj.learn.library.models.Person;
import sakuuj.learn.library.services.BookService;
import sakuuj.learn.library.services.PersonService;
import sakuuj.learn.library.util.PaginationUtil;
import sakuuj.learn.library.validators.BookValidator;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

    private BookService bookService;
    private PersonService personService;
    private BookValidator validator;

    private static final int DEFAULT_PAGE_SIZE = 3;
    private static final int STARTING_PAGE_NUMBER = 1;

    private static final String YEAR_COLUMN_NAME = "yearOfPublishing";

    @Autowired
    public BooksController(BookService bookService,
                           PersonService personService,
                           BookValidator validator) {
        this.bookService = bookService;
        this.personService = personService;
        this.validator = validator;
    }

    @GetMapping()
    public String getAll(@RequestParam(value = "page", required = false)
                         String pageNumber,
                         @RequestParam(value = "books_per_page", required = false)
                         String booksPerPage,
                         @RequestParam(value = "sort_by_year", required = false)
                         String sortByYearParam,
                         Model model) {

        boolean sortByYear;
        sortByYear = Boolean.parseBoolean(sortByYearParam);

        int pageSize = DEFAULT_PAGE_SIZE;
        if (booksPerPage != null && !booksPerPage.isEmpty()) {
            try {
                pageSize = PaginationUtil.parsePageSize(booksPerPage, DEFAULT_PAGE_SIZE);
            } catch (NumberFormatException ex) {
                return "redirect:/books";
            }
        }

        int currentPage = PaginationUtil.getCurrentPage(pageNumber, STARTING_PAGE_NUMBER);
        Slice<Book> slice;
        if (sortByYear) {
            slice = bookService.findAll(PageRequest
                    .of(currentPage, pageSize, Sort.by(YEAR_COLUMN_NAME)));
        } else {
            slice = bookService.findAll(PageRequest.of(currentPage, pageSize));
        }

        if (!slice.hasContent()) {
            if (currentPage != 0)
                return "redirect:/books";
        }

        int prevPage = PaginationUtil.getPrevPage(currentPage, slice);
        int nextPage = PaginationUtil.getNextPage(currentPage, slice);
        List<Book> books = slice.getContent();

        model.addAttribute("books", books);
        model.addAttribute("prevPage", prevPage + 1);
        model.addAttribute("currentPage", currentPage + 1);
        model.addAttribute("nextPage", nextPage + 1);
        model.addAttribute("books_per_page", pageSize);
        model.addAttribute("sort_by_year", sortByYear);

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

package sakuuj.learn.library.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sakuuj.learn.library.models.Book;
import sakuuj.learn.library.models.Person;
import sakuuj.learn.library.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepo;

    @Autowired
    public BookService(BookRepository bookRepo) {
        this.bookRepo = bookRepo;
    }


    public List<Book> findAll() {
        return bookRepo.findAll();
    }

    public Optional<Book> findById(int id) {
        return bookRepo.findById(id);
    }

    public List<Book> findByOwner(Person owner) {
        return bookRepo.findByOwner(owner);
    }

    @Transactional
    public void update(int id, Book book) {
        book.setId(id);
        bookRepo.save(book);
    }

    @Transactional
    public void deleteById(int id) {
        bookRepo.deleteById(id);
    }

    @Transactional
    public Book save(Book book) {
        return bookRepo.save(book);
    }
}

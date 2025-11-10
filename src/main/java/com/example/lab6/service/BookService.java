package com.example.lab6.service;

import com.example.lab6.model.Book;
import com.example.lab6.repository.BookRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    private final BookRepository repo;

    public BookService(BookRepository repo) {
        this.repo = repo;
    }

    public Book create(Book book) {
        return repo.save(book);
    }

    public List<Book> listAll() {
        return repo.findAll();
    }

    public Optional<Book> get(Long id) {
        return repo.findById(id);
    }

    public Optional<Book> getByIsbn(String isbn) {
        return repo.findByIsbn(isbn);
    }

    public Book update(Long id, Book update) {
        return repo.findById(id).map(existing -> {
            existing.setTitle(update.getTitle());
            existing.setAuthor(update.getAuthor());
            existing.setIsbn(update.getIsbn());
            existing.setAvailable(update.isAvailable());
            return repo.save(existing);
        }).orElseThrow(() -> new IllegalArgumentException("Book not found: " + id));
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}

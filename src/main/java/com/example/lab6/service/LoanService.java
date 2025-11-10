package com.example.lab6.service;

import com.example.lab6.model.Book;
import com.example.lab6.model.Loan;
import com.example.lab6.model.Reader;
import com.example.lab6.repository.BookRepository;
import com.example.lab6.repository.LoanRepository;
import com.example.lab6.repository.ReaderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;
    private final ReaderRepository readerRepo;

    public LoanService(LoanRepository loanRepo, BookRepository bookRepo, ReaderRepository readerRepo) {
        this.loanRepo = loanRepo;
        this.bookRepo = bookRepo;
        this.readerRepo = readerRepo;
    }

    @Transactional
    public Loan borrowBook(Long bookId, Long readerId, int days) {
        Book book = bookRepo.findById(bookId).orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));
        if (!book.isAvailable()) throw new IllegalStateException("Book is not available");
        Reader reader = readerRepo.findById(readerId).orElseThrow(() -> new IllegalArgumentException("Reader not found: " + readerId));

        book.setAvailable(false);
        bookRepo.save(book);

        LocalDate loanDate = LocalDate.now();
        LocalDate due = loanDate.plusDays(days);

        Loan loan = new Loan(book, reader, loanDate, due);
        return loanRepo.save(loan);
    }

    @Transactional
    public Loan returnBook(Long loanId) {
        Loan loan = loanRepo.findById(loanId).orElseThrow(() -> new IllegalArgumentException("Loan not found: " + loanId));
        if (loan.getReturnDate() != null) throw new IllegalStateException("Book already returned");

        loan.setReturnDate(LocalDate.now());
        Book book = loan.getBook();
        book.setAvailable(true);
        bookRepo.save(book);
        return loanRepo.save(loan);
    }

    public List<Loan> listActiveLoans() {
        return loanRepo.findByReturnDateIsNull();
    }

    public List<Loan> listLoansForReader(Long readerId) {
        return loanRepo.findByReaderId(readerId);
    }

    public Optional<Loan> get(Long id) {
        return loanRepo.findById(id);
    }
}

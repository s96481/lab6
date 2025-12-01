package com.example.lab6.repository;

import com.example.lab6.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByReaderId(Long readerId);
    List<Loan> findByReturnDateIsNull();
}

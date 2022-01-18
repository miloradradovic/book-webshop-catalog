package com.example.catalogservice.repository;

import com.example.catalogservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findByNameAndRecap(String name, String recap);
    Book findById(int id);
}

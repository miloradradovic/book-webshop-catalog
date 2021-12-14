package com.example.catalogservice.service.impl;

import com.example.catalogservice.model.Book;
import com.example.catalogservice.repository.BookRepository;
import com.example.catalogservice.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService implements IBookService {

    @Autowired
    BookRepository bookRepository;

    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}

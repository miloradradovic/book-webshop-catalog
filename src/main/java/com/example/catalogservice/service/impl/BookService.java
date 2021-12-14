package com.example.catalogservice.service.impl;

import com.example.catalogservice.client.EditInStock;
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

    @Override
    public Book findById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public void editInStock(EditInStock editInStock) {
        Book found = findById(editInStock.getBookId());
        if (found == null) {
            // throw exception
        }
        found.setInStock(found.getInStock() - editInStock.getAmount());
        bookRepository.save(found);
    }
}

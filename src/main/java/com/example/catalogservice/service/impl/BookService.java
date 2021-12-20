package com.example.catalogservice.service.impl;

import com.example.catalogservice.exception.InStockFailException;
import com.example.catalogservice.feign.client.BookCatalogData;
import com.example.catalogservice.feign.client.CartClient;
import com.example.catalogservice.feign.client.EditInStock;
import com.example.catalogservice.exception.BookNotFoundException;
import com.example.catalogservice.model.Book;
import com.example.catalogservice.repository.BookRepository;
import com.example.catalogservice.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Transactional
    public void editInStock(EditInStock editInStock) {
        List<Book> updatedBooks = new ArrayList<>();
        editInStock.getAmounts().forEach((id, amount) -> {
            Book found = findById(id);
            if (found == null) {
                throw new BookNotFoundException();
            } else if (found.getInStock() - amount < 0) {
                throw new InStockFailException();
            }
            found.setInStock(found.getInStock() - amount);
            updatedBooks.add(found);
        });
        bookRepository.saveAll(updatedBooks);
    }

    @Override
    public List<BookCatalogData> getBookDataForCart(CartClient cart) {
        List<BookCatalogData> bookCatalogData = new ArrayList<>();

        for (Integer bookId : cart.getBookIds()) {
            Book found = findById(bookId);
            if (found == null) {
                throw new BookNotFoundException();
            }
            BookCatalogData bookData = new BookCatalogData(found.getName(), found.getPrice());
            bookCatalogData.add(bookData);
        }
        return bookCatalogData;
    }
}

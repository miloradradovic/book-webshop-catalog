package com.example.catalogservice.api;

import com.example.catalogservice.client.EditInStock;
import com.example.catalogservice.dto.BookDTO;
import com.example.catalogservice.mapper.BookMapper;
import com.example.catalogservice.model.Book;
import com.example.catalogservice.service.impl.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    BookMapper bookMapper;

    @GetMapping(value = "/all-books")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookService.findAll();
        return new ResponseEntity<>(bookMapper.toDTOList(books), HttpStatus.OK);
    }

    @GetMapping(value = "/client/get-by-id/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable int id) {
        Book book = bookService.findById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    // will be called after the order is placed
    @PutMapping(value = "/client/edit-in-stock")
    public ResponseEntity<?> editInStock(@RequestBody EditInStock editInStock) {
        bookService.editInStock(editInStock);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}

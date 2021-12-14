package com.example.catalogservice.api;

import com.example.catalogservice.dto.BookDTO;
import com.example.catalogservice.mapper.BookMapper;
import com.example.catalogservice.model.Book;
import com.example.catalogservice.service.impl.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    BookMapper bookMapper;

    @GetMapping(value = "/all-books")
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        List<Book> books = bookService.findAll();
        return new ResponseEntity<>(bookMapper.toDTOList(books), HttpStatus.OK);
    }
}

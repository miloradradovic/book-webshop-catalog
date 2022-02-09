package com.example.catalogservice.api;

import com.example.catalogservice.dto.BookDTO;
import com.example.catalogservice.dto.ModifyBookDTO;
import com.example.catalogservice.feign.client.*;
import com.example.catalogservice.mapper.BookMapper;
import com.example.catalogservice.model.Book;
import com.example.catalogservice.service.impl.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookController {

    @Autowired
    BookService bookService;

    @Autowired
    BookMapper bookMapper;

    // returns the whole catalog
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<List<BookDTO>> getAll() {
        List<Book> books = bookService.getAll();
        return new ResponseEntity<>(bookMapper.toBookDTOList(books), HttpStatus.OK);
    }

    @GetMapping(value = "/{bookId}")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_USER')")
    public ResponseEntity<BookDTO> getById(@PathVariable int bookId) {
        Book book = bookService.getByIdThrowsException(bookId);
        return new ResponseEntity<>(bookMapper.toBookDTO(book), HttpStatus.OK);
    }

    // will be called after the order is placed to reduce the in stock attribute
    @PutMapping(value = "/client/edit-in-stock")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> editInStock(@RequestBody @Valid EditInStockDTO editInStock) {
        bookService.editInStock(bookMapper.toEditInStock(editInStock));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // will be called when order is being placed to get detailed book data
    @PostMapping(value = "/client/get-by-cart")
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<BookCatalogDataDTO> getByCart(@RequestBody @Valid CartClientDTO cart) {
        List<BookCatalogData> bookCatalogData = bookService.getByCart(bookMapper.toCartClient(cart));
        return bookMapper.toBookCatalogDataDTOList(bookCatalogData);
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BookDTO> create(@RequestBody @Valid ModifyBookDTO bookDTO) {
        Book created = bookService.create(bookMapper.toBook(bookDTO), bookDTO.getWriterIds());
        return new ResponseEntity<>(bookMapper.toBookDTO(created), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{bookId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BookDTO> edit(@RequestBody @Valid ModifyBookDTO bookDTO, @PathVariable int bookId) {
        bookDTO.setId(bookId);
        Book edited = bookService.edit(bookMapper.toBook(bookDTO), bookDTO.getWriterIds());
        return new ResponseEntity<>(bookMapper.toBookDTO(edited), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{bookId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable int bookId) {
        bookService.delete(bookId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/by-name/{name}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String name) {
        bookService.delete(name);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

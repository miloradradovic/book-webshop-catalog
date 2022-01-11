package com.example.catalogservice.service.impl;

import com.example.catalogservice.exception.*;
import com.example.catalogservice.feign.client.BookCatalogData;
import com.example.catalogservice.feign.client.CartClient;
import com.example.catalogservice.feign.client.EditInStock;
import com.example.catalogservice.model.Book;
import com.example.catalogservice.model.ModifyBook;
import com.example.catalogservice.model.Writer;
import com.example.catalogservice.repository.BookRepository;
import com.example.catalogservice.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Handler;

@Service
public class BookService implements IBookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    WriterService writerService;

    @Override
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book getById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Book getByIdThrowsException(int id) {
        Book found = getById(id);
        if (found == null) {
            throw new BookNotFoundException();
        }
        return found;
    }

    @Override
    @Transactional
    public void editInStock(EditInStock editInStock) {
        List<Book> updatedBooks = new ArrayList<>();
        editInStock.getAmounts().forEach((id, amount) -> {
            Book found = getByIdThrowsException(id);
            if (found.getInStock() - amount < 0) {
                throw new InStockFailException();
            }
            found.setInStock(found.getInStock() - amount);
            updatedBooks.add(found);
        });
        bookRepository.saveAll(updatedBooks);
    }

    @Override
    public List<BookCatalogData> getByCart(CartClient cart) {
        List<BookCatalogData> bookCatalogData = new ArrayList<>();

        for (Integer bookId : cart.getBookIds()) {
            Book found = getByIdThrowsException(bookId);
            BookCatalogData bookData = new BookCatalogData(found.getName(), found.getPrice());
            bookCatalogData.add(bookData);
        }
        return bookCatalogData;
    }

    @Override
    public Book create(ModifyBook toCreate) {
        Book exists = bookRepository.findByNameAndRecap(toCreate.getName(), toCreate.getRecap());
        if (exists != null) {
            throw new BookAlreadyExistsException();
        }
        Set<Writer> writers = new HashSet<>();
        for (Integer writerId : toCreate.getWriterIds()) {
            Writer writer = writerService.getByIdThrowsException(writerId);
            writers.add(writer);
        }
        Book toCreateBook = new Book(toCreate.getName(), toCreate.getYearReleased(), toCreate.getRecap(), toCreate.getInStock(), toCreate.getPrice(),
                toCreate.getGenres(), writers);
        return bookRepository.save(toCreateBook);
    }

    @Override
    public Book edit(ModifyBook toEdit) {
        Book exists = bookRepository.findById(toEdit.getId()).orElse(null);
        if (exists == null) {
            throw new BookNotFoundException();
        }
        if (!exists.getName().equals(toEdit.getName()) && !exists.getRecap().equals(toEdit.getRecap())) {
            Book check = bookRepository.findByNameAndRecap(toEdit.getName(), toEdit.getRecap());
            if (check != null) {
                throw new BookAlreadyExistsException();
            }
        }
        Set<Writer> writers = new HashSet<>();
        for (Integer writerId : toEdit.getWriterIds()) {
            Writer writer = writerService.getById(writerId);
            writers.add(writer);
        }
        exists.setInStock(toEdit.getInStock());
        exists.setGenres(toEdit.getGenres());
        exists.setName(toEdit.getName());
        exists.setPrice(toEdit.getPrice());
        exists.setRecap(toEdit.getRecap());
        exists.setYearReleased(toEdit.getYearReleased());
        try {
            Set<Writer> writersOld = exists.getWriters();
            writerService.deleteWhereNoBooks(writersOld);
            exists.setWriters(writers);
        } catch (Exception e) {
            throw new DeleteBookFailException();
        }
        return bookRepository.save(exists);
    }

    @Override
    public void delete(int bookId) {
        Book toDelete = getByIdThrowsException(bookId);
        Set<Writer> writers = toDelete.getWriters(); // will be checked after book is deleted
        try {
            bookRepository.delete(toDelete);
            writerService.deleteWhereNoBooks(writers);
        } catch (Exception e) {
            throw new DeleteBookFailException();
        }
    }

    @Transactional
    @Override
    public void removeWriter(int writerId) {
        List<Book> books = getAll();
        for (Book book : books) {
            book.getWriters().removeIf(writer -> writer.getId() == writerId);
            if (book.getWriters().isEmpty()) {
                books.remove(book); // doing this because of saveAll
                delete(book.getId());
            }
        }
        bookRepository.saveAll(books);
    }
}

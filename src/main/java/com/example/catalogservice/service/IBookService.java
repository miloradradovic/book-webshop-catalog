package com.example.catalogservice.service;

import com.example.catalogservice.feign.client.BookCatalogData;
import com.example.catalogservice.feign.client.CartClient;
import com.example.catalogservice.feign.client.EditInStock;
import com.example.catalogservice.model.Book;
import com.example.catalogservice.model.ModifyBook;

import java.util.List;

public interface IBookService {

    List<Book> getAll();
    Book getById(int id);
    Book getByIdThrowsException(int id);
    void editInStock(EditInStock editInStock);
    List<BookCatalogData> getByCart(CartClient cart);
    Book create(ModifyBook toCreate);
    Book edit(ModifyBook toEdit);
    void delete(int bookId);
    void removeWriter(int writerId);
}

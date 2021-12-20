package com.example.catalogservice.service;

import com.example.catalogservice.feign.client.BookCatalogData;
import com.example.catalogservice.feign.client.CartClient;
import com.example.catalogservice.feign.client.EditInStock;
import com.example.catalogservice.model.Book;

import java.util.List;

public interface IBookService {

    List<Book> findAll();
    Book findById(int id);
    void editInStock(EditInStock editInStock);
    List<BookCatalogData> getBookDataForCart(CartClient cart);
}

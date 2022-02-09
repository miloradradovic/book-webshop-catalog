package com.example.catalogservice.exception;

import com.example.catalogservice.exception.general.BadRequestException;

public class BookNotFoundException extends BadRequestException {

    public BookNotFoundException() {
        super("Book not found!");
    }
}

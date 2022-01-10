package com.example.catalogservice.exception;

import com.example.catalogservice.exception.general.BadRequestException;

public class BookAlreadyExistsException extends BadRequestException {

    public BookAlreadyExistsException() {
        super("Book already exists!");
    }
}

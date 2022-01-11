package com.example.catalogservice.exception;

import com.example.catalogservice.exception.general.BadRequestException;

public class WriterAlreadyExistsException extends BadRequestException {

    public WriterAlreadyExistsException() {
        super("Writer already exists!");
    }
}

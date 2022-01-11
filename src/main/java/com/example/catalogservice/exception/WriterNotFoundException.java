package com.example.catalogservice.exception;

import com.example.catalogservice.exception.general.BadRequestException;

public class WriterNotFoundException extends BadRequestException {

    public WriterNotFoundException() {
        super("Writer not found!");
    }
}

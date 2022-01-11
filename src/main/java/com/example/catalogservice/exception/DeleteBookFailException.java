package com.example.catalogservice.exception;

import com.example.catalogservice.exception.general.BadRequestException;

public class DeleteBookFailException extends BadRequestException {

    public DeleteBookFailException() {
        super("Failed to delete book by id!");
    }
}

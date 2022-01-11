package com.example.catalogservice.exception;

import com.example.catalogservice.exception.general.BadRequestException;

public class DeleteWriterFailException extends BadRequestException {

    public DeleteWriterFailException() {
        super("Failed to delete writer!");
    }
}

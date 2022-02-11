package com.example.catalogservice.exception;

import com.example.catalogservice.exception.general.BadRequestException;

public class InStockFailException extends BadRequestException {

    public InStockFailException() {
        super("Failed to update in stock value for books!");
    }
}

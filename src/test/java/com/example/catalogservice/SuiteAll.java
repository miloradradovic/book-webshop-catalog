package com.example.catalogservice;

import com.example.catalogservice.api.BookControllerUnitTests;
import com.example.catalogservice.api.WriterControllerUnitTests;
import com.example.catalogservice.service.BookServiceUnitTests;
import com.example.catalogservice.service.WriterServiceUnitTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BookControllerUnitTests.class, WriterControllerUnitTests.class, WriterServiceUnitTests.class, BookServiceUnitTests.class
})
public class SuiteAll {
}

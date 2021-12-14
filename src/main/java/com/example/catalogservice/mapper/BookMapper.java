package com.example.catalogservice.mapper;

import com.example.catalogservice.dto.BookDTO;
import com.example.catalogservice.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class BookMapper {

    @Autowired
    GenreMapper genreMapper;

    @Autowired
    WriterMapper writerMapper;

    public BookDTO toDTO(Book book) {
        return new BookDTO(book.getId(), book.getName(), book.getYearReleased(), book.getRecap(), book.getInStock(),
                genreMapper.toStringList(book.getGenres()), writerMapper.toWriterDTOList(book.getWriters()));
    }

    public Book toBook(BookDTO bookDTO) {
        return null;
    }

    public List<BookDTO> toDTOList(List<Book> books) {
        List<BookDTO> bookDTOS = new ArrayList<>();
        for (Book book : books) {
            bookDTOS.add(toDTO(book));
        }
        return bookDTOS;
    }

    public Set<Book> toBookSet(List<Book> bookList) {
        return null;
    }
}

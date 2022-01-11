package com.example.catalogservice.mapper;

import com.example.catalogservice.dto.BookDTO;
import com.example.catalogservice.dto.ModifyBookDTO;
import com.example.catalogservice.feign.client.BookCatalogData;
import com.example.catalogservice.feign.client.BookCatalogDataDTO;
import com.example.catalogservice.model.Book;
import com.example.catalogservice.model.ModifyBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class BookMapper {

    @Autowired
    GenreMapper genreMapper;

    @Autowired
    WriterMapper writerMapper;

    public Book toBook(BookDTO bookDTO) {
        return new Book(bookDTO.getId(), bookDTO.getName(), bookDTO.getYearReleased(), bookDTO.getRecap(), bookDTO.getInStock(), bookDTO.getPrice(),
                genreMapper.toGenreSet(bookDTO.getGenres()), writerMapper.toWriterSet(bookDTO.getWriters()));
    }

    /*
    Never used, but might need it later
    public Set<Book> toBookSet(List<BookDTO> dtos) {
        Set<Book> books = new HashSet<>();
        for (BookDTO dto : dtos) {
            books.add(toBook(dto));
        }
        return books;
    }
     */

    public BookDTO toBookDTO(Book book) {
        return new BookDTO(book.getId(), book.getName(), book.getYearReleased(), book.getRecap(), book.getInStock(), book.getPrice(),
                genreMapper.toGenreStringList(book.getGenres()), writerMapper.toWriterDTOList(book.getWriters()));
    }

    public List<BookDTO> toBookDTOList(List<Book> books) {
        List<BookDTO> bookDTOS = new ArrayList<>();
        for (Book book : books) {
            bookDTOS.add(toBookDTO(book));
        }
        return bookDTOS;
    }

    public BookCatalogDataDTO toBookCatalogDataDTO(BookCatalogData bookCatalogData) {
        return new BookCatalogDataDTO(bookCatalogData.getName(), bookCatalogData.getPrice());
    }

    public List<BookCatalogDataDTO> toBookCatalogDataDTOList(List<BookCatalogData> bookCatalogData) {
        List<BookCatalogDataDTO> dtos = new ArrayList<>();
        for (BookCatalogData book : bookCatalogData) {
            dtos.add(toBookCatalogDataDTO(book));
        }
        return dtos;
    }

    public ModifyBook toModifyBook(ModifyBookDTO dto) {
        return new ModifyBook(dto.getId(), dto.getName(), dto.getYearReleased(), dto.getRecap(), dto.getInStock(), dto.getPrice(),
                genreMapper.toGenreSet(dto.getGenres()), dto.getWriterIds());
    }
}

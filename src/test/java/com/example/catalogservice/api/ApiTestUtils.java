package com.example.catalogservice.api;

import com.example.catalogservice.dto.BookDTO;
import com.example.catalogservice.dto.ModifyBookDTO;
import com.example.catalogservice.dto.ModifyWriterDTO;
import com.example.catalogservice.dto.WriterDTO;
import com.example.catalogservice.feign.client.*;
import com.example.catalogservice.model.*;
import com.example.catalogservice.security.UserDetailsImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

public class ApiTestUtils {

    public static int generateBookListSize() {
        return 2;
    }

    public static List<Book> generateBookList(int listSize) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            Book book = new Book();
            book.setId(i + 1);
            books.add(book);
        }
        return books;
    }

    public static List<BookDTO> generateBookDTOList(List<Book> books) {
        List<BookDTO> dtos = new ArrayList<>();
        for (Book book : books) {
            BookDTO bookDTO = new BookDTO();
            bookDTO.setId(book.getId());
            dtos.add(bookDTO);
        }
        return dtos;
    }

    public static int generateBookId(boolean success) {
        if (success) {
            return 1;
        }
        return -1;
    }

    public static Book generateBookFoundById(int bookId) {
        Book book = new Book();
        book.setId(bookId);
        return book;
    }

    public static BookDTO generateBookDTOFoundById(int bookId) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(bookId);
        return bookDTO;
    }

    public static CartClientDTO generateCartClientDTO(boolean success) {
        CartClientDTO cartClientDTO = new CartClientDTO();
        List<Integer> bookIds = new ArrayList<>();
        if (success) {
            bookIds.add(1);
            cartClientDTO.setBookIds(bookIds);
        } else {
            bookIds.add(-1);
            cartClientDTO.setBookIds(bookIds);
        }
        return cartClientDTO;
    }

    public static CartClient generateCartClient(CartClientDTO cartClientDTO) {
        return new CartClient(cartClientDTO.getBookIds());
    }

    public static List<BookCatalogData> generateBookCatalogData() {
        List<BookCatalogData> bookCatalogData = new ArrayList<>();
        BookCatalogData bookData = new BookCatalogData("Book1", 30.0);
        bookCatalogData.add(bookData);
        return bookCatalogData;
    }

    public static List<BookCatalogDataDTO> generateBookCatalogDataDTO(List<BookCatalogData> bookCatalogData) {
        List<BookCatalogDataDTO> dtos = new ArrayList<>();
        for (BookCatalogData book : bookCatalogData) {
            BookCatalogDataDTO dto = new BookCatalogDataDTO(book.getName(), book.getPrice());
            dtos.add(dto);
        }
        return dtos;
    }

    public static ModifyBookDTO generateModifyBookDTO(String type, String success) {
        if (type.equals("create")) {
            switch (success) {
                case "": {
                    List<String> genres = new ArrayList<>();
                    genres.add("SCIENCE");
                    List<Integer> writers = new ArrayList<>();
                    writers.add(1);
                    return new ModifyBookDTO("Bookkkk", 2020, "Recapppp", 15, 40.0, genres, writers);
                }
                case "nameandrecap": {
                    List<String> genres = new ArrayList<>();
                    genres.add("SCIENCE");
                    List<Integer> writers = new ArrayList<>();
                    writers.add(1);
                    return new ModifyBookDTO("Bookk", 2020, "Recapp", 15, 40.0, genres, writers);
                }
                default: { // writers
                    List<String> genres = new ArrayList<>();
                    genres.add("SCIENCE");
                    List<Integer> writers = new ArrayList<>();
                    writers.add(-1);
                    return new ModifyBookDTO("Bookkkk", 2020, "Recapppp", 15, 40.0, genres, writers);
                }
            }
        } else { // edit
            switch (success) {
                case "": {
                    List<String> genres = new ArrayList<>();
                    genres.add("SCIENCE");
                    List<Integer> writers = new ArrayList<>();
                    writers.add(1);
                    return new ModifyBookDTO(1, "Newname", 2020, "Newrecap", 15, 40.0, genres, writers);
                }
                case "id": {
                    List<String> genres = new ArrayList<>();
                    genres.add("SCIENCE");
                    List<Integer> writers = new ArrayList<>();
                    writers.add(1);
                    return new ModifyBookDTO(-1, "Newname", 2020, "Newrecap", 15, 40.0, genres, writers);
                }
                case "nameandrecap": {
                    List<String> genres = new ArrayList<>();
                    genres.add("SCIENCE");
                    List<Integer> writers = new ArrayList<>();
                    writers.add(1);
                    return new ModifyBookDTO(1, "Bookkk", 2020, "Recappp", 15, 40.0, genres, writers);
                }
                default: { // writers
                    List<String> genres = new ArrayList<>();
                    genres.add("SCIENCE");
                    List<Integer> writers = new ArrayList<>();
                    writers.add(-1);
                    return new ModifyBookDTO(1, "Newname", 2020, "Newrecap", 15, 40.0, genres, writers);
                }
            }
        }
    }

    public static Book generateBook(ModifyBookDTO modifyBookDTO, List<Integer> writerIds) {
        Set<Writer> writers = new HashSet<>();
        for (Integer writerId : writerIds) {
            Writer writer = new Writer();
            writer.setId(writerId);
            writers.add(writer);
        }
        Set<Genre> genres = new HashSet<>();
        for (String genre : modifyBookDTO.getGenres()) {
            genres.add(Genre.valueOf(genre));
        }
        return new Book(modifyBookDTO.getId(), modifyBookDTO.getName(), modifyBookDTO.getYearReleased(), modifyBookDTO.getRecap(),
                modifyBookDTO.getInStock(), modifyBookDTO.getPrice(), genres, writers);
    }

    public static Book generateCreatedBook(Book toCreate) {
        toCreate.setId(3);
        return toCreate;
    }

    public static BookDTO generateBookDTO(Book created) {
        List<WriterDTO> writers = new ArrayList<>();
        List<String> genres = new ArrayList<>();
        for(Writer writer : created.getWriters()) {
            WriterDTO dto = new WriterDTO();
            dto.setId(writer.getId());
            writers.add(dto);
        }
        for (Genre genre : created.getGenres()) {
            genres.add(genre.toString());
        }
        return new BookDTO(created.getId(), created.getName(), created.getYearReleased(), created.getRecap(), created.getInStock(),
                created.getPrice(), genres, writers);
    }

    public static Book generateEditedBook(Book toEdit) {
        return toEdit;
    }

    public static EditInStockDTO generateEditInStockDTO(String success) {
        Map<Integer, Integer> amounts = new HashMap<>();
        if (success.equals("")) {
            amounts.put(1, 1);
        } else if (success.equals("id")){
            amounts.put(-1, 1);
        } else { // amount
            amounts.put(1, 100);
        }
        return new EditInStockDTO(amounts);
    }

    public static EditInStock generateEditInStock(EditInStockDTO editInStockDTO) {
        return new EditInStock(editInStockDTO.getAmounts());
    }

    public static int generateWriterListSize() {
        return 2;
    }

    public static List<Writer> generateWriterList(int listSize) {
        List<Writer> writers = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            Writer writer = new Writer();
            writer.setId(i + 1);
            writers.add(writer);
        }
        return writers;
    }

    public static List<WriterDTO> generateWriterDTOList(List<Writer> writers) {
        List<WriterDTO> dtos = new ArrayList<>();
        for (Writer writer : writers) {
            WriterDTO dto = new WriterDTO();
            dto.setId(writer.getId());
            dtos.add(dto);
        }
        return dtos;
    }

    public static int generateWriterId(boolean success) {
        if (success) {
            return 1;
        }
        return -1;
    }

    public static Writer generateWriterFoundById(int writerId) {
        Writer writer = new Writer();
        writer.setId(writerId);
        return writer;
    }

    public static WriterDTO generateWriterDTOFoundById(int writerId) {
        WriterDTO dto = new WriterDTO();
        dto.setId(writerId);
        return dto;
    }

    public static ModifyWriterDTO generateModifyWriterDTO(String type, String success) {
        if (type.equals("create")) {
            if (success.equals("")) {
                return new ModifyWriterDTO("Newwriter", "Newwriter", "Biographynew");
            } else {
                return new ModifyWriterDTO("Name", "Surname", "Biography");
            }
        } else { // edit
            if (success.equals("")) {
                return new ModifyWriterDTO(1, "Nameee", "Surnameee", "Biographyyy");
            } else if (success.equals("id")) {
                return new ModifyWriterDTO(-1, "Nameee", "Surnameee", "Biographyyy");
            } else { // name surname biography
                return new ModifyWriterDTO(1, "Name", "Surname", "Biography");
            }
        }
    }

    public static Writer generateWriter(ModifyWriterDTO modifyWriterDTO) {
        return new Writer(modifyWriterDTO.getId(), modifyWriterDTO.getName(), modifyWriterDTO.getSurname(), modifyWriterDTO.getBiography());
    }

    public static Writer generateCreatedWriter(Writer toCreate) {
        toCreate.setId(3);
        return toCreate;
    }

    public static WriterDTO generateWriterDTO(Writer created) {
        return new WriterDTO(created.getId(), created.getName(), created.getSurname(), created.getBiography());
    }

    public static Writer generateEditedWriter(Writer toEdit) {
        return toEdit;
    }

    /*

    public static UserDetailsImpl generateUserDetailsRoleUser() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        return new UserDetailsImpl("email1@email.com", "", roles
                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
    }

    public static UserDetailsImpl generateUserDetailsRoleAdmin() {
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_ADMIN");
        return new UserDetailsImpl("admin@admin.com", "", roles
                .stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
    }

    public static Authentication generateAuthentication(UserDetailsImpl userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public static int generateBookListSize() {
        return 2;
    }

    public static List<Book> generateBookList(int listSize) {
        List<Book> books = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            Book book = new Book();
            book.setId(i + 1);
            books.add(book);
        }
        return books;
    }

    public static int generateValidBookId() {
        return 1;
    }

    public static int generateInvalidBookId() {
        return -1;
    }

    public static Book generateBookFoundById(int bookId) {
        Book book = new Book();
        book.setId(bookId);
        return book;
    }

    public static EditInStockDTO generateEditInStockDTOSuccess() {
        HashMap<Integer, Integer> amounts = new HashMap<>();
        amounts.put(1, 1);
        return new EditInStockDTO(amounts);
    }

    public static EditInStockDTO generateEditInStockDTOFail() {
        HashMap<Integer, Integer> amounts = new HashMap<>();
        amounts.put(-1, 1);
        return new EditInStockDTO(amounts);
    }

    public static CartClientDTO generateCartClientDTOSuccess() {
        CartClientDTO cartClientDTO = new CartClientDTO();
        List<Integer> bookIds = new ArrayList<>();
        bookIds.add(1);
        cartClientDTO.setBookIds(bookIds);
        return cartClientDTO;
    }

    public static List<BookCatalogData> generateBookCatalogData() {
        List<BookCatalogData> bookCatalogData = new ArrayList<>();
        BookCatalogData bookData = new BookCatalogData("Book1", 30.0);
        bookCatalogData.add(bookData);
        return bookCatalogData;
    }

    public static CartClientDTO generateCartClientDTOFail() {
        CartClientDTO cartClientDTO = new CartClientDTO();
        List<Integer> bookIds = new ArrayList<>();
        bookIds.add(-1);
        cartClientDTO.setBookIds(bookIds);
        return cartClientDTO;
    }

    public static ModifyBookDTO generateCreateBookDTOSuccess() {
        List<String> genres = new ArrayList<>();
        genres.add("SCIENCE");
        List<Integer> writers = new ArrayList<>();
        writers.add(1);
        return new ModifyBookDTO("Nameee", 2020, "Recap2", 15, 40.0, genres, writers);
    }

    public static ModifyBookDTO generateCreateBookDTOFailNameAndRecap() {
        List<String> genres = new ArrayList<>();
        genres.add("SCIENCE");
        List<Integer> writers = new ArrayList<>();
        writers.add(1);
        return new ModifyBookDTO("Bookk", 2020, "Recapp", 15, 40.0, genres, writers);
    }

    public static ModifyBookDTO generateCreateBookDTOFailWriters() {
        List<String> genres = new ArrayList<>();
        genres.add("SCIENCE");
        List<Integer> writers = new ArrayList<>();
        writers.add(-1);
        return new ModifyBookDTO("Nameee", 2020, "Recap2", 15, 40.0, genres, writers);
    }

    public static Book generateCreatedBook(Book book, List<Integer> writerIds) {
        Set<Writer> writers = new HashSet<>();
        for (Integer writerId : writerIds) {
            Writer writer = new Writer();
            writer.setId(writerId);
            writers.add(writer);
        }
        return new Book(2, book.getName(), book.getYearReleased(), book.getRecap(), book.getInStock(),
                book.getPrice(), book.getGenres(), writers);
    }

    public static ModifyBookDTO generateEditBookDTOSuccess() {
        List<String> genres = new ArrayList<>();
        genres.add("SCIENCE");
        List<Integer> writers = new ArrayList<>();
        writers.add(1);
        return new ModifyBookDTO("Nameee", 2020, "Recap2", 15, 40.0, genres, writers);
    }

    public static Book generateEditedBook(Book book, List<Integer> writerIds) {
        Set<Writer> writers = new HashSet<>();
        for (Integer writerId : writerIds) {
            Writer writer = new Writer();
            writer.setId(writerId);
            writers.add(writer);
        }
        return new Book(book.getId(), book.getName(), book.getYearReleased(), book.getRecap(), book.getInStock(),
                book.getPrice(), book.getGenres(), writers);
    }

    public static ModifyBookDTO generateEditBookDTOFailNameAndRecap() {
        List<String> genres = new ArrayList<>();
        genres.add("SCIENCE");
        List<Integer> writers = new ArrayList<>();
        writers.add(1);
        return new ModifyBookDTO("Bookkk", 2020, "Recappp", 15, 40.0, genres, writers);

    }

    public static ModifyBookDTO generateEditBookDTOFailWriters() {
        List<String> genres = new ArrayList<>();
        genres.add("SCIENCE");
        List<Integer> writers = new ArrayList<>();
        writers.add(-1);
        return new ModifyBookDTO("Nameee", 2020, "Recap2", 15, 40.0, genres, writers);
    }

    public static int generateWriterListSize() {
        return 2;
    }

    public static List<Writer> generateWriterList(int listSize) {
        List<Writer> writers = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            Writer writer = new Writer();
            writer.setId(i + 1);
            writers.add(writer);
        }
        return writers;
    }

    public static int generateValidWriterId() {
        return 1;
    }

    public static Writer generateWriterFoundById(int writerId) {
        Writer writer = new Writer();
        writer.setId(writerId);
        return writer;
    }

    public static int generateInvalidWriterId() {
        return -1;
    }

    public static ModifyWriterDTO generateCreateWriterDTOSuccess() {
        return new ModifyWriterDTO("Newwriter", "Newwriter", "Biographynew");
    }

    public static Writer generateCreatedWriter(Writer modifyWriter) {
        return new Writer(modifyWriter.getId(), modifyWriter.getName(), modifyWriter.getSurname(), modifyWriter.getBiography());
    }

    public static ModifyWriterDTO generateCreateWriterDTOFail() {
        return new ModifyWriterDTO("Name", "Surname", "Biography");
    }

    public static ModifyWriterDTO generateEditWriterDTOSuccess() {
        return new ModifyWriterDTO("Nameee", "Surnameee", "Biographyyy");
    }

    public static Writer generateEditedWriter(Writer modifyWriter) {
        return new Writer(modifyWriter.getId(), modifyWriter.getName(), modifyWriter.getSurname(), modifyWriter.getBiography());
    }

    public static ModifyWriterDTO generateEditWriterDTOFail() {
        return new ModifyWriterDTO("Namee", "Surnamee", "Biographyy");
    }

    public static Writer generateWriterToCreate(ModifyWriterDTO modifyWriter) {
        return new Writer(modifyWriter.getId(), modifyWriter.getName(), modifyWriter.getSurname(), modifyWriter.getBiography());
    }

    public static Writer generateWriterToEdit(ModifyWriterDTO modifyWriterDTO) {
        return new Writer(modifyWriterDTO.getId(), modifyWriterDTO.getName(), modifyWriterDTO.getSurname(), modifyWriterDTO.getBiography());
    }

    public static Book generateBookToCreate(ModifyBookDTO modifyBookDTO) {
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.valueOf(modifyBookDTO.getGenres().get(0)));
        return new Book(modifyBookDTO.getName(), modifyBookDTO.getYearReleased(), modifyBookDTO.getRecap(), modifyBookDTO.getInStock(),
                modifyBookDTO.getPrice(), genres);
    }

    public static Book generateBookToEdit(ModifyBookDTO modifyBookDTO) {
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.valueOf(modifyBookDTO.getGenres().get(0)));
        return new Book(modifyBookDTO.getId(), modifyBookDTO.getName(), modifyBookDTO.getYearReleased(), modifyBookDTO.getRecap(), modifyBookDTO.getInStock(),
                modifyBookDTO.getPrice(), genres);
    }

     */
}

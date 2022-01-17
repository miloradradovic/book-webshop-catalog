package com.example.catalogservice.api;

import com.example.catalogservice.dto.ModifyBookDTO;
import com.example.catalogservice.dto.ModifyWriterDTO;
import com.example.catalogservice.feign.client.BookCatalogData;
import com.example.catalogservice.feign.client.CartClientDTO;
import com.example.catalogservice.feign.client.EditInStockDTO;
import com.example.catalogservice.model.*;
import com.example.catalogservice.security.UserDetailsImpl;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public class ApiTestUtils {

    public static String json(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return mapper.writeValueAsString(obj);
    }


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

    public static Book generateBookFoundById(int bookId) {
        Book book = new Book();
        book.setId(bookId);
        return book;
    }

    public static int generateInvalidBookId() {
        return -1;
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

    public static ModifyBook generateModifyBook(ModifyBookDTO modifyBookDTO) {
        Set<Genre> genres = new HashSet<>();
        for (String genre : modifyBookDTO.getGenres()) {
            genres.add(Genre.valueOf(genre));
        }
        return new ModifyBook(modifyBookDTO.getId(), modifyBookDTO.getName(), modifyBookDTO.getYearReleased(), modifyBookDTO.getRecap(),
                modifyBookDTO.getInStock(), modifyBookDTO.getPrice(), genres, modifyBookDTO.getWriterIds());
    }

    public static Book generateCreatedBook(ModifyBook modifyBook) {
        Set<Writer> writers = new HashSet<>();
        for (Integer writerId : modifyBook.getWriterIds()) {
            Writer writer = new Writer();
            writer.setId(writerId);
            writers.add(writer);
        }
        return new Book(2, modifyBook.getName(), modifyBook.getYearReleased(), modifyBook.getRecap(), modifyBook.getInStock(),
                modifyBook.getPrice(), modifyBook.getGenres(), writers);
    }

    public static ModifyBookDTO generateEditBookDTOSuccess() {
        List<String> genres = new ArrayList<>();
        genres.add("SCIENCE");
        List<Integer> writers = new ArrayList<>();
        writers.add(1);
        return new ModifyBookDTO("Nameee", 2020, "Recap2", 15, 40.0, genres, writers);
    }

    public static Book generateEditedBook(ModifyBook modifyBook) {
        Set<Writer> writers = new HashSet<>();
        for (Integer writerId : modifyBook.getWriterIds()) {
            Writer writer = new Writer();
            writer.setId(writerId);
            writers.add(writer);
        }
        return new Book(modifyBook.getId(), modifyBook.getName(), modifyBook.getYearReleased(), modifyBook.getRecap(), modifyBook.getInStock(),
                modifyBook.getPrice(), modifyBook.getGenres(), writers);
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

    public static ModifyWriter generateModifyWriter(ModifyWriterDTO modifyWriterDTO) {
        return new ModifyWriter(modifyWriterDTO.getId(), modifyWriterDTO.getName(), modifyWriterDTO.getSurname(), modifyWriterDTO.getBiography());
    }

    public static Writer generateCreatedWriter(ModifyWriter modifyWriter) {
        return new Writer(modifyWriter.getId(), modifyWriter.getName(), modifyWriter.getSurname(), modifyWriter.getBiography());
    }

    public static ModifyWriterDTO generateCreateWriterDTOFail() {
        return new ModifyWriterDTO("Name", "Surname", "Biography");
    }

    public static ModifyWriterDTO generateEditWriterDTOSuccess() {
        return new ModifyWriterDTO("Nameee", "Surnameee", "Biographyyy");
    }

    public static Writer generateEditedWriter(ModifyWriter modifyWriter) {
        return new Writer(modifyWriter.getId(), modifyWriter.getName(), modifyWriter.getSurname(), modifyWriter.getBiography());
    }

    public static ModifyWriterDTO generateEditWriterDTOFail() {
        return new ModifyWriterDTO("Namee", "Surnamee", "Biographyy");
    }
}

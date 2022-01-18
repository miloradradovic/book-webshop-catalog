package com.example.catalogservice.service;

import com.example.catalogservice.feign.client.CartClient;
import com.example.catalogservice.feign.client.EditInStock;
import com.example.catalogservice.model.Book;
import com.example.catalogservice.model.Genre;
import com.example.catalogservice.model.Writer;

import java.util.*;

public class ServiceTestUtils {

    public static int generateValidWriterId() {
        return 1;
    }

    public static Writer generateWriterFoundById(int writerId) {
        Writer writer = new Writer();
        writer.setId(writerId);
        writer.setName("Name");
        writer.setSurname("Surname");
        writer.setBiography("Biography");
        return writer;
    }

    public static int generateInvalidWriterId() {
        return -1;
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

    public static Writer generateWriterToCreateSuccess() {
        return new Writer(0, "Nameee", "Surnameee", "Biographyyy");
    }

    public static Writer generateCreatedWriter(Writer toCreate) {
        toCreate.setId(3);
        return toCreate;
    }

    public static Writer generateWriterToCreateFail() {
        return new Writer(0, "Name", "Surname", "Biography");
    }

    public static Writer generateWriterFoundByNameAndSurnameAndBiography(String name, String surname, String biography) {
        Writer writer = new Writer();
        writer.setName(name);
        writer.setSurname(surname);
        writer.setBiography(biography);
        return writer;
    }

    public static Writer generateWriterToEditSuccess() {
        return new Writer(1, "Namenew", "Surnamenew", "Biographynew");
    }

    public static Writer generateEditedWriter(Writer toEdit) {
        return new Writer(toEdit.getId(), toEdit.getName(), toEdit.getSurname(), toEdit.getBiography());
    }

    public static Writer generateWriterToEditFailWriterData() {
        return new Writer(1, "Namee", "Surnamee", "Biographyy");
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

    public static Book generateBookFoundById(int bookId) {
        Book book = new Book();
        book.setId(bookId);
        book.setInStock(3);
        book.setName("Bookk");
        book.setRecap("Recapp");
        return book;
    }

    public static int generateValidBookId() {
        return 1;
    }

    public static int generateInvalidBookId() {
        return -1;
    }

    public static EditInStock generateEditInStockFailId() {
        HashMap<Integer, Integer> amounts = new HashMap<>();
        amounts.put(-1, 2);
        return new EditInStock(amounts);
    }


    public static EditInStock generateEditInStockFailAmount() {
        HashMap<Integer, Integer> amounts = new HashMap<>();
        amounts.put(1, 99);
        return new EditInStock(amounts);
    }

    public static CartClient generateCartClient() {
        List<Integer> bookIds = new ArrayList<>();
        bookIds.add(1);
        return new CartClient(bookIds);
    }

    public static Book generateBookToCreateSuccess() {
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.SCIENCE);
        return new Book("Newbook", 2020, "Newrecap", 6, 50.0, genres);
    }

    public static List<Integer> generateWriterIdListSuccess(int listSize) {
        List<Integer> writerIds = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            writerIds.add(i + 1);
        }
        return writerIds;
    }

    public static List<Integer> generateWriterIdListFail(int listSize) {
        List<Integer> writerIds = new ArrayList<>();
        for (int i = 0; i < listSize; i++) {
            writerIds.add(i - 100);
        }
        return writerIds;
    }

    public static Book generateCreatedBook(Book toCreate) {
        toCreate.setId(3);
        return toCreate;
    }

    public static Book generateBookToCreateFail() {
        Book book = new Book();
        book.setRecap("Recapp");
        book.setName("Bookk");
        return book;
    }

    public static Book generateBookFoundByNameAndRecap(String name, String recap) {
        Book book = new Book();
        book.setName(name);
        book.setRecap(recap);
        return book;
    }

    public static Book generateBookToEditSuccess() {
        Book book = new Book();
        book.setId(1);
        book.setName("Newname");
        book.setRecap("Newrecap");
        book.setPrice(30.0);
        book.setYearReleased(2000);
        book.setInStock(6);
        Set<Genre> genres = new HashSet<>();
        genres.add(Genre.COMEDY);
        book.setGenres(genres);
        return book;
    }

    public static Book generateEditedBook(Book toEdit) {
        return toEdit;
    }
}

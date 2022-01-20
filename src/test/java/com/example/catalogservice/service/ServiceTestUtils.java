package com.example.catalogservice.service;

import com.example.catalogservice.feign.client.CartClient;
import com.example.catalogservice.feign.client.EditInStock;
import com.example.catalogservice.model.Book;
import com.example.catalogservice.model.Genre;
import com.example.catalogservice.model.Writer;

import java.util.*;

public class ServiceTestUtils {

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

    public static int generateBookId(boolean success) {
        if (success) {
            return 1;
        }
        return -1;
    }

    public static Book generateBookFoundBy(int bookId, String name, String recap) {
        Book book = new Book();
        if (bookId != 0) {
            book.setId(bookId);
            book.setInStock(3);
            book.setName("Bookk");
            book.setRecap("Recapp");
            book.setPrice(30.0);
        } else {
            book.setName(name);
            book.setRecap(recap);
        }
        return book;
    }

    public static EditInStock generateEditInStock(String success) {
        Map<Integer, Integer> amounts = new HashMap<>();
        if (success.equals("")) {
            amounts.put(1, 1);
        } else if (success.equals("id")) {
            amounts.put(-1, 1);
        } else { // amount
            amounts.put(1, 100);
        }
        return new EditInStock(amounts);
    }

    public static CartClient generateCartClient(boolean success) {
        List<Integer> bookIds = new ArrayList<>();
        if (success) {
            bookIds.add(1);
        } else {
            bookIds.add(-1);
        }
        return new CartClient(bookIds);
    }

    public static Book generateBook(String type, String success) {
        if (type.equals("create")) {
            if (success.equals("")) {
                Set<Genre> genres = new HashSet<>();
                genres.add(Genre.SCIENCE);
                return new Book("Newbook", 2020, "Newrecap", 6, 50.0, genres);
            } else { // name and recap
                Set<Genre> genres = new HashSet<>();
                genres.add(Genre.SCIENCE);
                return new Book("Bookk", 2020, "Recapp", 6, 50.0, genres);
            }
        } else { // edit
            if (success.equals("")) {
                Set<Genre> genres = new HashSet<>();
                genres.add(Genre.SCIENCE);
                return new Book(1, "Newbook", 2020, "Newrecap", 6, 50.0, genres);
            } else if (success.equals("id")) {
                Set<Genre> genres = new HashSet<>();
                genres.add(Genre.SCIENCE);
                return new Book(-1, "Newbook", 2020, "Newrecap", 6, 50.0, genres);
            } else { // nameandrecap
                Set<Genre> genres = new HashSet<>();
                genres.add(Genre.SCIENCE);
                return new Book(1, "Bookkk", 2020, "Recappp", 6, 50.0, genres);
            }
        }
    }

    public static List<Integer> generateWriterIdList(boolean success) {
        List<Integer> writers = new ArrayList<>();
        if (success) {
            writers.add(1);
        } else {
            writers.add(-1);
        }
        return writers;
    }

    public static Writer generateWriterFoundBy(Integer id, String name, String surname, String biography) {
        Writer writer = new Writer();
        if (id != 0) {
            writer.setId(id);
            writer.setName("Name");
            writer.setSurname("Surname");
            writer.setBiography("Biography");
        } else {
            writer.setName(name);
            writer.setSurname(surname);
            writer.setBiography(biography);
        }
        return writer;
    }

    public static Book generateCreatedBook(Book toCreate) {
        toCreate.setId(3);
        return toCreate;
    }

    public static Book generateEditedBook(Book foundBook, Book toEdit) {
        return new Book(foundBook.getId(), toEdit.getName(), toEdit.getYearReleased(), toEdit.getRecap(), toEdit.getInStock(),
                toEdit.getPrice(), toEdit.getGenres());
    }

    public static int generateWriterId(boolean success) {
        if (success) {
            return 1;
        }
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

    public static Writer generateWriter(String type, String success) {
        if (type.equals("create")) {
            if (success.equals("")) {
                return new Writer("Newname", "Newsurname", "Newbiography");
            } else { // writer data
                return new Writer("Name", "Surname", "Biography");
            }
        } else { // edit
            if (success.equals("")) {
                return new Writer(1, "Newname", "Newsurname", "Newbiography");
            } else if (success.equals("id")) {
                return new Writer(-1, "Newname", "Newsurname", "Newbiography");
            } else { // writer data
                return new Writer(1, "Namee", "Surnamee", "Biographyy");
            }
        }
    }

    public static Writer generateCreatedWriter(Writer toCreate) {
        toCreate.setId(3);
        return toCreate;
    }

    public static Writer generateEditedWriter(Writer foundById, Writer toEdit) {
        return new Writer(foundById.getId(), toEdit.getName(), toEdit.getSurname(), toEdit.getBiography());
    }
}

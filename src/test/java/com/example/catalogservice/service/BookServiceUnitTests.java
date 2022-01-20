package com.example.catalogservice.service;

import com.example.catalogservice.exception.BookAlreadyExistsException;
import com.example.catalogservice.exception.BookNotFoundException;
import com.example.catalogservice.exception.InStockFailException;
import com.example.catalogservice.exception.WriterNotFoundException;
import com.example.catalogservice.feign.client.BookCatalogData;
import com.example.catalogservice.feign.client.CartClient;
import com.example.catalogservice.feign.client.EditInStock;
import com.example.catalogservice.model.Book;
import com.example.catalogservice.model.Writer;
import com.example.catalogservice.repository.BookRepository;
import com.example.catalogservice.service.impl.BookService;
import com.example.catalogservice.service.impl.WriterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookServiceUnitTests {

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private WriterService writerService;

    @Test
    public void getAllSuccess() {
        int listSize = ServiceTestUtils.generateBookListSize();
        List<Book> books = ServiceTestUtils.generateBookList(listSize);

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getAll();
        verify(bookRepository).findAll();
        verifyNoMoreInteractions(bookRepository);
        assertEquals(listSize, result.size());
    }

    @Test
    public void getByIdSuccess() {
        int bookId = ServiceTestUtils.generateBookId(true);
        Book found = ServiceTestUtils.generateBookFoundBy(bookId, "", "");

        when(bookRepository.findById(bookId)).thenReturn(found);

        Book result = bookService.getById(bookId);
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
        assertEquals(bookId, result.getId());
    }

    @Test
    public void getByIdReturnsNull() {
        int bookId = ServiceTestUtils.generateBookId(false);

        when(bookRepository.findById(bookId)).thenReturn(null);

        Book result = bookService.getById(bookId);
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
        assertNull(result);
    }

    @Test
    public void getByIdThrowsExceptionSuccess() {
        int bookId = ServiceTestUtils.generateBookId(true);
        Book foundById = ServiceTestUtils.generateBookFoundBy(bookId, "", "");

        when(bookRepository.findById(bookId)).thenReturn(foundById);

        Book result = bookService.getByIdThrowsException(bookId);
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
        assertEquals(bookId, result.getId());
    }

    @Test(expected = BookNotFoundException.class)
    public void getByIdThrowsExceptionFail() {
        int bookId = ServiceTestUtils.generateBookId(false);

        when(bookRepository.findById(bookId)).thenReturn(null);

        bookService.getByIdThrowsException(bookId);
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void editInStockSuccess() {
        EditInStock editInStock = ServiceTestUtils.generateEditInStock("");
        int bookId = ServiceTestUtils.generateBookId(true);
        Book found = ServiceTestUtils.generateBookFoundBy(bookId, "", "");

        when(bookRepository.findById(bookId)).thenReturn(found);

        boolean result = bookService.editInStock(editInStock);
        verify(bookRepository).findById(bookId);
        assertTrue(result);
    }

    @Test(expected = BookNotFoundException.class)
    public void editInStockFailId() {
        EditInStock editInStock = ServiceTestUtils.generateEditInStock("id");
        int bookId = ServiceTestUtils.generateBookId(false);

        when(bookRepository.findById(bookId)).thenReturn(null);

        bookService.editInStock(editInStock);
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test(expected = InStockFailException.class)
    public void editInStockFailAmount() {
        int bookId = ServiceTestUtils.generateBookId(true);
        Book found = ServiceTestUtils.generateBookFoundBy(bookId, "", "");
        EditInStock editInStock = ServiceTestUtils.generateEditInStock("amount");

        when(bookRepository.findById(bookId)).thenReturn(found);

        bookService.editInStock(editInStock);
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void getByCartSuccess() {
        int bookId = ServiceTestUtils.generateBookId(true);
        Book found = ServiceTestUtils.generateBookFoundBy(bookId, "", "");
        CartClient cartClient = ServiceTestUtils.generateCartClient(true);

        when(bookRepository.findById(bookId)).thenReturn(found);

        List<BookCatalogData> result = bookService.getByCart(cartClient);
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
        assertEquals(found.getName(), result.get(0).getName());
        assertEquals(found.getPrice(), result.get(0).getPrice(), 0);
    }

    @Test(expected = BookNotFoundException.class)
    public void getByCartFail() {
        int bookId = ServiceTestUtils.generateBookId(false);
        CartClient cartClient = ServiceTestUtils.generateCartClient(false);

        when(bookRepository.findById(bookId)).thenReturn(null);

        bookService.getByCart(cartClient);
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void createSuccess() {
        Book toCreate = ServiceTestUtils.generateBook("create", "");
        List<Integer> writerIds = ServiceTestUtils.generateWriterIdList(true);
        Writer found = ServiceTestUtils.generateWriterFoundBy(writerIds.get(0), "", "", "");
        Book created = ServiceTestUtils.generateCreatedBook(toCreate);

        when(bookRepository.findByNameAndRecap(toCreate.getName(), toCreate.getRecap())).thenReturn(null);
        when(writerService.getByIdThrowsException(writerIds.get(0))).thenReturn(found);
        when(bookRepository.save(toCreate)).thenReturn(created);

        Book result = bookService.create(toCreate, writerIds);
        verify(bookRepository).findByNameAndRecap(toCreate.getName(), toCreate.getRecap());
        verify(bookRepository).save(toCreate);
        verify(writerService).getByIdThrowsException(writerIds.get(0));
        verifyNoMoreInteractions(bookRepository, writerService);
        assertEquals(created.getId(), result.getId());
    }

    @Test(expected = BookAlreadyExistsException.class)
    public void createFailBookData() {
        Book toCreate = ServiceTestUtils.generateBook("create", "namerecap");
        Book found = ServiceTestUtils.generateBookFoundBy(0, toCreate.getName(), toCreate.getRecap());

        when(bookRepository.findByNameAndRecap(toCreate.getName(), toCreate.getRecap())).thenReturn(found);

        bookService.create(toCreate, new ArrayList<>());
        verify(bookRepository).findByNameAndRecap(toCreate.getName(), toCreate.getRecap());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test(expected = WriterNotFoundException.class)
    public void createFailWriter() {
        Book toCreate = ServiceTestUtils.generateBook("create", "");
        List<Integer> writerIds = ServiceTestUtils.generateWriterIdList(false);

        when(bookRepository.findByNameAndRecap(toCreate.getName(), toCreate.getRecap())).thenReturn(null);
        when(writerService.getByIdThrowsException(writerIds.get(0))).thenThrow(WriterNotFoundException.class);

        bookService.create(toCreate, writerIds);
        verify(bookRepository).findByNameAndRecap(toCreate.getName(), toCreate.getRecap());
        verify(writerService).getByIdThrowsException(writerIds.get(0));
        verifyNoMoreInteractions(bookRepository, writerService);
    }

    @Test
    public void editSuccess() {
        Book toEdit = ServiceTestUtils.generateBook("edit", "");
        List<Integer> writerIds = ServiceTestUtils.generateWriterIdList(true);
        Book foundBook = ServiceTestUtils.generateBookFoundBy(toEdit.getId(), "", "");
        Writer foundWriter = ServiceTestUtils.generateWriterFoundBy(writerIds.get(0), "", "", "");
        Book edited = ServiceTestUtils.generateEditedBook(foundBook, toEdit);

        when(bookRepository.findById(toEdit.getId())).thenReturn(foundBook);
        when(bookRepository.findByNameAndRecap(toEdit.getName(), toEdit.getRecap())).thenReturn(null);
        when(writerService.getByIdThrowsException(writerIds.get(0))).thenReturn(foundWriter);
        when(bookRepository.save(foundBook)).thenReturn(edited);

        Book result = bookService.edit(toEdit, writerIds);
        verify(bookRepository).findById(toEdit.getId());
        verify(bookRepository).findByNameAndRecap(toEdit.getName(), toEdit.getRecap());
        verify(bookRepository).save(foundBook);
        verify(writerService).getByIdThrowsException(writerIds.get(0));
        verifyNoMoreInteractions(bookRepository);
        assertEquals(edited.getId(), result.getId());
        assertEquals(edited.getName(), result.getName());
        assertEquals(edited.getYearReleased(), result.getYearReleased());
        assertEquals(edited.getInStock(), result.getInStock());
        assertEquals(edited.getPrice(), result.getPrice(), 0);
        assertEquals(edited.getRecap(), result.getRecap());
    }

    @Test(expected = BookNotFoundException.class)
    public void editFailId() {
        Book toEdit = ServiceTestUtils.generateBook("edit", "id");

        when(bookRepository.findById(toEdit.getId())).thenReturn(null);

        bookService.edit(toEdit, new ArrayList<>());
        verify(bookRepository).findById(toEdit.getId());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test(expected = WriterNotFoundException.class)
    public void editFailWriters() {
        Book toEdit = ServiceTestUtils.generateBook("edit", "writers");
        List<Integer> writerIds = ServiceTestUtils.generateWriterIdList(false);
        Book foundBook = ServiceTestUtils.generateBookFoundBy(toEdit.getId(), "", "");

        when(bookRepository.findById(toEdit.getId())).thenReturn(foundBook);
        when(bookRepository.findByNameAndRecap(toEdit.getName(), toEdit.getRecap())).thenReturn(null);
        when(writerService.getByIdThrowsException(writerIds.get(0))).thenThrow(WriterNotFoundException.class);

        bookService.edit(toEdit, writerIds);
        verify(bookRepository).findById(toEdit.getId());
        verify(bookRepository).findByNameAndRecap(toEdit.getName(), toEdit.getRecap());
        verify(writerService).getByIdThrowsException(writerIds.get(0));
        verifyNoMoreInteractions(bookRepository, writerService);
    }

    @Test(expected = BookAlreadyExistsException.class)
    public void editFailBookData() {
        Book toEdit = ServiceTestUtils.generateBook("edit", "nameandrecap");
        Book foundById = ServiceTestUtils.generateBookFoundBy(toEdit.getId(), "", "");
        Book foundByData = ServiceTestUtils.generateBookFoundBy(0, toEdit.getName(), toEdit.getRecap());

        when(bookRepository.findById(toEdit.getId())).thenReturn(foundById);
        when(bookRepository.findByNameAndRecap(toEdit.getName(), toEdit.getRecap())).thenReturn(foundByData);

        bookService.edit(toEdit, new ArrayList<>());
        verify(bookRepository).findById(toEdit.getId());
        verify(bookRepository).findByNameAndRecap(toEdit.getName(), toEdit.getRecap());
        verifyNoMoreInteractions(bookRepository);
    }

    @Test
    public void deleteSuccess() {
        int bookId = ServiceTestUtils.generateBookId(true);
        Book found = ServiceTestUtils.generateBookFoundBy(bookId, "", "");

        when(bookRepository.findById(bookId)).thenReturn(found);

        boolean result = bookService.delete(bookId);
        verify(bookRepository).findById(bookId);
        verify(bookRepository).delete(found);
        verifyNoMoreInteractions(bookRepository);
        assertTrue(result);
    }

    @Test(expected = BookNotFoundException.class)
    public void deleteFail() {
        int bookId = ServiceTestUtils.generateBookId(false);

        when(bookRepository.findById(bookId)).thenReturn(null);

        bookService.delete(bookId);
        verify(bookRepository).findById(bookId);
        verifyNoMoreInteractions(bookRepository);
    }
}

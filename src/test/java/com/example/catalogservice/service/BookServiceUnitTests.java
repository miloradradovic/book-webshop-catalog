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
import com.example.catalogservice.repository.WriterRepository;
import com.example.catalogservice.service.impl.BookService;
import com.example.catalogservice.service.impl.WriterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookServiceUnitTests {

    private MockMvc mockMvc;

    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private WriterService writerService;

    @Mock
    private WriterRepository writerRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    public void setup() {
        this.mockMvc = MockMvcBuilders.
                webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getAllSuccess() {
        int listSize = ServiceTestUtils.generateBookListSize();
        List<Book> books = ServiceTestUtils.generateBookList(listSize);

        given(bookService.getAll()).willReturn(books);

        List<Book> result = bookService.getAll();
        assertEquals(listSize, result.size());
    }

    @Test
    public void getByIdSuccess() {
        int bookId = ServiceTestUtils.generateValidBookId();
        Book found = ServiceTestUtils.generateBookFoundById(bookId);

        given(bookRepository.findById(bookId)).willReturn(found);

        Book result = bookService.getById(bookId);
        assertEquals(bookId, result.getId());
    }

    @Test
    public void getByIdReturnsNull() {
        int bookId = ServiceTestUtils.generateInvalidBookId();

        given(bookRepository.findById(bookId)).willReturn(null);

        Book result = bookService.getById(bookId);
        assertNull(result);
    }

    @Test
    public void getByIdThrowsExceptionSuccess() {
        int bookId = ServiceTestUtils.generateValidBookId();
        Book foundById = ServiceTestUtils.generateBookFoundById(bookId);

        given(bookRepository.findById(bookId)).willReturn(foundById);
        given(bookService.getById(bookId)).willReturn(foundById);

        Book result = bookService.getByIdThrowsException(bookId);
        assertEquals(bookId, result.getId());
    }

    @Test(expected = BookNotFoundException.class)
    public void getByIdThrowsExceptionFail() {
        int bookId = ServiceTestUtils.generateInvalidBookId();

        given(bookRepository.findById(bookId)).willReturn(null);
        given(bookService.getById(bookId)).willReturn(null);

        bookService.getByIdThrowsException(bookId);
    }

    @Test(expected = BookNotFoundException.class)
    public void editInStockFailId() {
        EditInStock editInStock = ServiceTestUtils.generateEditInStockFailId();
        int bookId = ServiceTestUtils.generateInvalidBookId();

        given(bookRepository.findById(bookId)).willReturn(null);
        given(bookService.getById(bookId)).willReturn(null);
        given(bookService.getByIdThrowsException(bookId)).willThrow(BookNotFoundException.class);

        bookService.editInStock(editInStock);
    }

    @Test(expected = InStockFailException.class)
    public void editInStockFailAmount() {
        int bookId = ServiceTestUtils.generateValidBookId();
        Book found = ServiceTestUtils.generateBookFoundById(bookId);
        EditInStock editInStock = ServiceTestUtils.generateEditInStockFailAmount();

        given(bookRepository.findById(bookId)).willReturn(found);
        given(bookService.getById(bookId)).willReturn(found);
        given(bookService.getByIdThrowsException(bookId)).willReturn(found);

        bookService.editInStock(editInStock);
    }

    @Test
    public void getByCartSuccess() {
        int bookId = ServiceTestUtils.generateValidBookId();
        Book found = ServiceTestUtils.generateBookFoundById(bookId);
        CartClient cartClient = ServiceTestUtils.generateCartClient();

        given(bookRepository.findById(bookId)).willReturn(found);
        given(bookService.getById(bookId)).willReturn(found);
        given(bookService.getByIdThrowsException(bookId)).willReturn(found);

        List<BookCatalogData> bookCatalogData = bookService.getByCart(cartClient);
        assertEquals(cartClient.getBookIds().size(), bookCatalogData.size());
    }

    @Test(expected = BookNotFoundException.class)
    public void getByCartFail() {
        int bookId = ServiceTestUtils.generateInvalidBookId();
        CartClient cartClient = ServiceTestUtils.generateCartClient();

        given(bookRepository.findById(bookId)).willReturn(null);
        given(bookService.getById(bookId)).willReturn(null);
        given(bookService.getByIdThrowsException(bookId)).willThrow(BookNotFoundException.class);

        bookService.getByCart(cartClient);
    }

    @Test
    @Transactional
    public void createSuccess() {
        int listSize = ServiceTestUtils.generateBookListSize();
        Book toCreate = ServiceTestUtils.generateBookToCreateSuccess();
        List<Integer> writerIds = ServiceTestUtils.generateWriterIdListSuccess(1);
        Writer found = ServiceTestUtils.generateWriterFoundById(writerIds.get(0));
        Book created = ServiceTestUtils.generateCreatedBook(toCreate);

        given(bookRepository.findByNameAndRecap(toCreate.getName(), toCreate.getRecap())).willReturn(null);
        given(writerRepository.findById((int)writerIds.get(0))).willReturn(found);
        given(writerService.getById(writerIds.get(0))).willReturn(found);
        given(writerService.getByIdThrowsException(writerIds.get(0))).willReturn(found);
        given(bookRepository.save(toCreate)).willReturn(created);

        Book result = bookService.create(toCreate, writerIds);
        assertTrue(result.getId() > listSize);
    }

    @Test(expected = BookAlreadyExistsException.class)
    public void createFailBookData() {
        Book toCreate = ServiceTestUtils.generateBookToCreateFail();
        Book found = ServiceTestUtils.generateBookFoundByNameAndRecap(toCreate.getName(), toCreate.getRecap());

        given(bookRepository.findByNameAndRecap(toCreate.getName(), toCreate.getRecap())).willReturn(found);

        bookService.create(toCreate, null);
    }

    @Test(expected = WriterNotFoundException.class)
    public void createFailWriter() {
        Book toCreate = ServiceTestUtils.generateBookToCreateSuccess();
        List<Integer> writerIds = ServiceTestUtils.generateWriterIdListFail(1);

        given(bookRepository.findByNameAndRecap(toCreate.getName(), toCreate.getRecap())).willReturn(null);
        given(writerRepository.findById((int)writerIds.get(0))).willReturn(null);
        given(writerService.getById(writerIds.get(0))).willReturn(null);
        given(writerService.getByIdThrowsException(writerIds.get(0))).willThrow(WriterNotFoundException.class);

        bookService.create(toCreate, writerIds);
    }

    @Test
    @Transactional
    public void editSuccess() {
        int bookId = ServiceTestUtils.generateValidBookId();
        Book toEdit = ServiceTestUtils.generateBookToEditSuccess();
        toEdit.setId(bookId);
        List<Integer> writerIds = ServiceTestUtils.generateWriterIdListSuccess(1);
        Writer foundWriter = ServiceTestUtils.generateWriterFoundById(writerIds.get(0));
        Book foundBook = ServiceTestUtils.generateBookFoundById(bookId);
        Book edited = ServiceTestUtils.generateEditedBook(toEdit);

        given(bookRepository.findById(bookId)).willReturn(foundBook);
        given(bookService.getById(bookId)).willReturn(foundBook);
        given(bookService.getByIdThrowsException(bookId)).willReturn(foundBook);
        given(bookRepository.findByNameAndRecap(toEdit.getName(), toEdit.getRecap())).willReturn(null);
        given(writerRepository.findById((int)writerIds.get(0))).willReturn(foundWriter);
        given(writerService.getById(writerIds.get(0))).willReturn(foundWriter);
        given(writerService.getByIdThrowsException(writerIds.get(0))).willReturn(foundWriter);
        given(bookRepository.save(foundBook)).willReturn(edited);

        Book result = bookService.edit(toEdit, writerIds);
        assertNotNull(result);
    }

    @Test(expected = BookNotFoundException.class)
    public void editFailId() {
        int bookId = ServiceTestUtils.generateInvalidBookId();
        Book toEdit = ServiceTestUtils.generateBookToEditSuccess();
        toEdit.setId(bookId);
        List<Integer> writerIds = ServiceTestUtils.generateWriterIdListSuccess(1);

        given(bookRepository.findById(bookId)).willReturn(null);
        given(bookService.getById(bookId)).willReturn(null);
        given(bookService.getByIdThrowsException(bookId)).willThrow(BookNotFoundException.class);

        bookService.edit(toEdit, writerIds);
    }

    @Test(expected = WriterNotFoundException.class)
    public void editFailWriters() {
        int bookId = ServiceTestUtils.generateValidBookId();
        Book toEdit = ServiceTestUtils.generateBookToEditSuccess();
        toEdit.setId(bookId);
        List<Integer> writerIds = ServiceTestUtils.generateWriterIdListFail(1);
        Book foundBook = ServiceTestUtils.generateBookFoundById(bookId);

        given(bookRepository.findById(bookId)).willReturn(foundBook);
        given(bookService.getById(bookId)).willReturn(foundBook);
        given(bookService.getByIdThrowsException(bookId)).willReturn(foundBook);
        given(bookRepository.findByNameAndRecap(toEdit.getName(), toEdit.getRecap())).willReturn(null);
        given(writerRepository.findById((int)writerIds.get(0))).willReturn(null);
        given(writerService.getById(writerIds.get(0))).willReturn(null);
        given(writerService.getByIdThrowsException(writerIds.get(0))).willThrow(WriterNotFoundException.class);

        bookService.edit(toEdit, writerIds);
    }

    @Test
    @Transactional
    public void deleteSuccess() {
        int bookId = ServiceTestUtils.generateValidBookId();
        Book found = ServiceTestUtils.generateBookFoundById(bookId);

        given(bookRepository.findById(bookId)).willReturn(found);
        given(bookService.getById(bookId)).willReturn(found);
        given(bookService.getByIdThrowsException(bookId)).willReturn(found);

        bookService.delete(bookId);
    }

    @Test(expected = BookNotFoundException.class)
    public void deleteFail() {
        int bookId = ServiceTestUtils.generateInvalidBookId();

        given(bookRepository.findById(bookId)).willReturn(null);
        given(bookService.getById(bookId)).willReturn(null);
        given(bookService.getByIdThrowsException(bookId)).willThrow(BookNotFoundException.class);

        bookService.delete(bookId);
    }
}

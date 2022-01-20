package com.example.catalogservice.api;

import com.example.catalogservice.dto.BookDTO;
import com.example.catalogservice.dto.ModifyBookDTO;
import com.example.catalogservice.exception.BookAlreadyExistsException;
import com.example.catalogservice.exception.BookNotFoundException;
import com.example.catalogservice.exception.InStockFailException;
import com.example.catalogservice.exception.WriterNotFoundException;
import com.example.catalogservice.feign.client.*;
import com.example.catalogservice.mapper.BookMapper;
import com.example.catalogservice.model.Book;
import com.example.catalogservice.service.impl.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerUnitTests {

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;

    @Test
    public void getAllSuccess() {
        int listSize = ApiTestUtils.generateBookListSize();
        List<Book> books = ApiTestUtils.generateBookList(listSize);
        List<BookDTO> booksDTO = ApiTestUtils.generateBookDTOList(books);

        when(bookService.getAll()).thenReturn(books);
        when(bookMapper.toBookDTOList(books)).thenReturn(booksDTO);

        ResponseEntity<List<BookDTO>> result = bookController.getAll();
        assertNotNull(result.getBody());
        assertEquals(200, result.getStatusCodeValue());
        assertEquals(listSize, result.getBody().size());
    }

    @Test
    public void getByIdSuccess() {
        int bookId = ApiTestUtils.generateBookId(true);
        Book found = ApiTestUtils.generateBookFoundById(bookId);
        BookDTO bookDTO = ApiTestUtils.generateBookDTOFoundById(bookId);

        when(bookService.getByIdThrowsException(bookId)).thenReturn(found);
        when(bookMapper.toBookDTO(found)).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.getById(bookId);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(bookId, response.getBody().getId());
    }

    @Test(expected = BookNotFoundException.class)
    public void getByIdFail() {
        int bookId = ApiTestUtils.generateBookId(false);

        when(bookService.getByIdThrowsException(bookId)).thenThrow(BookNotFoundException.class);

        bookController.getById(bookId);
    }

    @Test
    public void getByCartSuccess() {
        CartClientDTO cartClientDTO = ApiTestUtils.generateCartClientDTO(true);
        CartClient cartClient = ApiTestUtils.generateCartClient(cartClientDTO);
        List<BookCatalogData> bookCatalogData = ApiTestUtils.generateBookCatalogData();
        List<BookCatalogDataDTO> bookCatalogDataDTO = ApiTestUtils.generateBookCatalogDataDTO(bookCatalogData);

        when(bookMapper.toCartClient(cartClientDTO)).thenReturn(cartClient);
        when(bookService.getByCart(cartClient)).thenReturn(bookCatalogData);
        when(bookMapper.toBookCatalogDataDTOList(bookCatalogData)).thenReturn(bookCatalogDataDTO);

        List<BookCatalogDataDTO> result = bookController.getByCart(cartClientDTO);
        assertNotNull(result);
        assertEquals(bookCatalogDataDTO.size(), result.size());
    }

    @Test(expected = BookNotFoundException.class)
    public void getByCartFail() {
        CartClientDTO cartClientDTO = ApiTestUtils.generateCartClientDTO(false);
        CartClient cartClient = ApiTestUtils.generateCartClient(cartClientDTO);

        when(bookMapper.toCartClient(cartClientDTO)).thenReturn(cartClient);
        when(bookService.getByCart(cartClient)).thenThrow(BookNotFoundException.class);

        bookController.getByCart(cartClientDTO);
    }

    @Test
    @Transactional
    public void createSuccess() {
        ModifyBookDTO modifyBookDTO = ApiTestUtils.generateModifyBookDTO("create", "");
        Book toCreate = ApiTestUtils.generateBook(modifyBookDTO, modifyBookDTO.getWriterIds());
        Book created = ApiTestUtils.generateCreatedBook(toCreate);
        BookDTO bookDTO = ApiTestUtils.generateBookDTO(created);

        when(bookMapper.toBook(modifyBookDTO)).thenReturn(toCreate);
        when(bookService.create(toCreate, modifyBookDTO.getWriterIds())).thenReturn(created);
        when(bookMapper.toBookDTO(created)).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.create(modifyBookDTO);
        assertNotNull(response.getBody());
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(created.getId(), response.getBody().getId());
    }

    @Test(expected = BookAlreadyExistsException.class)
    public void createFailNameAndRecap() {
        ModifyBookDTO modifyBookDTO = ApiTestUtils.generateModifyBookDTO("create", "nameandrecap");
        Book toCreate = ApiTestUtils.generateBook(modifyBookDTO, modifyBookDTO.getWriterIds());

        when(bookMapper.toBook(modifyBookDTO)).thenReturn(toCreate);
        when(bookService.create(toCreate, modifyBookDTO.getWriterIds())).thenThrow(BookAlreadyExistsException.class);

        bookController.create(modifyBookDTO);
    }

    @Test(expected = WriterNotFoundException.class)
    public void createFailWriters() {
        ModifyBookDTO modifyBookDTO = ApiTestUtils.generateModifyBookDTO("create", "writers");
        Book toCreate = ApiTestUtils.generateBook(modifyBookDTO, modifyBookDTO.getWriterIds());

        when(bookMapper.toBook(modifyBookDTO)).thenReturn(toCreate);
        when(bookService.create(toCreate, modifyBookDTO.getWriterIds())).thenThrow(WriterNotFoundException.class);

        bookController.create(modifyBookDTO);
    }

    @Test
    public void editSuccess() {
        ModifyBookDTO modifyBookDTO = ApiTestUtils.generateModifyBookDTO("edit", "");
        Book toEdit = ApiTestUtils.generateBook(modifyBookDTO, modifyBookDTO.getWriterIds());
        Book edited = ApiTestUtils.generateEditedBook(toEdit);
        BookDTO bookDTO = ApiTestUtils.generateBookDTO(edited);

        when(bookMapper.toBook(modifyBookDTO)).thenReturn(toEdit);
        when(bookService.edit(toEdit, modifyBookDTO.getWriterIds())).thenReturn(edited);
        when(bookMapper.toBookDTO(edited)).thenReturn(bookDTO);

        ResponseEntity<BookDTO> response = bookController.edit(modifyBookDTO, modifyBookDTO.getId());
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(edited.getName(), response.getBody().getName());
        assertEquals(edited.getRecap(), response.getBody().getRecap());
        assertEquals(edited.getPrice(), response.getBody().getPrice(), 0);
        assertEquals(edited.getInStock(), response.getBody().getInStock());
        assertEquals(edited.getYearReleased(), response.getBody().getYearReleased());
    }

    @Test(expected = BookAlreadyExistsException.class)
    public void editFailNameAndRecap() {
        ModifyBookDTO modifyBookDTO = ApiTestUtils.generateModifyBookDTO("edit", "nameandrecap");
        Book toEdit = ApiTestUtils.generateBook(modifyBookDTO, modifyBookDTO.getWriterIds());

        when(bookMapper.toBook(modifyBookDTO)).thenReturn(toEdit);
        when(bookService.edit(toEdit, modifyBookDTO.getWriterIds())).thenThrow(BookAlreadyExistsException.class);

        bookController.edit(modifyBookDTO, modifyBookDTO.getId());
    }

    @Test(expected = WriterNotFoundException.class)
    public void editFailWriters() {
        ModifyBookDTO modifyBookDTO = ApiTestUtils.generateModifyBookDTO("edit", "writers");
        Book toEdit = ApiTestUtils.generateBook(modifyBookDTO, modifyBookDTO.getWriterIds());

        when(bookMapper.toBook(modifyBookDTO)).thenReturn(toEdit);
        when(bookService.edit(toEdit, modifyBookDTO.getWriterIds())).thenThrow(WriterNotFoundException.class);

        bookController.edit(modifyBookDTO, modifyBookDTO.getId());
    }

    @Test(expected = BookNotFoundException.class)
    public void editFailId() {
        ModifyBookDTO modifyBookDTO = ApiTestUtils.generateModifyBookDTO("edit", "id");
        Book toEdit = ApiTestUtils.generateBook(modifyBookDTO, modifyBookDTO.getWriterIds());

        when(bookMapper.toBook(modifyBookDTO)).thenReturn(toEdit);
        when(bookService.edit(toEdit, modifyBookDTO.getWriterIds())).thenThrow(BookNotFoundException.class);

        bookController.edit(modifyBookDTO, modifyBookDTO.getId());
    }

    @Test
    @Transactional
    public void deleteSuccess() {
        int bookId = ApiTestUtils.generateBookId(true);

        when(bookService.delete(bookId)).thenReturn(true);

        ResponseEntity<?> response = bookController.delete(bookId);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test(expected = BookNotFoundException.class)
    public void deleteFail() {
        int bookId = ApiTestUtils.generateBookId(false);

        when(bookService.delete(bookId)).thenThrow(BookNotFoundException.class);

        bookController.delete(bookId);
    }

    @Test
    @Transactional
    public void editInStockSuccess() {
        EditInStockDTO editInStockDTO = ApiTestUtils.generateEditInStockDTO("");
        EditInStock editInStock = ApiTestUtils.generateEditInStock(editInStockDTO);

        when(bookMapper.toEditInStock(editInStockDTO)).thenReturn(editInStock);
        when(bookService.editInStock(editInStock)).thenReturn(true);

        ResponseEntity<?> response = bookController.editInStock(editInStockDTO);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test(expected = BookNotFoundException.class)
    public void editInStockFailId() {
        EditInStockDTO editInStockDTO = ApiTestUtils.generateEditInStockDTO("id");
        EditInStock editInStock = ApiTestUtils.generateEditInStock(editInStockDTO);

        when(bookMapper.toEditInStock(editInStockDTO)).thenReturn(editInStock);
        when(bookService.editInStock(editInStock)).thenThrow(BookNotFoundException.class);

        bookController.editInStock(editInStockDTO);
    }

    @Test(expected = InStockFailException.class)
    public void editInStockFailAmounts() {
        EditInStockDTO editInStockDTO = ApiTestUtils.generateEditInStockDTO("amounts");
        EditInStock editInStock = ApiTestUtils.generateEditInStock(editInStockDTO);

        when(bookMapper.toEditInStock(editInStockDTO)).thenReturn(editInStock);
        when(bookService.editInStock(editInStock)).thenThrow(InStockFailException.class);

        bookController.editInStock(editInStockDTO);
    }
}

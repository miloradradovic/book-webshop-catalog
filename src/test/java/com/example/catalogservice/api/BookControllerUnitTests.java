package com.example.catalogservice.api;

import com.example.catalogservice.dto.ModifyBookDTO;
import com.example.catalogservice.exception.BookAlreadyExistsException;
import com.example.catalogservice.exception.BookNotFoundException;
import com.example.catalogservice.exception.WriterNotFoundException;
import com.example.catalogservice.feign.client.BookCatalogData;
import com.example.catalogservice.feign.client.CartClientDTO;
import com.example.catalogservice.feign.client.EditInStockDTO;
import com.example.catalogservice.mapper.BookMapper;
import com.example.catalogservice.model.Book;
import com.example.catalogservice.model.ModifyBook;
import com.example.catalogservice.security.UserDetailsImpl;
import com.example.catalogservice.service.impl.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookControllerUnitTests {

    private MockMvc mockMvc;

    @Mock
    private BookMapper bookMapper;

    @Mock
    private BookService bookService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final String basePath = "/api/books";

    @PostConstruct
    public void setup() {
        this.mockMvc = MockMvcBuilders.
                webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getAllSuccess() throws Exception {
        loginUser();
        int listSize = ApiTestUtils.generateBookListSize();
        List<Book> books = ApiTestUtils.generateBookList(listSize);

        given(bookService.getAll()).willReturn(books);

        mockMvc.perform(get(basePath))
                .andExpect(jsonPath("$", hasSize(listSize)))
                .andExpect(status().isOk());
    }

    @Test
    public void getByIdSuccess() throws Exception {
        loginUser();
        int bookId = ApiTestUtils.generateValidBookId();
        Book found = ApiTestUtils.generateBookFoundById(bookId);

        given(bookService.getByIdThrowsException(bookId)).willReturn(found);

        mockMvc.perform(get(basePath + "/" + bookId))
                .andExpect(jsonPath("$.id", is(bookId)))
                .andExpect(status().isOk());
    }

    @Test
    public void getByIdFail() throws Exception {
        loginUser();
        int bookId = ApiTestUtils.generateInvalidBookId();

        given(bookService.getByIdThrowsException(bookId)).willThrow(BookNotFoundException.class);

        mockMvc.perform(get(basePath + "/" + bookId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void editInStockSuccess() throws Exception {
        loginUser();
        EditInStockDTO editInStockDTO = ApiTestUtils.generateEditInStockDTOSuccess();
        String json = ApiTestUtils.json(editInStockDTO);

        mockMvc.perform(put(basePath + "/client/edit-in-stock")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void editInStockFail() throws Exception {
        loginUser();
        EditInStockDTO editInStockDTO = ApiTestUtils.generateEditInStockDTOFail();
        String json = ApiTestUtils.json(editInStockDTO);

        mockMvc.perform(put(basePath + "/client/edit-in-stock")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getByCartSuccess() throws Exception {
        loginUser();
        CartClientDTO cartClientDTO = ApiTestUtils.generateCartClientDTOSuccess();
        List<BookCatalogData> bookCatalogData = ApiTestUtils.generateBookCatalogData();
        String json = ApiTestUtils.json(cartClientDTO);

        given(bookService.getByCart(cartClientDTO.toCartClient())).willReturn(bookCatalogData);

        mockMvc.perform(post(basePath + "/client/get-by-cart")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(bookCatalogData.size())));
    }

    @Test
    public void getByCartFail() throws Exception {
        loginUser();
        CartClientDTO cartClientDTO = ApiTestUtils.generateCartClientDTOFail();
        String json = ApiTestUtils.json(cartClientDTO);

        given(bookService.getByCart(cartClientDTO.toCartClient())).willThrow(BookNotFoundException.class);

        mockMvc.perform(post(basePath + "/client/get-by-cart")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createSuccess() throws Exception {
        loginAdmin();
        ModifyBookDTO modifyBookDTO = ApiTestUtils.generateCreateBookDTOSuccess();
        ModifyBook modifyBook = ApiTestUtils.generateModifyBook(modifyBookDTO);
        String json = ApiTestUtils.json(modifyBookDTO);
        Book created = ApiTestUtils.generateCreatedBook(modifyBook);

        given(bookMapper.toModifyBook(modifyBookDTO)).willReturn(modifyBook);
        given(bookService.create(modifyBook)).willReturn(created);

        mockMvc.perform(post(basePath + "/create")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(created.getName())));
    }

    @Test
    public void createFailNameAndRecap() throws Exception {
        loginAdmin();
        ModifyBookDTO modifyBookDTO = ApiTestUtils.generateCreateBookDTOFailNameAndRecap();
        ModifyBook modifyBook = ApiTestUtils.generateModifyBook(modifyBookDTO);
        String json = ApiTestUtils.json(modifyBookDTO);

        given(bookMapper.toModifyBook(modifyBookDTO)).willReturn(modifyBook);
        given(bookService.create(modifyBook)).willThrow(BookAlreadyExistsException.class);

        mockMvc.perform(post(basePath + "/create")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createFailWriters() throws Exception {
        loginAdmin();
        ModifyBookDTO modifyBookDTO = ApiTestUtils.generateCreateBookDTOFailWriters();
        ModifyBook modifyBook = ApiTestUtils.generateModifyBook(modifyBookDTO);
        String json = ApiTestUtils.json(modifyBookDTO);

        given(bookMapper.toModifyBook(modifyBookDTO)).willReturn(modifyBook);
        given(bookService.create(modifyBook)).willThrow(WriterNotFoundException.class);

        mockMvc.perform(post(basePath + "/create")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void editSuccess() throws Exception {
        loginAdmin();
        int editId = ApiTestUtils.generateValidBookId();
        ModifyBookDTO modifyBookDTO = ApiTestUtils.generateEditBookDTOSuccess();
        modifyBookDTO.setId(editId);
        ModifyBook modifyBook = ApiTestUtils.generateModifyBook(modifyBookDTO);
        String json = ApiTestUtils.json(modifyBookDTO);
        Book edited = ApiTestUtils.generateEditedBook(modifyBook);

        given(bookMapper.toModifyBook(modifyBookDTO)).willReturn(modifyBook);
        given(bookService.edit(modifyBook)).willReturn(edited);

        mockMvc.perform(put(basePath + "/edit/" + editId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(edited.getName())))
                .andExpect(jsonPath("$.recap", is(edited.getRecap())));
    }

    @Test
    @Transactional
    public void editFailNameAndRecap() throws Exception {
        loginAdmin();
        int editId = ApiTestUtils.generateValidBookId();
        ModifyBookDTO modifyBookDTO = ApiTestUtils.generateEditBookDTOFailNameAndRecap();
        modifyBookDTO.setId(editId);
        ModifyBook modifyBook = ApiTestUtils.generateModifyBook(modifyBookDTO);
        String json = ApiTestUtils.json(modifyBookDTO);

        given(bookMapper.toModifyBook(modifyBookDTO)).willReturn(modifyBook);
        given(bookService.edit(modifyBook)).willThrow(BookAlreadyExistsException.class);

        mockMvc.perform(put(basePath + "/edit/" + editId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void editFailWriters() throws Exception {
        loginAdmin();
        int editId = ApiTestUtils.generateValidBookId();
        ModifyBookDTO modifyBookDTO = ApiTestUtils.generateEditBookDTOFailWriters();
        modifyBookDTO.setId(editId);
        ModifyBook modifyBook = ApiTestUtils.generateModifyBook(modifyBookDTO);
        String json = ApiTestUtils.json(modifyBookDTO);

        given(bookMapper.toModifyBook(modifyBookDTO)).willReturn(modifyBook);
        given(bookService.edit(modifyBook)).willThrow(WriterNotFoundException.class);

        mockMvc.perform(put(basePath + "/edit/" + editId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void editFailId() throws Exception {
        loginAdmin();
        int editId = ApiTestUtils.generateInvalidBookId();
        ModifyBookDTO modifyBookDTO = ApiTestUtils.generateEditBookDTOSuccess();
        modifyBookDTO.setId(editId);
        ModifyBook modifyBook = ApiTestUtils.generateModifyBook(modifyBookDTO);
        String json = ApiTestUtils.json(modifyBookDTO);

        given(bookMapper.toModifyBook(modifyBookDTO)).willReturn(modifyBook);
        given(bookService.edit(modifyBook)).willThrow(BookNotFoundException.class);

        mockMvc.perform(put(basePath + "/edit/" + editId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void deleteSuccess() throws Exception {
        loginAdmin();
        int bookId = ApiTestUtils.generateValidBookId();

        mockMvc.perform(delete(basePath + "/" + bookId))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteFail() throws Exception {
        loginAdmin();
        int bookId = ApiTestUtils.generateInvalidBookId();

        mockMvc.perform(delete(basePath + "/" + bookId))
                .andExpect(status().isBadRequest());
    }

    private void loginUser() {
        UserDetailsImpl user = ApiTestUtils.generateUserDetailsRoleUser();
        SecurityContextHolder.getContext().setAuthentication(ApiTestUtils.generateAuthentication(user));
    }

    private void loginAdmin() {
        UserDetailsImpl user = ApiTestUtils.generateUserDetailsRoleAdmin();
        SecurityContextHolder.getContext().setAuthentication(ApiTestUtils.generateAuthentication(user));
    }
}

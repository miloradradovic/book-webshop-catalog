package com.example.catalogservice.service;

import com.example.catalogservice.exception.WriterAlreadyExistsException;
import com.example.catalogservice.exception.WriterNotFoundException;
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
public class WriterServiceUnitTests {

    private MockMvc mockMvc;

    @InjectMocks
    private WriterService writerService;

    @Mock
    private WriterRepository writerRepository;

    @Mock
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    public void setup() {
        this.mockMvc = MockMvcBuilders.
                webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getByIdSuccess() {
        int writerId = ServiceTestUtils.generateValidWriterId();
        Writer found = ServiceTestUtils.generateWriterFoundById(writerId);

        given(writerRepository.findById(writerId)).willReturn(found);

        Writer result = writerService.getById(writerId);
        assertEquals(found.getId(), result.getId());
    }

    @Test
    public void getByIdReturnsNull() {
        int writerId = ServiceTestUtils.generateInvalidWriterId();

        given(writerRepository.findById(writerId)).willReturn(null);

        Writer result = writerService.getById(writerId);
        assertNull(result);
    }

    @Test
    public void getByIdThrowsExceptionSuccess() {
        int writerId = ServiceTestUtils.generateValidWriterId();
        Writer found = ServiceTestUtils.generateWriterFoundById(writerId);

        given(writerRepository.findById(writerId)).willReturn(found);
        given(writerService.getById(writerId)).willReturn(found);

        Writer result = writerService.getByIdThrowsException(writerId);
        assertEquals(found.getId(), result.getId());
    }

    @Test(expected = WriterNotFoundException.class)
    public void getByIdThrowsExceptionFail() {
        int writerId = ServiceTestUtils.generateInvalidWriterId();

        given(writerRepository.findById(writerId)).willReturn(null);
        given(writerService.getById(writerId)).willReturn(null);

        writerService.getByIdThrowsException(writerId);
    }

    @Test
    public void getAllSuccess() {
        int listSize = ServiceTestUtils.generateWriterListSize();
        List<Writer> writers = ServiceTestUtils.generateWriterList(listSize);

        given(writerRepository.findAll()).willReturn(writers);

        List<Writer> result = writerService.getAll();
        assertEquals(writers.size(), result.size());
    }

    @Test
    @Transactional
    public void createSuccess() {
        Writer toCreate = ServiceTestUtils.generateWriterToCreateSuccess();
        Writer created = ServiceTestUtils.generateCreatedWriter(toCreate);
        int listSize = ServiceTestUtils.generateWriterListSize();

        given(writerRepository.findByNameAndSurnameAndBiography(toCreate.getName(), toCreate.getSurname(), toCreate.getBiography())).willReturn(null);
        given(writerRepository.save(toCreate)).willReturn(created);

        Writer result = writerService.create(toCreate);
        assertEquals(toCreate.getName(), result.getName());
        assertEquals(toCreate.getSurname(), result.getSurname());
        assertEquals(toCreate.getBiography(), result.getBiography());
        assertTrue(result.getId() > listSize);
    }

    @Test(expected = WriterAlreadyExistsException.class)
    public void createFail() {
        Writer toCreate = ServiceTestUtils.generateWriterToCreateFail();
        Writer found = ServiceTestUtils.generateWriterFoundByNameAndSurnameAndBiography(toCreate.getName(), toCreate.getSurname(), toCreate.getBiography());

        given(writerRepository.findByNameAndSurnameAndBiography(toCreate.getName(), toCreate.getSurname(), toCreate.getBiography())).willReturn(found);

        writerService.create(toCreate);
    }

    @Test
    @Transactional
    public void editSuccess() {
        Writer toEdit = ServiceTestUtils.generateWriterToEditSuccess();
        Writer foundById = ServiceTestUtils.generateWriterFoundById(toEdit.getId());
        Writer edited = ServiceTestUtils.generateEditedWriter(toEdit);

        given(writerService.getById(toEdit.getId())).willReturn(foundById);
        given(writerService.getByIdThrowsException(toEdit.getId())).willReturn(foundById);
        given(writerRepository.findByNameAndSurnameAndBiography(toEdit.getName(), toEdit.getSurname(), toEdit.getBiography())).willReturn(null);
        given(writerRepository.save(foundById)).willReturn(edited);

        Writer result = writerService.edit(toEdit);
        assertEquals(edited.getName(), result.getName());
        assertEquals(edited.getSurname(), result.getSurname());
        assertEquals(edited.getBiography(), result.getBiography());

    }

    @Test(expected = WriterAlreadyExistsException.class)
    public void editFailWriterData() {
        Writer toEdit = ServiceTestUtils.generateWriterToEditFailWriterData();
        Writer foundById = ServiceTestUtils.generateWriterFoundById(toEdit.getId());

        given(writerService.getById(toEdit.getId())).willReturn(foundById);
        given(writerService.getByIdThrowsException(toEdit.getId())).willReturn(foundById);
        given(writerRepository.findByNameAndSurnameAndBiography(toEdit.getName(), toEdit.getSurname(), toEdit.getBiography())).willReturn(foundById);

        writerService.edit(toEdit);
    }

    @Test(expected = WriterNotFoundException.class)
    public void editFailId() {
        int writerId = ServiceTestUtils.generateInvalidWriterId();
        Writer toEdit = ServiceTestUtils.generateWriterToEditSuccess();
        toEdit.setId(writerId);

        given(writerService.getById(toEdit.getId())).willReturn(null);
        given(writerService.getByIdThrowsException(toEdit.getId())).willThrow(WriterNotFoundException.class);

        writerService.edit(toEdit);
    }
}

package com.example.catalogservice.service;

import com.example.catalogservice.exception.WriterAlreadyExistsException;
import com.example.catalogservice.exception.WriterNotFoundException;
import com.example.catalogservice.model.Writer;
import com.example.catalogservice.repository.WriterRepository;
import com.example.catalogservice.service.impl.WriterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WriterServiceUnitTests {

    @InjectMocks
    private WriterService writerService;

    @Mock
    private WriterRepository writerRepository;

    @Test
    public void getByIdSuccess() {
        int writerId = ServiceTestUtils.generateWriterId(true);
        Writer found = ServiceTestUtils.generateWriterFoundBy(writerId, "", "", "");

        when(writerRepository.findById(writerId)).thenReturn(found);

        Writer result = writerService.getById(writerId);
        verify(writerRepository).findById(writerId);
        verifyNoMoreInteractions(writerRepository);
        assertEquals(writerId, result.getId());
    }

    @Test
    public void getByIdReturnsNull() {
        int writerId = ServiceTestUtils.generateWriterId(false);

        when(writerRepository.findById(writerId)).thenReturn(null);

        Writer result = writerService.getById(writerId);
        verify(writerRepository).findById(writerId);
        verifyNoMoreInteractions(writerRepository);
        assertNull(result);
    }

    @Test
    public void getByIdThrowsExceptionSuccess() {
        int writerId = ServiceTestUtils.generateWriterId(true);
        Writer found = ServiceTestUtils.generateWriterFoundBy(writerId, "", "", "");

        when(writerRepository.findById(writerId)).thenReturn(found);

        Writer result = writerService.getByIdThrowsException(writerId);
        verify(writerRepository).findById(writerId);
        verifyNoMoreInteractions(writerRepository);
        assertEquals(writerId, result.getId());
    }

    @Test(expected = WriterNotFoundException.class)
    public void getByIdThrowsExceptionFail() {
        int writerId = ServiceTestUtils.generateWriterId(false);

        when(writerRepository.findById(writerId)).thenReturn(null);

        writerService.getByIdThrowsException(writerId);
        verify(writerRepository).findById(writerId);
        verifyNoMoreInteractions(writerRepository);
    }

    @Test
    public void getAllSuccess() {
        int listSize = ServiceTestUtils.generateWriterListSize();
        List<Writer> writers = ServiceTestUtils.generateWriterList(listSize);

        when(writerRepository.findAll()).thenReturn(writers);

        List<Writer> result = writerService.getAll();
        verify(writerRepository).findAll();
        verifyNoMoreInteractions(writerRepository);
        assertEquals(listSize, result.size());
    }

    @Test
    public void createSuccess() {
        Writer toCreate = ServiceTestUtils.generateWriter("create", "");
        Writer created = ServiceTestUtils.generateCreatedWriter(toCreate);

        when(writerRepository.findByNameAndSurnameAndBiography(toCreate.getName(), toCreate.getSurname(), toCreate.getBiography())).thenReturn(null);
        when(writerRepository.save(toCreate)).thenReturn(created);

        Writer result = writerService.create(toCreate);
        verify(writerRepository).findByNameAndSurnameAndBiography(toCreate.getName(), toCreate.getSurname(), toCreate.getBiography());
        verify(writerRepository).save(toCreate);
        verifyNoMoreInteractions(writerRepository);
        assertEquals(created.getId(), result.getId());
    }

    @Test(expected = WriterAlreadyExistsException.class)
    public void createFail() {
        Writer toCreate = ServiceTestUtils.generateWriter("create", "namesurnamebiography");
        Writer found = ServiceTestUtils.generateWriterFoundBy(0, toCreate.getName(), toCreate.getSurname(), toCreate.getBiography());

        when(writerRepository.findByNameAndSurnameAndBiography(toCreate.getName(), toCreate.getSurname(), toCreate.getBiography())).thenReturn(found);

        writerService.create(toCreate);
        verify(writerRepository).findByNameAndSurnameAndBiography(toCreate.getName(), toCreate.getSurname(), toCreate.getBiography());
        verifyNoMoreInteractions(writerRepository);
    }

    @Test
    public void editSuccess() {
        Writer toEdit = ServiceTestUtils.generateWriter("edit", "");
        Writer foundById = ServiceTestUtils.generateWriterFoundBy(toEdit.getId(), "", "", "");
        Writer edited = ServiceTestUtils.generateEditedWriter(foundById, toEdit);

        when(writerRepository.findById(toEdit.getId())).thenReturn(foundById);
        when(writerRepository.findByNameAndSurnameAndBiography(toEdit.getName(), toEdit.getSurname(), toEdit.getBiography())).thenReturn(null);
        when(writerRepository.save(foundById)).thenReturn(edited);

        Writer result = writerService.edit(toEdit);
        verify(writerRepository).findById(toEdit.getId());
        verify(writerRepository).findByNameAndSurnameAndBiography(toEdit.getName(), toEdit.getSurname(), toEdit.getBiography());
        verify(writerRepository).save(foundById);
        verifyNoMoreInteractions(writerRepository);
        assertEquals(edited.getId(), result.getId());
        assertEquals(edited.getSurname(), result.getSurname());
        assertEquals(edited.getName(), result.getName());
        assertEquals(edited.getBiography(), result.getBiography());
    }

    @Test(expected = WriterAlreadyExistsException.class)
    public void editFailWriterData() {
        Writer toEdit = ServiceTestUtils.generateWriter("edit", "namesurnamebiography");
        Writer foundById = ServiceTestUtils.generateWriterFoundBy(toEdit.getId(), "", "", "");
        Writer foundByData = ServiceTestUtils.generateWriterFoundBy(0, toEdit.getName(), toEdit.getSurname(), toEdit.getBiography());

        when(writerRepository.findById(toEdit.getId())).thenReturn(foundById);
        when(writerRepository.findByNameAndSurnameAndBiography(toEdit.getName(), toEdit.getSurname(), toEdit.getBiography())).thenReturn(foundByData);

        writerService.edit(toEdit);
        verify(writerRepository).findById(toEdit.getId());
        verify(writerRepository).findByNameAndSurnameAndBiography(toEdit.getName(), toEdit.getSurname(), toEdit.getBiography());
        verifyNoMoreInteractions(writerRepository);
    }

    @Test(expected = WriterNotFoundException.class)
    public void editFailId() {
        Writer toEdit = ServiceTestUtils.generateWriter("edit", "id");

        when(writerRepository.findById(toEdit.getId())).thenReturn(null);

        writerService.edit(toEdit);
        verify(writerRepository).findById(toEdit.getId());
        verifyNoMoreInteractions(writerRepository);
    }
}

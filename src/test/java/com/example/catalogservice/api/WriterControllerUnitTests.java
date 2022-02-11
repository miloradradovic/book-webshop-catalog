package com.example.catalogservice.api;

import com.example.catalogservice.dto.ModifyWriterDTO;
import com.example.catalogservice.dto.WriterDTO;
import com.example.catalogservice.exception.WriterAlreadyExistsException;
import com.example.catalogservice.exception.WriterNotFoundException;
import com.example.catalogservice.mapper.WriterMapper;
import com.example.catalogservice.model.Writer;
import com.example.catalogservice.service.impl.WriterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WriterControllerUnitTests {

    @InjectMocks
    private WriterController writerController;

    @Mock
    private WriterService writerService;

    @Mock
    private WriterMapper writerMapper;

    @Test
    public void getAllSuccess() {
        int listSize = ApiTestUtils.generateWriterListSize();
        List<Writer> writers = ApiTestUtils.generateWriterList(listSize);
        List<WriterDTO> writerDTOList = ApiTestUtils.generateWriterDTOList(writers);

        when(writerService.getAll()).thenReturn(writers);
        when(writerMapper.toWriterDTOList(writers)).thenReturn(writerDTOList);

        ResponseEntity<List<WriterDTO>> response = writerController.getAll();
        verify(writerService).getAll();
        verify(writerMapper).toWriterDTOList(writers);
        verifyNoMoreInteractions(writerService, writerMapper);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(listSize, response.getBody().size());
    }

    @Test
    public void getByIdSuccess() {
        int writerId = ApiTestUtils.generateWriterId(true);
        Writer found = ApiTestUtils.generateWriterFoundById(writerId);
        WriterDTO writerDTO = ApiTestUtils.generateWriterDTOFoundById(writerId);

        when(writerService.getByIdThrowsException(writerId)).thenReturn(found);
        when(writerMapper.toWriterDTO(found)).thenReturn(writerDTO);

        ResponseEntity<WriterDTO> response = writerController.getById(writerId);
        verify(writerService).getByIdThrowsException(writerId);
        verify(writerMapper).toWriterDTO(found);
        verifyNoMoreInteractions(writerService, writerMapper);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(writerId, response.getBody().getId());
    }

    @Test(expected = WriterNotFoundException.class)
    public void getByIdFail() {
        int writerId = ApiTestUtils.generateWriterId(false);

        when(writerService.getByIdThrowsException(writerId)).thenThrow(WriterNotFoundException.class);

        writerController.getById(writerId);
        verify(writerService).getByIdThrowsException(writerId);
        verifyNoMoreInteractions(writerService);
    }

    @Test
    public void createSuccess() {
        ModifyWriterDTO modifyWriterDTO = ApiTestUtils.generateModifyWriterDTO("create", "");
        Writer toCreate = ApiTestUtils.generateWriter(modifyWriterDTO);
        Writer created = ApiTestUtils.generateCreatedWriter(toCreate);
        WriterDTO writerDTO = ApiTestUtils.generateWriterDTO(created);

        when(writerMapper.toWriter(modifyWriterDTO)).thenReturn(toCreate);
        when(writerService.create(toCreate)).thenReturn(created);
        when(writerMapper.toWriterDTO(created)).thenReturn(writerDTO);

        ResponseEntity<WriterDTO> response = writerController.create(modifyWriterDTO);
        verify(writerMapper).toWriter(modifyWriterDTO);
        verify(writerMapper).toWriterDTO(created);
        verify(writerService).create(toCreate);
        verifyNoMoreInteractions(writerMapper, writerService);
        assertNotNull(response.getBody());
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(created.getId(), response.getBody().getId());
    }

    @Test(expected = WriterAlreadyExistsException.class)
    public void createFail() {
        ModifyWriterDTO modifyWriterDTO = ApiTestUtils.generateModifyWriterDTO("create", "namesurnamebiography");
        Writer toCreate = ApiTestUtils.generateWriter(modifyWriterDTO);

        when(writerMapper.toWriter(modifyWriterDTO)).thenReturn(toCreate);
        when(writerService.create(toCreate)).thenThrow(WriterAlreadyExistsException.class);

        writerController.create(modifyWriterDTO);
        verify(writerMapper).toWriter(modifyWriterDTO);
        verify(writerService).create(toCreate);
        verifyNoMoreInteractions(writerMapper, writerService);
    }

    @Test
    public void editSuccess() {
        ModifyWriterDTO modifyWriterDTO = ApiTestUtils.generateModifyWriterDTO("edit", "");
        Writer toEdit = ApiTestUtils.generateWriter(modifyWriterDTO);
        Writer edited = ApiTestUtils.generateEditedWriter(toEdit);
        WriterDTO writerDTO = ApiTestUtils.generateWriterDTO(edited);

        when(writerMapper.toWriter(modifyWriterDTO)).thenReturn(toEdit);
        when(writerService.edit(toEdit)).thenReturn(edited);
        when(writerMapper.toWriterDTO(edited)).thenReturn(writerDTO);

        ResponseEntity<WriterDTO> response = writerController.edit(modifyWriterDTO, modifyWriterDTO.getId());
        verify(writerMapper).toWriter(modifyWriterDTO);
        verify(writerMapper).toWriterDTO(edited);
        verify(writerService).edit(toEdit);
        verifyNoMoreInteractions(writerMapper, writerService);
        assertNotNull(response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(writerDTO.getId(), response.getBody().getId());
        assertEquals(writerDTO.getName(), response.getBody().getName());
        assertEquals(writerDTO.getSurname(), response.getBody().getSurname());
        assertEquals(writerDTO.getBiography(), response.getBody().getBiography());
    }

    @Test(expected = WriterAlreadyExistsException.class)
    public void editFailWriterData() {
        ModifyWriterDTO modifyWriterDTO = ApiTestUtils.generateModifyWriterDTO("edit", "namesurnamebiography");
        Writer toEdit = ApiTestUtils.generateWriter(modifyWriterDTO);

        when(writerMapper.toWriter(modifyWriterDTO)).thenReturn(toEdit);
        when(writerService.edit(toEdit)).thenThrow(WriterAlreadyExistsException.class);

        writerController.edit(modifyWriterDTO, modifyWriterDTO.getId());
        verify(writerMapper).toWriter(modifyWriterDTO);
        verify(writerService).edit(toEdit);
        verifyNoMoreInteractions(writerMapper, writerService);
    }

    @Test(expected = WriterNotFoundException.class)
    public void editFailId() {
        ModifyWriterDTO modifyWriterDTO = ApiTestUtils.generateModifyWriterDTO("edit", "id");
        Writer toEdit = ApiTestUtils.generateWriter(modifyWriterDTO);

        when(writerMapper.toWriter(modifyWriterDTO)).thenReturn(toEdit);
        when(writerService.edit(toEdit)).thenThrow(WriterNotFoundException.class);

        writerController.edit(modifyWriterDTO, modifyWriterDTO.getId());
        verify(writerMapper).toWriter(modifyWriterDTO);
        verify(writerService).edit(toEdit);
        verifyNoMoreInteractions(writerMapper, writerService);
    }
}

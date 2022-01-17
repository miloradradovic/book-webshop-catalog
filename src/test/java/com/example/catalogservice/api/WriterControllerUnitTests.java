package com.example.catalogservice.api;

import com.example.catalogservice.dto.ModifyWriterDTO;
import com.example.catalogservice.exception.WriterAlreadyExistsException;
import com.example.catalogservice.exception.WriterNotFoundException;
import com.example.catalogservice.mapper.WriterMapper;
import com.example.catalogservice.model.ModifyWriter;
import com.example.catalogservice.model.Writer;
import com.example.catalogservice.security.UserDetailsImpl;
import com.example.catalogservice.service.impl.WriterService;
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
public class WriterControllerUnitTests {

    private MockMvc mockMvc;

    @Mock
    private WriterMapper writerMapper;

    @Mock
    private WriterService writerService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final String basePath = "/api/writers";

    @PostConstruct
    public void setup() {
        this.mockMvc = MockMvcBuilders.
                webAppContextSetup(webApplicationContext).build();

        UserDetailsImpl user = ApiTestUtils.generateUserDetailsRoleAdmin();
        SecurityContextHolder.getContext().setAuthentication(ApiTestUtils.generateAuthentication(user));
    }

    @Test
    public void getAllSuccess() throws Exception {
        int listSize = ApiTestUtils.generateWriterListSize();
        List<Writer> writers = ApiTestUtils.generateWriterList(listSize);

        given(writerService.getAll()).willReturn(writers);

        mockMvc.perform(get(basePath))
                .andExpect(jsonPath("$", hasSize(listSize)))
                .andExpect(status().isOk());
    }

    @Test
    public void getByIdSuccess() throws Exception {
        int writerId = ApiTestUtils.generateValidWriterId();
        Writer found = ApiTestUtils.generateWriterFoundById(writerId);

        given(writerService.getByIdThrowsException(writerId)).willReturn(found);

        mockMvc.perform(get(basePath + "/" + writerId))
                .andExpect(jsonPath("$.id", is(writerId)))
                .andExpect(status().isOk());
    }

    @Test
    public void getByIdFail() throws Exception {
        int writerId = ApiTestUtils.generateInvalidWriterId();

        given(writerService.getByIdThrowsException(writerId)).willThrow(WriterNotFoundException.class);

        mockMvc.perform(get(basePath + "/" + writerId))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void createSuccess() throws Exception {
        ModifyWriterDTO modifyWriterDTO = ApiTestUtils.generateCreateWriterDTOSuccess();
        String json = ApiTestUtils.json(modifyWriterDTO);
        ModifyWriter modifyWriter = ApiTestUtils.generateModifyWriter(modifyWriterDTO);
        Writer created = ApiTestUtils.generateCreatedWriter(modifyWriter);

        given(writerMapper.toModifyWriter(modifyWriterDTO)).willReturn(modifyWriter);
        given(writerService.create(modifyWriter)).willReturn(created);

        mockMvc.perform(post(basePath + "/create")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(created.getName())))
                .andExpect(jsonPath("$.surname", is(created.getSurname())))
                .andExpect(jsonPath("$.biography", is(created.getBiography())));
    }

    @Test
    @Transactional
    public void createFail() throws Exception {
        ModifyWriterDTO modifyWriterDTO = ApiTestUtils.generateCreateWriterDTOFail();
        String json = ApiTestUtils.json(modifyWriterDTO);
        ModifyWriter modifyWriter = ApiTestUtils.generateModifyWriter(modifyWriterDTO);

        given(writerMapper.toModifyWriter(modifyWriterDTO)).willReturn(modifyWriter);
        given(writerService.create(modifyWriter)).willThrow(WriterAlreadyExistsException.class);

        mockMvc.perform(post(basePath + "/create")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    public void editSuccess() throws Exception {
        int writerId = ApiTestUtils.generateValidWriterId();
        ModifyWriterDTO modifyWriterDTO = ApiTestUtils.generateEditWriterDTOSuccess();
        modifyWriterDTO.setId(writerId);
        String json = ApiTestUtils.json(modifyWriterDTO);
        ModifyWriter modifyWriter = ApiTestUtils.generateModifyWriter(modifyWriterDTO);
        Writer edited = ApiTestUtils.generateEditedWriter(modifyWriter);

        given(writerMapper.toModifyWriter(modifyWriterDTO)).willReturn(modifyWriter);
        given(writerService.edit(modifyWriter)).willReturn(edited);

        mockMvc.perform(put(basePath + "/" + writerId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(edited.getName())))
                .andExpect(jsonPath("$.surname", is(edited.getSurname())))
                .andExpect(jsonPath("$.biography", is(edited.getBiography())));
    }

    @Test
    public void editFailWriterData() throws Exception {
        int writerId = ApiTestUtils.generateValidWriterId();
        ModifyWriterDTO modifyWriterDTO = ApiTestUtils.generateEditWriterDTOFail();
        modifyWriterDTO.setId(writerId);
        String json = ApiTestUtils.json(modifyWriterDTO);
        ModifyWriter modifyWriter = ApiTestUtils.generateModifyWriter(modifyWriterDTO);

        given(writerMapper.toModifyWriter(modifyWriterDTO)).willReturn(modifyWriter);
        given(writerService.edit(modifyWriter)).willThrow(WriterAlreadyExistsException.class);

        mockMvc.perform(put(basePath + "/" + writerId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void editFailId() throws Exception {
        int writerId = ApiTestUtils.generateInvalidWriterId();
        ModifyWriterDTO modifyWriterDTO = ApiTestUtils.generateEditWriterDTOSuccess();
        modifyWriterDTO.setId(writerId);
        String json = ApiTestUtils.json(modifyWriterDTO);
        ModifyWriter modifyWriter = ApiTestUtils.generateModifyWriter(modifyWriterDTO);

        given(writerMapper.toModifyWriter(modifyWriterDTO)).willReturn(modifyWriter);
        given(writerService.edit(modifyWriter)).willThrow(WriterNotFoundException.class);

        mockMvc.perform(put(basePath + "/" + writerId)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}

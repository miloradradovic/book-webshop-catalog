package com.example.catalogservice.api;

import com.example.catalogservice.dto.ModifyWriterDTO;
import com.example.catalogservice.dto.WriterDTO;
import com.example.catalogservice.mapper.WriterMapper;
import com.example.catalogservice.model.Writer;
import com.example.catalogservice.service.impl.WriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/api/writers", produces = MediaType.APPLICATION_JSON_VALUE)
public class WriterController {

    @Autowired
    WriterService writerService;

    @Autowired
    WriterMapper writerMapper;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<WriterDTO>> getAll() {
        List<Writer> writers = writerService.getAll();
        return new ResponseEntity<>(writerMapper.toWriterDTOList(writers), HttpStatus.OK);
    }

    @GetMapping(value = "/{writerId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<WriterDTO> getById(@PathVariable int writerId) {
        Writer writer = writerService.getByIdThrowsException(writerId);
        return new ResponseEntity<>(writerMapper.toWriterDTO(writer), HttpStatus.OK);
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<WriterDTO> create(@RequestBody @Valid  ModifyWriterDTO writerDTO) {
        Writer created = writerService.create(writerMapper.toWriter(writerDTO));
        return new ResponseEntity<>(writerMapper.toWriterDTO(created), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{writerId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<WriterDTO> edit(@RequestBody @Valid ModifyWriterDTO writerDTO, @PathVariable int writerId) {
        writerDTO.setId(writerId);
        Writer edited = writerService.edit(writerMapper.toWriter(writerDTO));
        return new ResponseEntity<>(writerMapper.toWriterDTO(edited), HttpStatus.OK);
    }

}

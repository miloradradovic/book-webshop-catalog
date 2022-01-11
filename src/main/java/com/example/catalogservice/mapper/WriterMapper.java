package com.example.catalogservice.mapper;

import com.example.catalogservice.dto.ModifyWriterDTO;
import com.example.catalogservice.dto.WriterDTO;
import com.example.catalogservice.model.ModifyWriter;
import com.example.catalogservice.model.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class WriterMapper {

    public Writer toWriter(WriterDTO dto) {
        return new Writer(dto.getId(), dto.getName(), dto.getSurname(), dto.getBiography());
    }

    public Set<Writer> toWriterSet(List<WriterDTO> dtos) {
        Set<Writer> writers = new HashSet<>();
        for (WriterDTO dto : dtos) {
            writers.add(toWriter(dto));
        }
        return writers;
    }

    public WriterDTO toWriterDTO(Writer writer) {
        return new WriterDTO(writer.getId(), writer.getName(), writer.getSurname(), writer.getBiography());
    }

    public List<WriterDTO> toWriterDTOList(Set<Writer> writerSet) {
        List<WriterDTO> writerDTOList = new ArrayList<>();
        for (Writer writer : writerSet) {
            writerDTOList.add(toWriterDTO(writer));
        }
        return writerDTOList;
    }

    public List<WriterDTO> toWriterDTOList(List<Writer> writers) {
        List<WriterDTO> writerDTOList = new ArrayList<>();
        for (Writer writer : writers) {
            writerDTOList.add(toWriterDTO(writer));
        }
        return writerDTOList;
    }

    public ModifyWriter toModifyWriter(ModifyWriterDTO dto) {
        return new ModifyWriter(dto.getId(), dto.getName(), dto.getSurname(), dto.getBiography());
    }
}

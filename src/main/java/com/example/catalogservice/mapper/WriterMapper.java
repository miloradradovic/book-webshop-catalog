package com.example.catalogservice.mapper;

import com.example.catalogservice.dto.WriterDTO;
import com.example.catalogservice.model.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class WriterMapper {

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
}

package com.example.catalogservice.service.impl;

import com.example.catalogservice.exception.DeleteWriterFailException;
import com.example.catalogservice.exception.WriterAlreadyExistsException;
import com.example.catalogservice.exception.WriterNotFoundException;
import com.example.catalogservice.model.Writer;
import com.example.catalogservice.repository.WriterRepository;
import com.example.catalogservice.service.IWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class WriterService implements IWriterService {

    @Autowired
    WriterRepository writerRepository;

    @Override
    public Writer getById(int writerId) {
        return writerRepository.findById(writerId);
    }

    @Override
    public Writer getByIdThrowsException(int writerId) {
        Writer found = getById(writerId);
        if (found == null) {
            throw new WriterNotFoundException();
        }
        return found;
    }

    @Override
    public List<Writer> getAll() {
        return writerRepository.findAll();
    }

    @Override
    public Writer create(Writer toCreate) {
        Writer exists = writerRepository.findByNameAndSurnameAndBiography(toCreate.getName(), toCreate.getSurname(), toCreate.getBiography());
        if (exists != null) {
            throw new WriterAlreadyExistsException();
        }
        return writerRepository.save(toCreate);
    }

    @Override
    public Writer edit(Writer toEdit) {
        Writer found = getByIdThrowsException(toEdit.getId());

        if (!found.getName().equals(toEdit.getName()) && !found.getSurname().equals(toEdit.getSurname()) &&
                !found.getBiography().equals(toEdit.getBiography())) {
            Writer exist = writerRepository.findByNameAndSurnameAndBiography(toEdit.getName(), toEdit.getSurname(), toEdit.getBiography());
            if (exist != null) {
                throw new WriterAlreadyExistsException();
            }
        }
        found.setBiography(toEdit.getBiography());
        found.setName(toEdit.getName());
        found.setSurname(toEdit.getSurname());
        return writerRepository.save(found);
    }

    @Override
    public boolean deleteWhereNoBooks(Set<Writer> writers) {
        try {
            for (Writer writer : writers) {
                if (writerRepository.countBooks(writer.getId()) == 0) {
                    writerRepository.delete(writer);
                }
            }
            return true;
        } catch (Exception e) {
            throw new DeleteWriterFailException();
        }
    }
}

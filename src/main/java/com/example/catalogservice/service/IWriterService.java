package com.example.catalogservice.service;

import com.example.catalogservice.model.ModifyWriter;
import com.example.catalogservice.model.Writer;

import java.util.List;
import java.util.Set;

public interface IWriterService {

    Writer getById(int writerId);
    Writer getByIdThrowsException(int writerId);
    List<Writer> getAll();
    Writer create(ModifyWriter toCreate);
    Writer edit(ModifyWriter toEdit);
    void deleteWhereNoBooks(Set<Writer> writers);
}

package com.example.catalogservice.repository;

import com.example.catalogservice.model.Writer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WriterRepository extends JpaRepository<Writer, Integer> {

    Writer findByNameAndSurnameAndBiography(String name, String surname, String biography);

    @Query(value = "SELECT count(writer_id) from writer_book where writer_id = :id", nativeQuery = true)
    int countBooks(int id);

    Writer findById(int id);
}

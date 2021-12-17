package com.example.catalogservice.mapper;

import com.example.catalogservice.model.Genre;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class GenreMapper {

    public String toString(Genre genre) {
        return genre.toString();
    }

    public List<String> toStringList(Set<Genre> genres) {
        List<String> genresList = new ArrayList<>();
        for (Genre genre : genres) {
            genresList.add(toString(genre));
        }
        return genresList;
    }
}

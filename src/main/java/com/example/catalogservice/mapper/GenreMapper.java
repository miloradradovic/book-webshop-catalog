package com.example.catalogservice.mapper;

import com.example.catalogservice.model.Genre;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class GenreMapper {

    public Genre toGenre(String genre) {
        return Genre.valueOf(genre);
    }

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

    public Set<Genre> toGenreSet(List<String> genres) {
        Set<Genre> genreSet = new HashSet<>();
        for (String genre : genres) {
            genreSet.add(toGenre(genre));
        }
        return genreSet;
    }
}

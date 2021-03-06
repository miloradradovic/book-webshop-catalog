package com.example.catalogservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookDTO {

    private int id;
    private String name;
    private int yearReleased;
    private String recap;
    private int inStock;
    private double price;
    private List<String> genres;
    private List<WriterDTO> writers;

}

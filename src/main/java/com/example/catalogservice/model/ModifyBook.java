package com.example.catalogservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModifyBook {

    private int id;
    private String name;
    private int yearReleased;
    private String recap;
    private int inStock;
    private double price;
    private Set<Genre> genres;
    private List<Integer> writerIds;
}

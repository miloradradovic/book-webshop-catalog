package com.example.catalogservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModifyBookDTO {

    private int id;

    @NotBlank(message = "Name can't be blank")
    private String name;

    @Min(value = 1800, message = "Year can't be less than 1800!")
    @Max(value = 2022, message = "Year can't be greater than 2022!")
    private int yearReleased;

    @NotBlank(message = "Recap can't be blank")
    private String recap;

    @Min(value = 0, message = "In stock value can't be less than 0!")
    private int inStock;

    @Min(value = 0, message = "Price value can't be less than 0!")
    private double price;

    @NotNull(message = "Genres can't be null!")
    @Size(min = 1, message = "Please provide at least one genre!")
    private List<String> genres;

    @NotNull(message = "Writers can't be null!")
    @Size(min = 1, message = "Please provide at least one writer!")
    private List<Integer> writerIds;

    public ModifyBookDTO(String name, int yearReleased, String recap, int inStock, double price, List<String> genres, List<Integer> writerIds) {
        this.name = name;
        this.yearReleased = yearReleased;
        this.recap = recap;
        this.inStock = inStock;
        this.price = price;
        this.genres = genres;
        this.writerIds = writerIds;
    }
}

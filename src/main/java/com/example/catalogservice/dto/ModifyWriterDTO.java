package com.example.catalogservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ModifyWriterDTO {

    private int id;

    @NotBlank(message = "Name can't be blank")
    @Pattern(regexp = "[A-Z][a-z]+", message = "Name starts with the uppercase!")
    private String name;

    @NotBlank(message = "Surname can't be blank")
    @Pattern(regexp = "[A-Z][a-z]+", message = "Surname starts with the uppercase!")
    private String surname;

    @NotBlank(message = "Biography can't be blank")
    private String biography;
}

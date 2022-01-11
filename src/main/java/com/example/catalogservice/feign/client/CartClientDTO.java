package com.example.catalogservice.feign.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartClientDTO {

    @NotNull(message = "Book ids can't be null!")
    private List<Integer> bookIds;

    public CartClient toCartClient() {
        return new CartClient(bookIds);
    }
}

package com.example.catalogservice.feign.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EditInStockDTO {

    private Map<Integer, Integer> amounts;

    public EditInStock toEditInStock() {
        return new EditInStock(amounts);
    }
}

package com.example.catalogservice.feign.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDetailsResponse {

    private String email;
    private String password;
    private List<String> roles;
}

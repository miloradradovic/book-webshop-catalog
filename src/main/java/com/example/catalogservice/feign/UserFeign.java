package com.example.catalogservice.feign;

import com.example.catalogservice.feign.client.UserDetailsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("auth-server")
public interface UserFeign {

    @GetMapping(value = "api/auth/client/get-user-details")
    UserDetailsResponse getUserDetails();
}

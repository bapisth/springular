package com.hemendra.springular.resource;

import com.hemendra.springular.model.UserDto;
import com.hemendra.springular.service.UserService;
import com.hemendra.springular.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * @author Hemendra
 */
@RestController
//@RequestMapping("/user")
public class UserResource {
    @Autowired
    private UserService userService;


    @GetMapping("/user")
    public List<UserDto> getAll() {
        List<UserDto> userDtoList = userService.getAll();
        return userDtoList;
    }

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE)
    public UserDto saveUser(@RequestBody UserDto userDto) {
        UserDto dto = userService.save(userDto);
        return dto;
    }
}

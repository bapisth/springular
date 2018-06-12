package com.hemendra.springular.service;

import com.hemendra.springular.model.UserDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserService {
    public List<UserDto> getAll();

    public UserDto save(UserDto userDto);

    public void update(UserDto userDto);

    public void delete(UserDto userDto);

}

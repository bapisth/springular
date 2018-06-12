package com.hemendra.springular.service.impl;

import com.hemendra.springular.entity.User;
import com.hemendra.springular.model.UserDto;
import com.hemendra.springular.repository.UserRepository;
import com.hemendra.springular.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<UserDto> getAll() {
        final List<UserDto> userDtoList = new ArrayList<>();
        Iterable<User> allUser = userRepository.findAll();
        if (!Objects.isNull(allUser)) {
            allUser.forEach(data-> {
                UserDto mapUserDto = modelMapper.map(data, UserDto.class);
                userDtoList.add(mapUserDto);
            });
        }

        return userDtoList;
    }

    @Override
    public UserDto save(UserDto userDto) {
        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userDto.setUserName(String.valueOf(userDto.getUserName()).toLowerCase());
        User user = modelMapper.map(userDto, User.class);
        User save = userRepository.save(user);
        return modelMapper.map(save, UserDto.class);
    }

    @Override
    public void update(UserDto userDto) {

    }

    @Override
    public void delete(UserDto userDto) {

    }
}

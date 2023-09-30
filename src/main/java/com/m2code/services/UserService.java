package com.m2code.services;

import com.m2code.dtos.PageableResponse;
import com.m2code.dtos.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    // create
    UserDto createUser(UserDto userDto);

    // update
    UserDto updateUser(UserDto userDto, String userId);

    // delete
    void delete(String userId) throws IOException;

    // get user by id
    UserDto getUserById(String userId);

    // get all user
    PageableResponse<UserDto> getAllUser(int page, int size, String sortBy, String sortDir);

    // get user by email
    UserDto getUserByEmail(String email);

    // search user
    List<UserDto> searchUser(String keyword);

    String uploadImage(MultipartFile file, String userId) throws IOException;
}

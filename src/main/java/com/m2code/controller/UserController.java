package com.m2code.controller;

import com.m2code.dtos.ApiResponseMessage;
import com.m2code.dtos.PageableResponse;
import com.m2code.dtos.UserDto;
import com.m2code.helper.Util;
import com.m2code.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "User")
public class UserController {

    private final UserService userService;

    @Value("${user.upload.path}")
    private String path;

    @PostMapping
    @Operation(summary = "create user")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserDto user = userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    @Operation(summary = "update user")
    public ResponseEntity<UserDto> updateUser(
            @RequestBody UserDto userDto,
            @PathVariable String userId) {
        UserDto updatedUser = userService.updateUser(userDto, userId);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "delete user- admin operation")
    public ResponseEntity<ApiResponseMessage<String>> delete(@PathVariable String userId) throws IOException {
        userService.delete(userId);
        ApiResponseMessage apiResponseMessage = ApiResponseMessage.builder()
                .message("User deleted successfully")
                .success(true)
                .build();
        return ResponseEntity.ok(apiResponseMessage);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "get user by email")
    public ResponseEntity<UserDto> findByEmail(@PathVariable String email) {
        UserDto userDto = userService.getUserByEmail(email);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping("/{userId}")
    @Operation(summary = "get user by id")
    public ResponseEntity<UserDto> findById(@PathVariable String userId) {
        UserDto userDto = userService.getUserById(userId);
        return ResponseEntity.ok(userDto);
    }

    @GetMapping
    @Operation(summary = "get all user")
    public ResponseEntity<PageableResponse<UserDto>> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "name") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDir
    ) {
        PageableResponse<UserDto> userDtos = userService.getAllUser(page, size, sortBy, sortDir);
        return ResponseEntity.ok(userDtos);
    }

    @GetMapping("/search/{keyword}")
    @Operation(summary = "search user")
    public ResponseEntity<List<UserDto>> search(@PathVariable String keyword) {
        List<UserDto> userDtos = userService.searchUser(keyword);
        return ResponseEntity.ok(userDtos);
    }

    @PostMapping("/image/{userId}")
    @Operation(summary = "upload user image")
    public ResponseEntity<ApiResponseMessage<String>> uploadUserImage(
            @RequestParam("image") MultipartFile file,
            @PathVariable String userId
    ) throws IOException {
        String fileName = userService.uploadImage(file, userId);
        ApiResponseMessage<String> apiResponseMessage = ApiResponseMessage.<String>builder()
                .message(fileName)
                .success(true)
                .build();
        return new ResponseEntity<>(apiResponseMessage, HttpStatus.CREATED);
    }

    @GetMapping("/image/{userId}")
    @Operation(summary = "searve user image")
    public void serveImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
        UserDto userDto = userService.getUserById(userId);
        InputStream inputStream = Util.serveFile(userDto.getProfileImage(), path);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream, response.getOutputStream());
    }
}

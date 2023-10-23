package com.m2code.services.impl;

import com.m2code.dtos.PageableResponse;
import com.m2code.dtos.UserDto;
import com.m2code.dtos._Role;
import com.m2code.entities.Role;
import com.m2code.entities.User;
import com.m2code.exception.BadApiRequestException;
import com.m2code.exception.ResourceNotFoundException;
import com.m2code.helper.UserSpecification;
import com.m2code.helper.Util;
import com.m2code.repositories.RoleRepository;
import com.m2code.repositories.UserRepository;
import com.m2code.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;

    @Value("${user.upload.path}")
    private String path;

    @Override
    public UserDto createUser(UserDto userDto) {
        Optional<User> dbUser = userRepository.findByEmail(userDto.getEmail());
        if (dbUser.isPresent()) {
            throw new BadApiRequestException("user already registered");
        }
        User user = modelMapper.map(userDto, User.class);
        user.setUserId(Util.getId());
        user.setPassword(encoder.encode(userDto.getPassword()));
        Role userRole =
                roleRepository.findByRole(_Role.ROLE_USER.name()).orElseThrow(() -> new ResourceNotFoundException(
                        "USER role is" +
                                " not" +
                                " available"));
        user.getRoles().add(userRole);
            
        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User is not registered"));

        User userToUpdate = modelMapper.map(userDto, User.class);
        userToUpdate.setUserId(user.getUserId());

        if (userToUpdate.getPassword() != null)
            userToUpdate.setPassword(userToUpdate.getPassword().trim());
        if (StringUtils.isEmpty(userToUpdate.getPassword()))
            userToUpdate.setPassword(user.getPassword());
        else
            userToUpdate.setPassword(encoder.encode(userToUpdate.getPassword()));

        return modelMapper.map(userRepository.save(userToUpdate), UserDto.class);
    }

    @Override
    public void delete(String userId) throws IOException {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User is not registered"));
        String fullFilePath = path.concat("/").concat(user.getProfileImage());
        if (new File(fullFilePath).exists()) {
            Files.delete(Path.of(fullFilePath));
        }
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User is not registered"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public PageableResponse<UserDto> getAllUser(int pageN, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC") ?
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageN, size, sort);
        Page<User> page = userRepository.findAll(pageable);

        return Util.getPageableResponse(page, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("User is not registered"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> searchUser(String keyword) {
        Specification<User> specification = Specification.where(null);
        specification = specification.and(UserSpecification.nameUserSpecification(keyword));
        return userRepository.findAll(specification).stream().map(
                user -> modelMapper.map(user, UserDto.class)
        ).collect(Collectors.toList());
    }

    @Override
    public String uploadImage(MultipartFile file, String userId) throws IOException {
        String fileName = Util.uploadFile(file, path);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User is not registered"));
        user.setProfileImage(fileName);
        userRepository.save(user);
        return fileName;
    }
}

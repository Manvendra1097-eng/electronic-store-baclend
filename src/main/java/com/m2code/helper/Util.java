package com.m2code.helper;

import com.m2code.dtos.PageableResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.stream.Collectors;

public interface Util {

    static String getId() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    static <U, V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> classN) {
        PageableResponse<V> pageableResponse = PageableResponse.<V>builder()
                .content(page.getContent().stream().map(
                        user -> new ModelMapper().map(user, classN)
                ).collect(Collectors.toList()))
                .isLastPage(page.isLast())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElement(page.getTotalElements())
                .totalPage(page.getTotalPages())
                .build();
        return pageableResponse;
    }

    static String uploadFile(MultipartFile file, String path) throws IOException {
        String filename = file.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf("."));
        filename = UUID.randomUUID().toString().replace("-", "").concat(extension);
        String fullFilePath = path + filename;
        File folder = new File(path);
        if (!folder.exists()) folder.mkdirs();
        Files.copy(file.getInputStream(), Path.of(fullFilePath));
        return filename;
    }

    static InputStream serveFile(String fileName, String path) throws FileNotFoundException {
        try {
            String fullFilePath = path.concat("/").concat(fileName);
            return new FileInputStream(fullFilePath);
        } catch (FileNotFoundException ex) {
            System.out.println("File not found");
        } catch (NullPointerException ex) {
            return null;
        }

        return null;
    }
}

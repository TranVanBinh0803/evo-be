package com.binhtv.uploadservice.controller;

import com.binhtv.uploadservice.dto.ApiResponse;
import com.binhtv.uploadservice.dto.UploadResponseDto;
import com.binhtv.uploadservice.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/uploads")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<UploadResponseDto>> uploadImage(@RequestParam("file") MultipartFile file) {
        final UploadResponseDto uploadResponse = uploadService.uploadProductImage(file);
        final ApiResponse<UploadResponseDto> response = new ApiResponse<>(
                "Upload image successful!",
                HttpStatus.CREATED.value(),
                uploadResponse);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
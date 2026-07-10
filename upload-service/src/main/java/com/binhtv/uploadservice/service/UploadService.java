package com.binhtv.uploadservice.service;

import com.binhtv.uploadservice.dto.UploadResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif");

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.product-image-prefix:products}")
    private String productImagePrefix;

    @Value("${aws.s3.public-base-url:}")
    private String publicBaseUrl;

    public UploadResponseDto uploadProductImage(MultipartFile file) {
        validateImage(file);

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename() == null
                ? "image"
                : file.getOriginalFilename());
        String fileName = buildFileName(originalFileName);
        String key = buildObjectKey(fileName);
        String contentType = file.getContentType();

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(contentType)
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            return UploadResponseDto.builder()
                    .fileName(fileName)
                    .contentType(contentType)
                    .size(file.getSize())
                    .url(resolvePublicUrl(key))
                    .build();
        } catch (IOException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not read uploaded file.", exception);
        } catch (S3Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Could not upload file to S3.", exception);
        }
    }

    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image file is required.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPEG, PNG, WEBP, and GIF images are allowed.");
        }
    }

    private String buildObjectKey(String fileName) {
        LocalDate now = LocalDate.now();
        return "%s/%d/%02d/%02d/%s".formatted(
                trimSlashes(productImagePrefix),
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                fileName);
    }

    private String buildFileName(String originalFileName) {
        String extension = StringUtils.getFilenameExtension(originalFileName);
        String safeBaseName = originalFileName;
        if (extension != null && originalFileName.length() > extension.length() + 1) {
            safeBaseName = originalFileName.substring(0, originalFileName.length() - extension.length() - 1);
        }

        safeBaseName = safeBaseName.replaceAll("[^a-zA-Z0-9-_]", "-").replaceAll("-+", "-");
        String suffix = extension == null || extension.isBlank() ? "" : "." + extension.toLowerCase();

        return "%s-%s%s".formatted(safeBaseName, UUID.randomUUID(), suffix);
    }

    private String resolvePublicUrl(String key) {
        if (publicBaseUrl != null && !publicBaseUrl.isBlank()) {
            return "%s/%s".formatted(publicBaseUrl.replaceAll("/+$", ""), key);
        }

        return s3Client.utilities()
                .getUrl(GetUrlRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .build())
                .toExternalForm();
    }

    private String trimSlashes(String value) {
        return value == null ? "" : value.replaceAll("^/+|/+$", "");
    }
}
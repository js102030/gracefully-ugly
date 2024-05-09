package com.gracefullyugly.domain.image.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.gracefullyugly.domain.image.dto.ImageUploadResponse;
import com.gracefullyugly.domain.image.entity.Image;
import com.gracefullyugly.domain.image.repository.ImageRepository;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
@Slf4j
public class ImageUploadService {

    private final AmazonS3 amazonS3;
    private final ImageRepository imageRepository;

    private final Set<String> uploadedFileNames = new HashSet<>();
    private final Set<Long> uploadedFileSizes = new HashSet<>();

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxSizeString;

    // 여러장의 파일 저장
//    public List<String> saveFiles(List<MultipartFile> multipartFiles) {
//        List<String> uploadedUrls = new ArrayList<>();
//
//        for (MultipartFile multipartFile : multipartFiles) {
//
//            if (isDuplicate(multipartFile)) {
//                throw new RuntimeException("Duplicate file request");
//            }
//
//            String uploadedUrl = saveFile(multipartFile);
//            uploadedUrls.add(uploadedUrl);
//        }
//
//        clear();
//        return uploadedUrls;
//    }

    // 파일 삭제
    public void deleteFile(String fileUrl) {
        String[] urlParts = fileUrl.split("/");
        String fileBucket = urlParts[2].split("\\.")[0];

        if (!fileBucket.equals(bucket)) {
            throw new RuntimeException("No Image Exist");
        }

        String objectKey = String.join("/", Arrays.copyOfRange(urlParts, 3, urlParts.length));

        if (!amazonS3.doesObjectExist(bucket, objectKey)) {
            throw new RuntimeException("No Image Exist");
        }

        try {
            amazonS3.deleteObject(bucket, objectKey);
        } catch (AmazonS3Exception e) {
            log.error("error : Amazon S3 error while deleting file: {}", e.getMessage());
            throw new RuntimeException("Fail to delete file");
        } catch (SdkClientException e) {
            log.error("error : AWS SDK client error while deleting file: {}", e.getMessage());
            throw new RuntimeException("Fail to delete file");
        }

        System.out.println("File delete complete: " + objectKey);
    }

    // 단일 파일 저장
    public ImageUploadResponse saveFile(MultipartFile file, Long itemId, Long reviewId) {

        String originalFilename = file.getOriginalFilename();
        String randomFilename = generateRandomFilename(file);

        log.info("File upload started: {}", randomFilename);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            amazonS3.putObject(bucket, randomFilename, file.getInputStream(), metadata);
        } catch (AmazonS3Exception e) {
            log.error("error : Amazon S3 error while uploading file: {}", e.getMessage());
            throw new RuntimeException("Fail to upload file");
        } catch (SdkClientException e) {
            log.error("error : AWS SDK client error while uploading file: {}", e.getMessage());
            throw new RuntimeException("Fail to upload file");
        } catch (IOException e) {
            log.error("error : IO error while uploading file: {}", e.getMessage());
            throw new RuntimeException("Fail to upload file");
        }

        System.out.println("File upload completed: " + randomFilename);

        String url = amazonS3.getUrl(bucket, randomFilename).toString();

        Image savedImage;
        Image createdImage;
        if (reviewId == null) {
            createdImage = Image.createItemImage(itemId, url, originalFilename, randomFilename, file.getSize());
        } else {
            createdImage = Image.createReviewImage(reviewId, url, originalFilename, randomFilename, file.getSize());
        }
        savedImage = imageRepository.save(createdImage);

        return ImageUploadResponse.builder()
                .url(savedImage.getUrl())
                .originalImageName(savedImage.getOriginalImageName())
                .storedImageName(savedImage.getStoredImageName())
                .size(savedImage.getSize())
                .build();

    }

    // 요청에 중복되는 파일 여부 확인
    private boolean isDuplicate(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        Long fileSize = multipartFile.getSize();

        if (uploadedFileNames.contains(fileName) && uploadedFileSizes.contains(fileSize)) {
            return true;
        }

        uploadedFileNames.add(fileName);
        uploadedFileSizes.add(fileSize);

        return false;
    }

    private void clear() {
        uploadedFileNames.clear();
        uploadedFileSizes.clear();
    }

    // 랜덤파일명 생성 (파일명 중복 방지)
    private String generateRandomFilename(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();
        String fileExtension = validateFileExtension(originalFilename);
        return UUID.randomUUID() + "." + fileExtension;
    }

    // 파일 확장자 체크
    private String validateFileExtension(String originalFilename) {
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        List<String> allowedExtensions = Arrays.asList("jpg", "png", "gif", "jpeg", "webp");

        if (!allowedExtensions.contains(fileExtension)) {
            throw new RuntimeException("Invalid file extension");
        }
        return fileExtension;
    }
}

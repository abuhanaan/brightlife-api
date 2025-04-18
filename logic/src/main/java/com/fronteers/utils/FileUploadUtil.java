package com.fronteers.utils;

import com.fronteers.config.AwsConfig;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Component
@RequiredArgsConstructor
public class FileUploadUtil {

  private final S3Client s3Client;
  private final AwsConfig awsConfig;

  public String uploadFile(MultipartFile file, String fileType, String owner) throws IOException {
    String bucketName = awsConfig.getBucketName();
    String fileName =
        UUID.randomUUID() + "_" + fileType + "_" + owner + "_" + file.getOriginalFilename();

    PutObjectRequest putObjectRequest = PutObjectRequest.builder()
        .bucket(bucketName)
        .key(fileName)
        .acl(ObjectCannedACL.PUBLIC_READ)
        .contentType(file.getContentType())
        .build();

    s3Client.putObject(putObjectRequest, RequestBody.fromBytes(file.getBytes()));

    return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, awsConfig.getRegion(),
        fileName);
  }

}

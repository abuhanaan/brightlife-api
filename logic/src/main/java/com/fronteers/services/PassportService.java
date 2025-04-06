package com.fronteers.services;

//import com.amazonaws.services.s3.model.CannedAccessControlList;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import com.amazonaws.services.s3.model.PutObjectRequest;
//import com.amazonaws.services.s3.AmazonS3;

import com.fronteers.brightlife.model.PassportResponse;
import com.fronteers.config.AwsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PassportService {

  //  private final AmazonS3 amazonS3;
  private final AwsConfig awsConfig;

  public PassportResponse upload(String name, Integer age, MultipartFile file) {
//    String bucketName = awsConfig.getBucketName();
//    String fileName = UUID.randomUUID() + name + "_" + file.getOriginalFilename();
//    try {
//      ObjectMetadata metadata = new ObjectMetadata();
//      metadata.setContentLength(file.getSize());
//      metadata.setContentType(file.getContentType());
//
//      amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), metadata)
//          .withCannedAcl(CannedAccessControlList.PublicRead));
//
//      String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();
//
//      PassportResponse response = new PassportResponse();
//      response.setAge(age);
//      response.setName(name);
//      response.setPassportUrl(fileUrl);
//      return response;
//
//    } catch (IOException e) {
//      throw new ProcessingException("Failed to upload file: " + e);
//    }
    return null;
  }
}

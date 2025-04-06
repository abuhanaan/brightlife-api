package com.fronteers.config;

//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Component
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "aws")
public class AwsConfig {

  private String accessKey;
  private String secretKey;
  private String bucketName;
  private String region;

//  @Bean
//  public AmazonS3 amazonS3() {
//    BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
//    return AmazonS3ClientBuilder.standard()
//        .withRegion(region)
//        .withCredentials(new AWSStaticCredentialsProvider(credentials))
//        .build();
//  }

  @Bean
  public S3Client s3Client() {
    AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);

    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(StaticCredentialsProvider.create(credentials))
        .build();
  }
}

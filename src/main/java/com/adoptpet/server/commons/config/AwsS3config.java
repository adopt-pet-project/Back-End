package com.adoptpet.server.commons.config;

import com.adoptpet.server.commons.properties.AwsS3Properties;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = AwsS3Properties.class)
@RequiredArgsConstructor
public class AwsS3config {

    private final AwsS3Properties awsS3Properties;

    @Bean
    public AmazonS3Client amazonS3Client() {

        // AWS의 기본 인증 자격을 생성하는 BasicAWSCredentials 객체 생성
        BasicAWSCredentials awsCredentials= new BasicAWSCredentials(awsS3Properties.getAccessKey(), awsS3Properties.getSecretKey());

        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(awsS3Properties.getRegion())
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials)) // 인증 자격을 제공할 객체 설정
                .build();
    }
}

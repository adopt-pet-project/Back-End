package com.adoptpet.server.commons.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "aws.s3")
@AllArgsConstructor
public class AwsS3Properties {

    String accessKey;

    String secretKey;

    String bucket;

    String region;
}
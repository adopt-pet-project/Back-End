package com.adoptpet.server.commons.properties;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@Setter
@ConstructorBinding
@ConfigurationProperties(prefix = "aws.s3")
@RequiredArgsConstructor
public class AwsS3Properties {

    String accessKey;

    String secretKey;

    String bucket;

    String region;
}
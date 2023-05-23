package com.adoptpet.server.commons.util;

public abstract class ConstantUtil {

    public static final String MASTER_DATASOURCE = "masterDataSource";
    public static final String SLAVE_DATASOURCE = "slaveDataSource";

    public static final String DEFAULT_PROFILE
            = "https://project-adopt-bucket.s3.ap-northeast-2.amazonaws.com/other/default-profile-image.jpeg";

    public static final String DEFAULT_ADOPT_IMAGE
            ="https://project-adopt-bucket.s3.ap-northeast-2.amazonaws.com/other/cat.jpeg";

    public static final String KAFKA_TOPIC = "adopt-chat";
    public static final String GROUP_ID = "adopt";
    public static final String KAFKA_BROKER = "3.36.132.160:9092";
}

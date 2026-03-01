package com.instructor.aws;

import io.github.cdimascio.dotenv.Dotenv;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

public class AWSConfig {
    private static final Dotenv dotenv = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    public static boolean areCredentialsPresent() {
        String accessKey = dotenv.get("AWS_ACCESS_KEY_ID", System.getenv("AWS_ACCESS_KEY_ID"));
        String secretKey = dotenv.get("AWS_SECRET_ACCESS_KEY", System.getenv("AWS_SECRET_ACCESS_KEY"));
        return accessKey != null && secretKey != null;
    }

    public static StaticCredentialsProvider getCredentialsProvider() {
        String accessKey = dotenv.get("AWS_ACCESS_KEY_ID", System.getenv("AWS_ACCESS_KEY_ID"));
        String secretKey = dotenv.get("AWS_SECRET_ACCESS_KEY", System.getenv("AWS_SECRET_ACCESS_KEY"));

        if (accessKey == null || secretKey == null) {
            return null; // Return null to signal missing credentials
        }

        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey));
    }

    public static Region getRegion() {
        String regionStr = dotenv.get("AWS_REGION", System.getenv("AWS_REGION"));
        if (regionStr == null) {
            return Region.US_EAST_1; // Default
        }
        return Region.of(regionStr);
    }
}

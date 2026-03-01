package com.instructor.aws;

import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;

public class LambdaService {
    private final LambdaClient lambdaClient;
    private final String functionName = "PoseEstimationFunction";

    public LambdaService() {
        this.lambdaClient = LambdaClient.builder()
                .region(AWSConfig.getRegion())
                .credentialsProvider(AWSConfig.getCredentialsProvider())
                .build();
    }

    public String invokePoseEstimation(String videoS3Key) {
        String payload = String.format("{\"videoKey\": \"%s\"}", videoS3Key);

        InvokeRequest invokeRequest = InvokeRequest.builder()
                .functionName(functionName)
                .payload(SdkBytes.fromUtf8String(payload))
                .build();

        InvokeResponse response = lambdaClient.invoke(invokeRequest);
        String result = response.payload().asUtf8String();
        System.out.println("Invoked Lambda for pose estimation result: " + result);
        return result;
    }
}

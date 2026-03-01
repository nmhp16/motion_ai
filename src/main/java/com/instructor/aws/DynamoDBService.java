package com.instructor.aws;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.util.HashMap;
import java.util.Map;

public class DynamoDBService {
    private final DynamoDbClient dynamoDbClient;
    private final String tableName = "MotionAI_Analyses";

    public DynamoDBService() {
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(AWSConfig.getRegion())
                .credentialsProvider(AWSConfig.getCredentialsProvider())
                .build();
    }

    public void saveAnalysisResult(String videoId, String videoUrl, int score, String feedback) {
        Map<String, AttributeValue> itemValues = new HashMap<>();
        itemValues.put("VideoId", AttributeValue.builder().s(videoId).build());
        itemValues.put("VideoUrl", AttributeValue.builder().s(videoUrl).build());
        itemValues.put("Score", AttributeValue.builder().n(String.valueOf(score)).build());
        itemValues.put("Feedback", AttributeValue.builder().s(feedback).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(tableName)
                .item(itemValues)
                .build();

        dynamoDbClient.putItem(putItemRequest);
        System.out.println("Saved analysis results for " + videoId + " in DynamoDB.");
    }

    public Map<String, AttributeValue> getAnalysisResult(String videoId) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("VideoId", AttributeValue.builder().s(videoId).build());

        GetItemRequest getItemRequest = GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build();

        GetItemResponse response = dynamoDbClient.getItem(getItemRequest);
        return response.item();
    }
}

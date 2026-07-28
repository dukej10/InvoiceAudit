package co.com.credit.dynamodb.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DynamoDBConnectionProperties {
    private String accessKey;
    private String secretKey;
}

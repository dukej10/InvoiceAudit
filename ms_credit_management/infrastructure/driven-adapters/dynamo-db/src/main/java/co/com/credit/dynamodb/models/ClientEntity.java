package co.com.credit.dynamodb.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/* Enhanced DynamoDB annotations are incompatible with Lombok #1932
         https://github.com/aws/aws-sdk-java-v2/issues/1932*/
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDbBean
public class ClientEntity {

    private String clientId;
    private Boolean creditState;
    private BigDecimal initialCreditLimit;
    private BigDecimal currentCreditLimit;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;


    @DynamoDbPartitionKey
    @DynamoDbAttribute("client_id")
    public String getClientId() {
        return clientId;
    }

    @DynamoDbAttribute("credit_state")
    public Boolean getCreditState() {
        return creditState;
    }

    @DynamoDbAttribute("initial_credit_limit")
    public BigDecimal getInitialCreditLimit() {
        return initialCreditLimit;
    }

    @DynamoDbAttribute("current_credit_limit")
    public BigDecimal getCurrentCreditLimit() {
        return currentCreditLimit;
    }

    @DynamoDbAttribute("created_date")
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    @DynamoDbAttribute("updated_date")
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

}

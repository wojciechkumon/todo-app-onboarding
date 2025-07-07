package com.jigcar.todoapp.config

import com.jigcar.todoapp.model.TodoDbRecord
import io.micronaut.context.annotation.Bean
import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

@Factory
class DynamoDbConfiguration {

    @Bean
    @Singleton
    fun dynamoDbClient(): DynamoDbClient =
        DynamoDbClient.builder()
            .region(Region.EU_WEST_3)
            .build()

    @Bean
    @Singleton
    fun dynamoDbEnhancedClient(dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient =
        DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build()

    @Bean
    @Singleton
    fun todoTable(enhancedClient: DynamoDbEnhancedClient): DynamoDbTable<TodoDbRecord> =
        enhancedClient.table(
            "Todos",
            TableSchema.fromBean(TodoDbRecord::class.java)
        )
}

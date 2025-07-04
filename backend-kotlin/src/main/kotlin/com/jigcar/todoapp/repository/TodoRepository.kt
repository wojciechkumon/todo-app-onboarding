package com.jigcar.todoapp.repository

import com.jigcar.todoapp.model.TodoDbRecord
import jakarta.inject.Singleton
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient

@Singleton
class TodoRepository {

    private val dynamoDbClient = DynamoDbClient.builder()
        .region(Region.EU_WEST_3)
        .build()

    private val enhancedClient = DynamoDbEnhancedClient.builder()
        .dynamoDbClient(dynamoDbClient)
        .build()

    private val table: DynamoDbTable<TodoDbRecord> = enhancedClient.table("Todos", TableSchema.fromBean(TodoDbRecord::class.java))

    fun save(todo: TodoDbRecord) {
        table.putItem(todo)
    }

    fun listAll(): List<TodoDbRecord> = table.scan().items().toList()
}

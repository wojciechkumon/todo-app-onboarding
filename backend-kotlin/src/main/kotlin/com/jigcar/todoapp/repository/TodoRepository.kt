package com.jigcar.todoapp.repository

import com.jigcar.todoapp.model.TodoDbRecord
import jakarta.inject.Singleton
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.ReturnValue
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest

@Singleton
class TodoRepository {

    private val dynamoDbClient = DynamoDbClient.builder()
        .region(Region.EU_WEST_3)
        .build()

    private val enhancedClient = DynamoDbEnhancedClient.builder()
        .dynamoDbClient(dynamoDbClient)
        .build()

    private val table: DynamoDbTable<TodoDbRecord> =
        enhancedClient.table("Todos", TableSchema.fromBean(TodoDbRecord::class.java))

    fun save(todo: TodoDbRecord) {
        table.putItem(todo)
    }

    fun updateById(id: String, content: String, completed: Boolean): TodoDbRecord {
        val updateRequest = UpdateItemRequest.builder()
            .tableName("Todos")
            .key(mapOf("id" to AttributeValue.builder().s(id).build()))
            .updateExpression("SET content = :content, completed = :completed")
            .expressionAttributeValues(
                mapOf(
                    ":content" to AttributeValue.builder().s(content).build(),
                    ":completed" to AttributeValue.builder().bool(completed).build()
                )
            )
            .conditionExpression("attribute_exists(id)")
            .returnValues(ReturnValue.ALL_NEW)
            .build()

        val result = dynamoDbClient.updateItem(updateRequest)

        val attributes = result.attributes()
        return TodoDbRecord(
            id = attributes["id"]?.s() ?: "",
            content = attributes["content"]?.s() ?: "",
            completed = attributes["completed"]?.bool() ?: false,
            createdAt = attributes["createdAt"]?.s() ?: ""
        )
    }

    fun listAll(): List<TodoDbRecord> = table.scan().items().toList()
}

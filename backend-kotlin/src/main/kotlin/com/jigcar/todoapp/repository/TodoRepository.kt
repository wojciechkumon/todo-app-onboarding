package com.jigcar.todoapp.repository

import com.jigcar.todoapp.model.TodoDbRecord
import jakarta.inject.Singleton
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest
import software.amazon.awssdk.services.dynamodb.model.ReturnValue
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest

@Singleton
class TodoRepository(
    private val dynamoDbClient: DynamoDbClient,
    private val table: DynamoDbTable<TodoDbRecord>
) {
    fun save(todo: TodoDbRecord) {
        table.putItem(todo)
    }

    fun updateById(id: String, content: String, completed: Boolean): TodoDbRecord? {
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

        val result = try {
            dynamoDbClient.updateItem(updateRequest)
        } catch (_: ConditionalCheckFailedException) {
            return null
        }

        val attributes = result.attributes()
        return TodoDbRecord(
            id = attributes["id"]?.s() ?: "",
            content = attributes["content"]?.s() ?: "",
            completed = attributes["completed"]?.bool() ?: false,
            createdAt = attributes["createdAt"]?.s() ?: ""
        )
    }

    fun deleteById(id: String) {
        val deleteRequest = DeleteItemRequest.builder()
            .tableName("Todos")
            .key(mapOf("id" to AttributeValue.builder().s(id).build()))
            .build()

        dynamoDbClient.deleteItem(deleteRequest)
    }

    fun listAll(): List<TodoDbRecord> = table.scan().items().toList()
}
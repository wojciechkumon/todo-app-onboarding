package com.jigcar.todoapp.repository

import com.jigcar.todoapp.model.TodoDbRecord
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argThat
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest
import software.amazon.awssdk.services.dynamodb.model.ReturnValue
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse

class TodoRepositoryTest {
    private lateinit var dynamoDbClient: DynamoDbClient
    private lateinit var table: DynamoDbTable<TodoDbRecord>
    private lateinit var todoRepository: TodoRepository

    private val testTodo = TodoDbRecord(
        id = "test-id",
        content = "Test content",
        completed = false,
        createdAt = "2023-01-01T00:00:00Z"
    )

    @BeforeEach
    fun setUp() {
        dynamoDbClient = mock()
        table = mock()
        todoRepository = TodoRepository(dynamoDbClient, table)
    }

    @Test
    fun `save should call putItem on the table`() {
        todoRepository.save(testTodo)

        verify(table).putItem(testTodo)
    }

    @Test
    fun `updateById should return updated todo when successful`() {
        val id = "test-id"
        val content = "Updated content"
        val completed = true
        val updateItemResponse = UpdateItemResponse.builder()
            .attributes(
                mapOf(
                    "id" to AttributeValue.builder().s(id).build(),
                    "content" to AttributeValue.builder().s(content).build(),
                    "completed" to AttributeValue.builder().bool(completed).build(),
                    "createdAt" to AttributeValue.builder().s("2023-01-01T00:00:00Z").build()
                )
            )
            .build()
        whenever(dynamoDbClient.updateItem(any<UpdateItemRequest>())).thenReturn(updateItemResponse)

        val result = todoRepository.updateById(id, content, completed)

        verify(dynamoDbClient).updateItem(
            argThat<UpdateItemRequest> {
                tableName() == "Todos" &&
                        key()["id"]?.s() == id &&
                        updateExpression() == "SET content = :content, completed = :completed" &&
                        expressionAttributeValues()[":content"]?.s() == content &&
                        expressionAttributeValues()[":completed"]?.bool() == completed &&
                        conditionExpression() == "attribute_exists(id)" &&
                        returnValues() == ReturnValue.ALL_NEW
            }
        )
        assertEquals(id, result?.id)
        assertEquals(content, result?.content)
        assertEquals(completed, result?.completed)
        assertEquals("2023-01-01T00:00:00Z", result?.createdAt)
    }

    @Test
    fun `updateById should return null when todo not found`() {
        val id = "non-existent-id"
        val content = "Updated content"
        val completed = true
        whenever(dynamoDbClient.updateItem(any<UpdateItemRequest>()))
            .thenThrow(ConditionalCheckFailedException.builder().build())

        val result = todoRepository.updateById(id, content, completed)

        assertNull(result)
    }

    @Test
    fun `deleteById should call deleteItem on the client`() {
        val id = "test-id"

        todoRepository.deleteById(id)

        verify(dynamoDbClient).deleteItem(
            argThat<DeleteItemRequest> {
                tableName() == "Todos" &&
                        key()["id"]?.s() == id
            }
        )
    }
}

package com.jigcar.todoapp.model

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
data class TodoDbRecord(
    @get:DynamoDbPartitionKey
    var id: String = "",
    var content: String = "",
    var completed: Boolean = false,
    var createdAt: String = ""
)

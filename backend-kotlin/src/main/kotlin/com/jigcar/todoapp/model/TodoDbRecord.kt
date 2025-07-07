package com.jigcar.todoapp.model

import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.enhanced.dynamodb.mapper.StaticAttributeTags

data class TodoDbRecord(
    var id: String = "",
    var content: String = "",
    var completed: Boolean = false,
    var createdAt: String = ""
)

val todoTableSchema: TableSchema<TodoDbRecord> = TableSchema.builder(TodoDbRecord::class.java)
    .newItemSupplier { TodoDbRecord() }
    .addAttribute(String::class.java) { a ->
        a.name("id")
            .getter { it.id }
            .setter { obj, v -> obj.id = v }
            .tags(StaticAttributeTags.primaryPartitionKey())
    }
    .addAttribute(String::class.java) { a ->
        a.name("content")
            .getter { it.content }
            .setter { obj, v -> obj.content = v }
    }
    .addAttribute(Boolean::class.javaObjectType) { a ->
        a.name("completed")
            .getter { it.completed }
            .setter { obj, v -> obj.completed = v }
    }
    .addAttribute(String::class.java) { a ->
        a.name("createdAt")
            .getter { it.createdAt }
            .setter { obj, v -> obj.createdAt = v }
    }
    .build()

<template>
  <q-page>
    <div class="row items-center justify-evenly">
      <NewTodoForm @add-todo="addTodo" class="col-12 col-lg-4" />
    </div>
    <TodoList :todos="todos" @delete="deleteTodo" @edit="editTodo" />
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import type { Todo } from 'components/models';
import NewTodoForm from 'components/NewTodoForm.vue';
import TodoList from 'components/TodoList.vue';

const todos = ref<Todo[]>([]);
let todoIdCounter = 0;

function addTodo(newTodo: string): void {
  todos.value.push({
    id: todoIdCounter,
    content: newTodo,
  });
  todoIdCounter += 1;
}

function deleteTodo(id: number): void {
  todos.value = todos.value.filter((todo) => todo.id !== id);
}

function editTodo(id: number, newContent: string) {
  const todo = todos.value.find((t) => t.id === id);
  if (todo) {
    todo.content = newContent;
  }
}
</script>

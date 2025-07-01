<template>
  <q-page>
    <div class="row items-center justify-evenly">
      <NewTodoForm @add-todo="addTodo" class="col-12 col-lg-4" />
    </div>
    <example-component
      title="Example component"
      active
      :todos="todos"
      :meta="meta"
    ></example-component>
  </q-page>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import type { Todo, Meta } from 'components/models';
import ExampleComponent from 'components/ExampleComponent.vue';
import NewTodoForm from 'components/NewTodoForm.vue';

const todos = ref<Todo[]>([]);
let todoIdCounter = 0;

function addTodo(newTodo: string): void {
  todos.value.push({
    id: todoIdCounter,
    content: newTodo,
  });
  todoIdCounter += 1;
}

const meta = ref<Meta>({
  totalCount: 1200,
});
</script>

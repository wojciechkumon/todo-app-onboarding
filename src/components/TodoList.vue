<template>
  <div>
    <h2 class="text-center">Your todos</h2>
    <div class="q-px-md">
      <div class="row items-stretch">
        <TodoItem
          v-for="todo in todos"
          :key="todo.id"
          :todo="todo"
          class="col-3 q-pa-md"
          @delete="emit('delete', todo.id)"
          data-cy="todo-item"
        />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import TodoItem from './TodoItem.vue';
import type { Todo } from 'components/models';

defineProps<{ todos: Todo[] }>();
const emit = defineEmits<{
  (e: 'delete', id: number): void;
}>();
</script>

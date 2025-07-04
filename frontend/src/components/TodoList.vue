<template>
  <div v-if="todos.length === 0" class="text-center text-grey-7">
    <h5>Add your first todo above</h5>
  </div>
  <div v-else>
    <div v-if="activeTodos.length > 0">
      <h2 class="text-center">Your todos</h2>
      <div class="q-px-md">
        <div class="row items-stretch">
          <TodoItem
            v-for="todo in activeTodos"
            :key="todo.id"
            :todo="todo"
            class="col-3 q-pa-md"
            @delete="emit('delete', todo.id)"
            @edit="(newContent: string) => emit('edit', todo.id, newContent)"
            @toggle-complete="emit('toggle-complete', todo.id)"
            data-cy="todo-item"
          />
        </div>
      </div>
    </div>

    <div v-if="completedTodos.length > 0" class="q-mt-md q-pt-xs">
      <h4 class="text-center">Completed todos</h4>
      <div class="q-px-md">
        <div class="row items-stretch">
          <TodoItem
            v-for="todo in completedTodos"
            :key="todo.id"
            :todo="todo"
            class="col-3 q-pa-md"
            @delete="emit('delete', todo.id)"
            @edit="(newContent: string) => emit('edit', todo.id, newContent)"
            @toggle-complete="emit('toggle-complete', todo.id)"
            data-cy="completed-todo-item"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import TodoItem from './TodoItem.vue';
import type { TodoViewModel } from 'components/models';
import { computed } from 'vue';

const props = defineProps<{ todos: TodoViewModel[] }>();
const emit = defineEmits<{
  (e: 'delete', id: string): void;
  (e: 'edit', id: string, newContent: string): void;
  (e: 'toggle-complete', id: string): void;
}>();

const activeTodos = computed(() => props.todos.filter((todo) => !todo.completed));
const completedTodos = computed(() => props.todos.filter((todo) => todo.completed));
</script>

<template>
  <q-page>
    <div class="row items-center justify-evenly q-pt-xl">
      <NewTodoForm @add-todo="addTodo" :is-fetching="isCreatingNewTodo" class="col-12 col-lg-4" />
    </div>
    <TodoList
      :todos="todos"
      @delete="deleteTodo"
      @edit="editTodo"
      @toggle-complete="toggleTodoComplete"
    />
  </q-page>
</template>

<script setup lang="ts">
import { useQuasar } from 'quasar';
import { ref } from 'vue';
import type { Todo } from 'components/models';
import NewTodoForm from 'components/NewTodoForm.vue';
import TodoList from 'components/TodoList.vue';
import { createTodo } from 'src/api/todos';

const todos = ref<Todo[]>([]);
const isCreatingNewTodo = ref<boolean>(false);
let todoIdCounter = 0;
const $q = useQuasar();

async function addTodo(newTodo: string): Promise<void> {
  isCreatingNewTodo.value = true;
  try {
    const todoFromServer = await createTodo(newTodo);
    $q.notify({
      message: 'HTTP response body: ' + JSON.stringify(todoFromServer),
      position: 'bottom-right',
      color: 'green-8',
      textColor: 'white',
      timeout: 5_000,
      actions: [{ label: 'Dismiss', color: 'white' }],
    });
    console.log('todoFromServer', todoFromServer);
  } catch {
    $q.notify({
      message: 'Error while creating a todo, please try again later',
      position: 'bottom-right',
      color: 'red-10',
      textColor: 'white',
      icon: 'warning',
      timeout: 5_000,
      actions: [{ label: 'Dismiss', color: 'white' }],
    });
  } finally {
    isCreatingNewTodo.value = false;
  }
  todos.value.push({
    id: todoIdCounter,
    content: newTodo,
    completed: false,
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

function toggleTodoComplete(id: number) {
  const todo = todos.value.find((t) => t.id === id);
  if (todo) {
    todo.completed = !todo.completed;
  }
}
</script>

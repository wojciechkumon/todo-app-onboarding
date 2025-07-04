<template>
  <q-page>
    <div class="row items-center justify-evenly q-pt-xl">
      <NewTodoForm
        @add-todo="addTodo"
        :is-fetching="isCreatingNewTodo || isFetchingAllTodos"
        class="col-12 col-lg-4"
      />
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
import { onMounted, ref } from 'vue';
import NewTodoForm from 'components/NewTodoForm.vue';
import TodoList from 'components/TodoList.vue';
import type { TodoViewModel } from 'components/models';
import { TodosApi } from 'src/api/todos';
import { showErrorSnackbar } from 'components/snackbars';

const todos = ref<TodoViewModel[]>([]);
const isFetchingAllTodos = ref<boolean>(false);
const isCreatingNewTodo = ref<boolean>(false);

onMounted(async () => {
  isFetchingAllTodos.value = true;
  try {
    const fetchedTodos = await TodosApi.fetchAll();
    fetchedTodos.sort((a, b) => a.createdAt.localeCompare(b.createdAt));
    todos.value = fetchedTodos;
  } catch {
    showErrorSnackbar('Error while fetching all todos, please try again later');
  } finally {
    isFetchingAllTodos.value = false;
  }
});

async function addTodo(newTodo: string): Promise<void> {
  isCreatingNewTodo.value = true;
  try {
    const todoFromServer = await TodosApi.create(newTodo);
    todos.value.push(todoFromServer);
  } catch {
    showErrorSnackbar('Error while creating a todo, please try again later');
  } finally {
    isCreatingNewTodo.value = false;
  }
}

async function deleteTodo(id: string): Promise<void> {
  const todoToDelete = todos.value.find((t) => t.id === id);
  if (!todoToDelete) {
    return;
  }
  todoToDelete.isDuringUpdate = true;
  try {
    await TodosApi.deleteOne(id);
    todos.value = todos.value.filter((todo) => todo.id !== id);
  } catch {
    showErrorSnackbar('Error while deleting a todo, please try again later');
  } finally {
    todoToDelete.isDuringUpdate = false;
  }
}

async function editTodo(id: string, newContent: string) {
  const todoToEdit = todos.value.find((t) => t.id === id);
  if (!todoToEdit) {
    return;
  }
  todoToEdit.isDuringUpdate = true;
  const oldContent = todoToEdit.content;
  try {
    todoToEdit.content = newContent;
    await TodosApi.updateOne(id, { content: newContent, completed: todoToEdit.completed });
  } catch {
    showErrorSnackbar('Error while updating a todo, please try again later');
    todoToEdit.content = oldContent;
  } finally {
    todoToEdit.isDuringUpdate = false;
  }
}

async function toggleTodoComplete(id: string) {
  const todoToEdit = todos.value.find((t) => t.id === id);
  if (!todoToEdit) {
    return;
  }
  todoToEdit.isDuringUpdate = true;
  const newCompletedValue = !todoToEdit.completed;
  try {
    todoToEdit.completed = newCompletedValue;
    await TodosApi.updateOne(id, { content: todoToEdit.content, completed: newCompletedValue });
  } catch {
    showErrorSnackbar('Error while updating a todo, please try again later');
    todoToEdit.completed = !newCompletedValue;
  } finally {
    todoToEdit.isDuringUpdate = false;
  }
}
</script>

<template>
  <div class="q-pa-md">
    <q-input v-model="text" label="New todo item" @keyup.enter="submitTodo" color="amber" />
    <div class="flex justify-center q-mt-sm">
      <q-btn
        label="Add"
        color="amber"
        @click="submitTodo"
        class="q-mt-sm text-black full-width"
        :disabled="!isValidForm()"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const emit = defineEmits<{
  (e: 'add-todo', newTodoValue: string): void;
}>();

const text = ref('');

function isValidForm(): boolean {
  const trimmedValue = text.value.trim();
  return trimmedValue.length > 0;
}

function submitTodo(): void {
  const trimmedValue = text.value.trim();
  if (trimmedValue) {
    emit('add-todo', trimmedValue);
  }
  text.value = '';
}
</script>

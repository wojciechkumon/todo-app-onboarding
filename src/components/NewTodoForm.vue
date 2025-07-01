<template>
  <form class="q-pa-md" @submit.prevent="submitTodo">
    <q-input
      v-model="text"
      ref="todoInput"
      label="New todo item"
      color="amber"
    />
    <div class="flex justify-center q-mt-sm">
      <q-btn
        label="Add"
        type="submit"
        color="amber"
        class="q-mt-sm text-black full-width"
        :disabled="!isValidForm()"
      />
    </div>
  </form>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';

const emit = defineEmits<{
  (e: 'add-todo', newTodoValue: string): void;
}>();

const text = ref('');
const todoInput = ref<HTMLInputElement | null>(null);

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

onMounted(() => todoInput.value?.focus());

</script>

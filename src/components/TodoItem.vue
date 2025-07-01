<template>
  <div>
    <q-item class="custom-border bg-grey-9 q-pa-sm" style="height: 100%">
      <q-item-section>
        {{ todo.content }}
      </q-item-section>
      <q-item-section side>
        <q-btn flat icon="edit" data-cy="edit-btn" />
      </q-item-section>
      <q-item-section side class="no-padding">
        <q-btn flat icon="delete" data-cy="delete-btn" @click="emit('delete')" />
      </q-item-section>
    </q-item>
  </div>
</template>

<script setup lang="ts">
import type { Todo } from 'components/models';

defineProps<{ todo: Todo }>();
const emit = defineEmits<{
  (e: 'delete'): void;
}>();
</script>

<style scoped>
.custom-border {
  border: 1px solid #aaaaaa;
  border-radius: 8px;
}
</style>

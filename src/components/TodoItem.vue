<template>
  <div>
    <q-item class="custom-border bg-grey-9 q-pa-sm" style="height: 100%">
      <template v-if="!isEditing">
        <q-item-section>
          {{ todo.content }}
        </q-item-section>
        <q-item-section side class="no-padding">
          <q-btn flat icon="edit" @click="startEdit" data-cy="edit-btn" class="hover-amber" />
        </q-item-section>
        <q-item-section side class="no-padding">
          <q-btn
            flat
            icon="delete"
            data-cy="delete-btn"
            @click="emit('delete')"
            class="hover-red"
          />
        </q-item-section>
      </template>
      <template v-else>
        <q-item-section>
          <q-input
            v-model="editedText"
            @keyup.esc="cancelEdit"
            filled
            autogrow
            dense
            color="amber"
            data-cy="edit-input"
          />
        </q-item-section>
        <q-item-section side>
          <q-btn
            flat
            icon="done"
            @click="saveEdit"
            data-cy="edit-save-btn"
            class="hover-amber"
            :disabled="!isValidEditForm()"
          />
          <q-btn
            flat
            icon="close"
            color=""
            @click="cancelEdit"
            data-cy="edit-cancel-btn"
            class="hover-red"
          />
        </q-item-section>
      </template>
    </q-item>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import type { Todo } from 'components/models';

const props = defineProps<{ todo: Todo }>();

const emit = defineEmits<{
  (e: 'delete'): void;
  (e: 'edit', newValue: string): void;
}>();

const isEditing = ref(false);
const editedText = ref(props.todo.content);

function startEdit() {
  editedText.value = props.todo.content;
  isEditing.value = true;
}

function isValidEditForm(): boolean {
  const trimmedValue = editedText.value.trim();
  return trimmedValue.length > 0;
}

function saveEdit() {
  const trimmed = editedText.value.trim();
  if (trimmed.length > 0 && trimmed !== props.todo.content) {
    emit('edit', trimmed);
  }
  isEditing.value = false;
}

function cancelEdit() {
  editedText.value = props.todo.content;
  isEditing.value = false;
}
</script>

<style scoped lang="scss">
.custom-border {
  border: 1px solid $grey-6;
  border-radius: 8px;
}

.hover-amber:hover {
  color: $amber;
}

.hover-red:hover {
  color: $red;
}
</style>

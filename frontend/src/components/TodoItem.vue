<template>
  <div>
    <q-item
      class="custom-border q-pa-sm"
      :class="[todo.completed ? 'bg-grey-10 slightly-transparent' : 'bg-grey-9']"
      style="height: 100%"
    >
      <template v-if="!isEditing">
        <q-item-section avatar>
          <q-checkbox
            v-model="isCompleted"
            @update:model-value="emit('toggle-complete')"
            color="green-9"
            class="rounded-checkbox"
            data-cy="todo-checkbox"
            :disable="todo.isDuringUpdate"
          />
        </q-item-section>
        <q-item-section class="col">
          {{ todo.content }}
        </q-item-section>
        <div class="flex items-center">
          <div class="text-right">
            <q-badge v-if="todo.completed" color="green-9" class="text-weight-medium"
              >completed
            </q-badge>
            <div class="flex">
              <q-item-section side class="no-padding">
                <q-btn
                  flat
                  icon="edit"
                  @click="startEdit"
                  data-cy="edit-btn"
                  class="hover-amber"
                  :loading="todo.isDuringUpdate"
                />
              </q-item-section>
              <q-item-section side class="no-padding">
                <q-btn
                  flat
                  icon="delete"
                  data-cy="delete-btn"
                  @click="emit('delete')"
                  class="hover-red"
                  :loading="todo.isDuringUpdate"
                />
              </q-item-section>
            </div>
          </div>
        </div>
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
import type { TodoViewModel } from 'components/models';

const props = defineProps<{ todo: TodoViewModel }>();

const emit = defineEmits<{
  (e: 'delete'): void;
  (e: 'edit', newValue: string): void;
  (e: 'toggle-complete'): void;
}>();

const isEditing = ref(false);
const editedText = ref(props.todo.content);
const isCompleted = ref(props.todo.completed || false);

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

.slightly-transparent {
  opacity: 0.7;
}

.rounded-checkbox ::v-deep(.q-checkbox__bg) {
  border-radius: 50% !important;
}
</style>

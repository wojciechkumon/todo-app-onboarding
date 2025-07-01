<template>
  <q-layout view="lHh Lpr lFf">
    <q-header>
      <q-toolbar class="text-center bg-grey-9">
        <q-toolbar-title class="text-amber-4">Todo List App</q-toolbar-title>
      </q-toolbar>
    </q-header>
    <q-page-container class="bg-grey-10">
      <div :style="dottedStyle">
        <router-view />
      </div>
    </q-page-container>
  </q-layout>
</template>

<script setup lang="ts">
const dottedStyle = {
  backgroundImage: 'radial-gradient(#555555 1px, transparent 1px)',
  backgroundSize: '32px 32px',
};
</script>

<template>
  <q-layout view="lHh Lpr lFf">
    <q-header>
      <q-toolbar class="bg-grey-9">
        <q-toolbar-title class="text-yellow-7 responsive-margin">Todo List App</q-toolbar-title>
      </q-toolbar>
    </q-header>
    <q-page-container class="bg-grey-10">
      <div class="dotted-background">
        <router-view />
      </div>
    </q-page-container>
  </q-layout>
</template>

<style scoped lang="scss">
.responsive-margin {
  margin-left: 15%;
}

@media (max-width: 1439px) {
  .responsive-margin {
    text-align: center;
  }
}

.dotted-background {
  background-image: radial-gradient($grey-9 1px, transparent 1px);
  background-size: 32px 32px;
}
</style>

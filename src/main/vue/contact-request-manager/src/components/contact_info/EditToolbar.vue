<template>
  <div class="btn-group">
    <button
      type="button"
      class="btn shadow-none pr-1 pb-0 pt-0 border-0"
      :disabled="disabled"
      @click="$emit('ok')"
    >
      <i class="fas fa-check" />
    </button>
    <button
      type="button"
      class="btn shadow-none pl-1 pb-0 pt-0 border-0"
      :disabled="disabled"
      @click="$emit('cancel')"
    >
      <i class="fas fa-ban" />
    </button>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  disabled: boolean;
}>();
defineEmits(['cancel', 'ok']);
</script>

<template>
  <div class="d-flex flex-row">
    <!-- eslint-disable -->
    <textarea
      :id="id"
      class="form-control"
      rows="4"
      :readonly="!enabled || !isEditMode"
      :value="comment"
      @blur="cancelEdit"
      @click="startEdit"
      v-on:input="handleValueChanged"
    />
    <div
      v-if="enabled && isEditMode"
      class="d-flex flex-column pl-1"
    >
      <div class="btn-group-vertical">
        <button
          type="button"
          class="btn btn-secondary shadow-none"
          title="Save"
          @mousedown="handleSave"
        >
          <i class="fa fa-check" />
        </button>
        <button
          type="button"
          class="btn btn-secondary shadow-none"
          title="Cancel"
        >
          <i class="fas fa-times" />
        </button>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';

defineProps({
  id: {
    type: String,
    default: '',
  },
  comment: {
    type: String,
    default: '',
  },
  enabled: {
    type: Boolean,
    required: true,
  },
});
const emit = defineEmits(['save']);
const isEditMode = ref(false);
let updateComment = '';
const handleValueChanged = (v) => {
  updateComment = v.target.value;
};
const startEdit = (): void => {
  isEditMode.value = true;
};
const cancelEdit = (): void => {
  isEditMode.value = false;
};
const handleSave = (): void => {
  emit('save', updateComment);
  isEditMode.value = false;
};
</script>

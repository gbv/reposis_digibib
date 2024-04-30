<template>
  <div class="d-flex flex-row">
    <textarea :id="id" class="form-control" rows="4"
      :readonly="!enabled || !isEditMode" @blur="cancelEdit"
      :value="comment" @click="startEdit"  v-on:input="handleValueChanged" />
    <div v-if="enabled && isEditMode" class="d-flex flex-column pl-1">
      <div class="btn-group-vertical">
        <button class="btn btn-secondary shadow-none" @mousedown="handleSave" title="Save">
          <i class="fa fa-check"></i>
        </button>
        <button class="btn btn-secondary shadow-none" title="Cancel">
          <i class="fas fa-times"></i>
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
  },
  comment: {
    type: String,
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

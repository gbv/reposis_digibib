<template>
  <tr>
    <td>
      <input
        v-if="isManualOrigin && isEditing"
        v-model="localContact.name"
        class="form-control form-control-sm"
        :class="nameInvalid ? 'is-invalid' : ''"
        type="text"
      >
      <span v-else>{{ contact.name }}</span>
    </td>
    <td><span>{{ contact.origin }}</span></td>
    <td><span>{{ contact.email }}</span></td>
    <td class="text-center align-middle">
      <div class="btn-group">
        <template v-if="isEditing">
          <EditToolbar
            :disabled="false"
            @ok="handleUpdate"
            @cancel="handleCancelEdit"
          />
        </template>
        <template v-else>
          <button
            type="button"
            class="btn shadow-none pr-1 pb-0 pt-0 border-0"
            :disabled="!isEditEnabled"
            @click="handleStartEdit"
          >
            <i class="fas fa-edit" />
          </button>
          <button
            type="button"
            class="btn shadow-none pl-1 pb-0 pt-0 border-0"
            :disabled="!isDeleteEnabled"
            @click="handleDelete"
          >
            <i class="fas fa-trash" />
          </button>
        </template>
      </div>
    </td>
  </tr>
</template>
<script setup lang="ts">
import {
  computed,
  reactive,
  ref,
  watch,
} from 'vue';
import ContactInfo, { ORIGIN_MANUAL } from '@/model/ContactInfo';
import EditToolbar from './EditToolbar.vue';

const props = defineProps<{
  contact: ContactInfo;
  isEditing: boolean;
  isContactRequestOpened: boolean;
}>();
const emit = defineEmits(['start-edit', 'cancel-edit', 'delete', 'update']);
const nameInvalid = ref<boolean>(false);
const localContact = reactive<ContactInfo>({ ...props.contact });
watch(() => props.contact, (newContact) => {
  Object.assign(localContact, newContact);
}, { deep: true });

const isManualOrigin = computed((): boolean => props.contact.origin === ORIGIN_MANUAL);
const isDeleteEnabled = computed((): boolean => {
  if (props.isEditing) {
    return false;
  }
  return props.isContactRequestOpened;
});
const isEditEnabled = computed((): boolean => {
  if (!isManualOrigin.value) {
    return false;
  }
  return isDeleteEnabled.value;
});
const handleStartEdit = (): void => {
  emit('start-edit', props.contact.id);
};
const handleCancelEdit = (): void => {
  Object.assign(localContact, props.contact);
  emit('cancel-edit');
};
const validate = (): boolean => {
  let valid = true;
  if (!localContact.name || localContact.name.trim() === '') {
    nameInvalid.value = true;
    valid = false;
  } else {
    nameInvalid.value = false;
  }
  return valid;
}
const handleUpdate = (): void => {
  if (validate()) {
    emit('update', localContact);
  }
};
const handleDelete = (): void => {
  emit('delete');
};
</script>

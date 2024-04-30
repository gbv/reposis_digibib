<template>
  <tr class="rowStyle">
    <td>
      <i
        role="button"
        tabindex="0"
        :class="['fa-solid', showAttemptTable ? 'fa-chevron-up' : 'fa-chevron-right']"
        @click="toggleAttempts"
        @keydown="toggleAttempts"
      />
    </td>
    <td colspan="0">
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
            @ok="handleUpdate"
            @cancel="handleCancelEdit"
          />
        </template>
        <template v-else>
          <button
            type="button"
            class="btn shadow-none pr-1 pb-0 pt-0 border-0"
            :disabled="!isContactRequestOpened"
            @click="handleForward"
          >
            <i class="fa fa-envelope" />
          </button>
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
  <tr v-if="showAttemptTable">
    <td colspan="5">
      <div class="p-3">
        <h6>{{ $t('digibib.contactRequest.frontend.manager.contactAttempts.header') }}</h6>
        <attempt-table :attempts="attempts" />
      </div>
    </td>
  </tr>
</template>
<script setup lang="ts">
import {
  PropType,
  computed,
  reactive,
  ref,
  watch,
} from 'vue';
import ContactInfo, { ORIGIN_MANUAL } from '@/model/ContactInfo';
import ContactAttempt from '@/model/ContactAttempt';
import EditToolbar from './EditToolbar.vue';
import AttemptTable from './ContactAttemptTable.vue';

const props = defineProps({
  contact: {
    type: Object as PropType<ContactInfo>,
    required: true,
  },
  isEditing: {
    type: Boolean,
    required: true,
  },
  attempts: {
    type: Object as PropType<Array<ContactAttempt>>,
    required: true,
  },
  isContactRequestOpened: {
    type: Boolean,
    default: false,
  },
});
const emit = defineEmits(['start-edit', 'cancel-edit', 'delete', 'forward', 'update']);
const nameInvalid = ref<boolean>(false);
const localContact = reactive<ContactInfo>({ ...props.contact });
const showAttemptTable = ref<boolean>(false);
const toggleAttempts = (): void => {
  if (showAttemptTable.value) {
    showAttemptTable.value = false;
  } else {
    showAttemptTable.value = true;
  }
};
watch(() => props.contact, (newContact) => {
  Object.assign(localContact, newContact);
}, { deep: true });

const isManualOrigin = computed((): boolean => props.contact.origin === ORIGIN_MANUAL);
const isDeleteEnabled = computed((): boolean => {
  if (props.isEditing) {
    return false;
  }
  if (props.attempts.length > 0) {
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
const handleForward = (): void => {
  emit('forward', props.contact.id);
};
</script>

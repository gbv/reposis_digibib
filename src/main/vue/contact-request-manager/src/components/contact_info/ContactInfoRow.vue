<template>
  <tr class=rowStyle>
    <td>
      <i role="button" @click="toggleAttempts"
        :class="['fa-solid',  showAttemptTable ? 'fa-chevron-up' : 'fa-chevron-right']"></i>
    </td>
    <td colspan="0">
      <input v-if="isManualOrigin && isEditing" class="form-control form-control-sm" type="text"
        v-model="localContact.name" :class="v.name.$error ? 'is-invalid' : ''" />
      <span v-else>{{ contact.name }}</span>
    </td>
    <td><span>{{ contact.origin }}</span></td>
    <td><span>{{ contact.email }}</span></td>
    <td class="text-center align-middle">
      <div class="btn-group">
        <template v-if="isEditing">
          <EditToolbar @ok="handleUpdate" @cancel="handleCancelEdit">
          </EditToolbar>
        </template>
        <template v-else>
          <button class="btn shadow-none pr-1 pb-0 pt-0 border-0" @click="handleForward">
            <i class="fa fa-envelope"></i>
          </button>
          <button class="btn shadow-none pr-1 pb-0 pt-0 border-0" @click="handleStartEdit"
            :disabled="!isEditEnabled">
            <i class="fas fa-edit"></i>
          </button>
          <button class="btn shadow-none pl-1 pb-0 pt-0 border-0" @click="handleDelete"
            :disabled="!isDeleteEnabled">
            <i class="fas fa-trash"></i>
          </button>
        </template>
      </div>
    </td>
  </tr>
  <tr v-if="showAttemptTable">
    <td colspan="5">
      <div class="p-3">
        <h6>{{ $t('digibib.contact.frontend.manager.contactAttempts.header') }}</h6>
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
import useVuelidate from '@vuelidate/core';
import { required, email } from '@vuelidate/validators';
import { ContactInfo, ContactAttempt, ORIGIN_MANUAL } from '@/utils';
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
  isTicketOpened: {
    type: Boolean,
    default: false,
  },
});
const emit = defineEmits(['start-edit', 'cancel-edit', 'delete', 'forward', 'update']);
const rules = computed(() => ({
  name: {
    required,
  },
  email: {
    required,
    email,
  },
  origin: {
    required,
  },
}));
const localContact = reactive({ ...props.contact });
const showAttemptTable = ref(false);
const toggleAttempts = async () => {
  if (showAttemptTable.value) {
    showAttemptTable.value = false;
  } else {
    showAttemptTable.value = true;
  }
};
const v = useVuelidate(rules, localContact);

watch(() => props.contact, (newContact) => {
  Object.assign(localContact, newContact);
}, { deep: true });

const isManualOrigin = computed(() => props.contact.origin === ORIGIN_MANUAL);
const isDeleteEnabled = computed(() => {
  if (props.isEditing) {
    return false;
  }
  return props.isTicketOpened;
});
const isEditEnabled = computed(() => {
  if (!isManualOrigin.value) {
    return false;
  }
  return isDeleteEnabled.value;
});
const handleStartEdit = () => {
  emit('start-edit', props.contact.id);
};
const handleCancelEdit = () => {
  Object.assign(localContact, props.contact);
  emit('cancel-edit');
};
const handleUpdate = () => {
  v.value.$validate();
  if (!v.value.$error) {
    emit('update', localContact);
  }
};
const handleDelete = () => {
  emit('delete');
};
const handleForward = () => {
  emit('forward', props.contact.id);
};
</script>

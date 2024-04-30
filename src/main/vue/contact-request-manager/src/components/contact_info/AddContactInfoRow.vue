<template>
  <tr>
    <td>
      <input
        v-model="name"
        class="form-control form-control-sm"
        :class="nameInvalid ? 'is-invalid' : ''"
        type="text"
        :disabled="disabled"
      >
    </td>
    <td>
      {{ ORIGIN_MANUAL }}
    </td>
    <td>
      <input
        v-model="email"
        class="form-control form-control-sm"
        :class="emailInvalid ? 'is-invalid' : ''"
        type="text"
        :disabled="disabled"
      >
    </td>
    <td class="col-2 text-center align-middle">
      <EditToolbar
        :disabled="disabled"
        @ok="addContact"
        @cancel="reset"
      />
    </td>
  </tr>
</template>
<script setup lang="ts">
import { ref } from 'vue';
import ContactInfo, { ORIGIN_MANUAL } from '@/model/ContactInfo';
import EditToolbar from './EditToolbar.vue';

defineProps<{
  disabled: boolean;
}>();
const name = ref<string>();
const nameInvalid = ref<boolean>(false);
const email = ref<string>();
const emailInvalid = ref<boolean>(false);
const origin = ref(ORIGIN_MANUAL);
const emit = defineEmits(['add']);
const reset = () => {
  name.value = undefined;
  nameInvalid.value = false;
  email.value = undefined;
  emailInvalid.value = false;
};
const validateEmail = (email: string): boolean => {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return re.test(String(email).toLowerCase());
};
const validate = (): boolean => {
  let valid = true;
  if (!name.value || name.value.trim() === '') {
    nameInvalid.value = true;
    valid = false;
  } else {
    nameInvalid.value = false;
  }
  if (!email.value || email.value.trim() === '' || !validateEmail(email.value)) {
    emailInvalid.value = true;
    valid = false;
  } else {
    emailInvalid.value = false;
  }
  return valid;
}
const addContact = async () => {
  if (validate()) {
    emit('add', {
      name: name.value?.trim(),
      email: email.value?.trim(),
      origin: origin.value,
    } as ContactInfo);
    reset();
  }
};
</script>

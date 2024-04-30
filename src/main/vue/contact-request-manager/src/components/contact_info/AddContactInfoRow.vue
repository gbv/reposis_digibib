<template>
  <tr>
    <td class="col-4" colspan="2">
      <input class="form-control form-control-sm" type="text" v-model="name"
        :class="v.name.$error? 'is-invalid' : ''" :disabled="disabled"/>
    </td>
    <td class="col-2">
      {{ ORIGIN_MANUAL }}
    </td>
    <td class="col-4">
      <input class="form-control form-control-sm" type="text" v-model="mail"
        :class="v.mail.$error ? 'is-invalid' : ''" :disabled="disabled" />
    </td>
    <td class="col-2 text-center align-middle">
      <EditToolbar :disabled="disabled" @ok="addContact" @cancel="reset"></EditToolbar>
    </td>
  </tr>
</template>
<script setup lang="ts">
import { computed, ref } from 'vue';
import useVuelidate from '@vuelidate/core';
import { required, email } from '@vuelidate/validators';
import { ContactInfo, ORIGIN_MANUAL } from '@/utils';
import EditToolbar from './EditToolbar.vue';

defineProps({
  disabled: {
    type: Boolean,
    required: true,
  },
});
const name = ref();
const mail = ref();
const origin = ref(ORIGIN_MANUAL);
const emit = defineEmits(['add']);
const rules = computed(() => ({
  name: {
    required,
  },
  mail: {
    required,
    email,
  },
  origin: {
    required,
  },
}));
const v = useVuelidate(rules, { name, mail, origin });
const reset = () => {
  v.value.$reset();
  name.value = null;
  mail.value = null;
};
const addContact = async () => {
  v.value.$validate();
  if (!v.value.$error) {
    emit('add', {
      name: name.value,
      email: mail.value,
      origin: origin.value,
    } as ContactInfo);
    reset();
  }
};
</script>

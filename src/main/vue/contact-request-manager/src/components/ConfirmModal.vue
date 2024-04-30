<template>
  <Modal
    :show="visible"
    hide-header-close
    :title="title"
    :cancel-title="$t('digibib.contactRequest.frontend.manager.confirm.cancelButton')"
    :ok-title="$t('digibib.contactRequest.frontend.manager.confirm.okButton')"
    @ok="ok"
    @cancel="cancel"
  >
    {{ message }}
  </Modal>
</template>
<script setup lang="ts">
import { ref } from 'vue';
import Modal from './Modal.vue';

const visible = ref(false);
const title = ref();
const message = ref(null);
let resolvePromise;
const show = (ops) => {
  title.value = ops.title;
  message.value = ops.message;
  visible.value = true;
  return new Promise((resolve) => {
    resolvePromise = resolve;
  });
};
const ok = () => {
  visible.value = false;
  resolvePromise(true);
};
const cancel = () => {
  visible.value = false;
  resolvePromise(false);
};
defineExpose({ show });
</script>

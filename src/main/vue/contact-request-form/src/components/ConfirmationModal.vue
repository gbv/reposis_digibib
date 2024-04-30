<template>
  <modal @ok="close" @close="close" :title="$t('digibib.contact.frontend.confirmation.title')">
    <p>{{ $t('digibib.contact.frontend.confirmation.text') }}</p>
    <p>{{ $t('digibib.contact.frontend.confirmation.text.help') }}</p>
  </modal>
</template>
<script setup lang="ts">
import Modal from './Modal.vue';

const emit = defineEmits(['close']);
const close = () => emit('close');
</script>

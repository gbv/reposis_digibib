<template>
  <Modal
    :show="showModal"
    hide-header-close
    :title="$t('digibib.contactRequest.frontend.manager.forwardModal.header')"
    ok-only
    @ok="emit('close')"
  >
    {{ $t('digibib.contactRequest.frontend.manager.forwardRequestToContact.forwardInfo') }}
  </Modal>
</template>

<script setup lang="ts">
import Modal from '@/components/Modal.vue';

defineProps<{
  showModal: boolean;
}>();
const emit = defineEmits(['close']);

</script>

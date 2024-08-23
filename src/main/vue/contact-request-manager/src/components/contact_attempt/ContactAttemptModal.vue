<template>
  <Modal
    v-if="contactAttempts"
    :show="showModal"
    :title="$t('digibib.contactRequest.frontend.manager.viewAttemptsModal.header')"
    size="xl"
    ok-only
    scrollable
    @close="emit('close')"
    @ok="emit('close')"
  >
    <div class="container">
      <p>{{ $t('digibib.contactRequest.frontend.manager.viewAttemptsModal.info') }}</p>
      <ContactAttemptTable :contact-attempts="contactAttempts" />
    </div>
  </Modal>
</template>

<script setup lang="ts">
import Modal from '@/components/Modal.vue';
import ContactAttemptTable from './ContactAttemptTable.vue';
import ContactAttempt from '@/model/ContactAttempt';

withDefaults(
  defineProps<{
    showModal?: boolean;
    contactAttempts?: ContactAttempt[] | null;
  }>(),
  {
    showModal: false,
    contactAttempts: () => [],
  }
);
const emit = defineEmits(['close']);
</script>

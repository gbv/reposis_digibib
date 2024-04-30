<template>
  <div>
    <Teleport to="#btnContact" append>
      <a class="dropdown-item" role="menuitem" tabindex="-1" @click="showFormModal = true">
        {{ $t('digibib.contact.frontend.button.contact') }}
      </a>
    </Teleport>
    <transition name="modal" appear>
      <confirmation-modal v-if="showConfirmationModal" @close="showConfirmationModal = false" />
    </transition>
    <transition name="modal" appear>
      <form-modal :objectId="objectId" :baseUrl="baseUrl" v-if="showFormModal"
          @close="showFormModal = false" @success="handleSuccess" />
    </transition>
  </div>
</template>
<script setup lang="ts">
import { ref } from 'vue';
import ConfirmationModal from './components/ConfirmationModal.vue';
import FormModal from './components/FormModal.vue';

defineProps({
  baseUrl: {
    type: String,
    required: true,
  },
  objectId: {
    type: String,
    required: true,
  },
});
const showConfirmationModal = ref(false);
const showFormModal = ref(false);
const handleSuccess = () => {
  showFormModal.value = false;
  showConfirmationModal.value = true;
};
</script>
<style>
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.3s ease;
}
.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}
</style>

<template>
  <Modal
    v-if="contactRequest && contactInfos"
    :show="showModal"
    :title="$t('digibib.contactRequest.frontend.manager.manageContactsModal.header')"
    size="xl"
    ok-only
    scrollable
    @close="emit('close')"
    @ok="emit('close')"
  >
    <div class="container">
      <div
        v-if="errorCode"
        class="alert alert-danger"
        role="alert"
      >
        {{ $t(`digibib.contactRequest.frontend.manager.error.${errorCode}`) }}
      </div>
      <p>{{ $t('digibib.contactRequest.frontend.manager.manageContactsModal.info') }}</p>
      <ContactInfoTable
        :contact-request-id="contactRequest.id"
        :contact-request-status="contactRequest.status"
        :contact-infos="contactInfos"
        @create-contact-info="createContactInfo"
        @update-contact-info="updateContactInfo"
        @delete-contact-info="deleteContactInfo"
      />
    </div>
  </Modal>
</template>

<script setup lang="ts">
import { ref, onErrorCaptured } from 'vue';
import Modal from '@/components/Modal.vue';
import ContactRequest from '@/model/ContactRequest';
import ContactInfo from '@/model/ContactInfo';
import ContactRequestService from '@/api/service';
import ContactInfoTable from './ContactInfoTable.vue';

const props = withDefaults(
  defineProps<{
    showModal?: boolean;
    contactRequest?: ContactRequest;
    contactInfos?: ContactInfo[] | null;
  }>(),
  {
    showModal: false,
    contactRequest: undefined,
    contactInfos: () => [],
  }
);
const emit = defineEmits(['close', 'created', 'updated', 'deleted']);
const errorCode = ref<string>();
const handleError = (error: unknown): void => {
  errorCode.value = error instanceof Error ? error.message : 'unknown';
};
const resetError = (): void => {
  errorCode.value = undefined;
};
const createContactInfo = async (contactInfo: ContactInfo): Promise<void> => {
  if (props.contactRequest) {
    resetError();
    const contactInfoId = await ContactRequestService.createContactInfo(
      props.contactRequest.id,
      contactInfo
    );
    const createdContactInfo = await ContactRequestService.getContactInfo(
      props.contactRequest.id,
      contactInfoId
    );
    emit('created', createdContactInfo);
  }
};
const updateContactInfo = async (contactInfo: ContactInfo): Promise<void> => {
  if (props.contactRequest && props.contactInfos) {
    resetError();
    await ContactRequestService.patchContactInfoName(
      props.contactRequest.id,
      contactInfo.id, contactInfo.name
    );
    const updatedContactInfo = await ContactRequestService.getContactInfo(
      props.contactRequest.id,
      contactInfo.id
    );
    emit('updated', updatedContactInfo);
  }
};
const deleteContactInfo = async (contactInfoId: string): Promise<void> => {
  if (props.contactRequest) {
    resetError();
    await ContactRequestService.removeContactInfo(props.contactRequest.id, contactInfoId);
    emit('deleted', contactInfoId);
  }
};
onErrorCaptured((error: unknown) => {
  handleError(error);
  return false;
});
</script>

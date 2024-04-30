<template>
  <Modal
    v-if="contactRequest"
    :show="showModal"
    :title="$t('digibib.contactRequest.frontend.manager.forwardRequestModal.header')"
    size="xl"
    ok-only
    scrollable
    @close="emit('close')"
    @ok="emit('close')"
  >
    <div class="container-fluid">
      <div
        v-if="errorCode"
        class="alert alert-danger"
        role="alert"
      >
        {{ $t(`digibib.contactRequest.frontend.manager.error.${errorCode}`) }}
      </div>
      <p>{{ $t('digibib.contactRequest.frontend.manager.forwardRequestModal.info') }}</p>
      <ContactInfoTable
        :contact-request-id="contactRequest.id"
        :contact-request-status="contactRequest.status"
        :contact-infos="contactInfos"
        :contact-attemps="contactAttempts"
        @update-contact-info="handleUpdateContactInfo"
        @delete-contact-info="handleDeleteContactInfo"
        @create-contact-info="handleCreateContactInfo"
        @forward-request="handleForwardRequest"
      />
    </div>
  </Modal>
</template>

<script setup lang="ts">
import {
  PropType,
  watch,
  ref,
  onErrorCaptured,
} from 'vue';
import Modal from '@/components/Modal.vue';
import ContactRequest from '@/model/ContactRequest';
import ContactInfo from '@/model/ContactInfo';
import {
  createContactInfo,
  getContactAttemps,
  getContactInfos,
  getContactInfo,
  patchContactInfoName,
  removeContactInfo,
  forwardContactRequest,
} from '@/api/service';
import ContactInfoTable from './ContactInfoTable.vue';

const props = defineProps({
  showModal: {
    type: Boolean,
    default: false,
  },
  contactRequest: {
    type: Object as PropType<ContactRequest>,
    default() {
      return undefined;
    },
  },
});
const emit = defineEmits(['close']);
const contactInfos = ref();
const errorCode = ref<string>();
const contactAttempts = ref();
const handleError = (error: unknown): void => {
  errorCode.value = error instanceof Error ? error.message : 'unknown';
};
const resetError = (): void => {
  errorCode.value = undefined;
};
watch(() => props.showModal, async (val) => {
  if (val) {
    const contactRequestId = props.contactRequest?.id;
    if (contactRequestId) {
      try {
        contactInfos.value = (await getContactInfos(contactRequestId)).data;
        contactAttempts.value = (await getContactAttemps(contactRequestId)).data;
      } catch (error) {
        handleError(error);
      }
    }
  }
});
const handleUpdateContactInfo = async (contactInfo: ContactInfo): Promise<void> => {
  if (props.contactRequest) {
    resetError();
    const contactInfoIndex = contactInfos.value
      .findIndex((c: ContactInfo) => contactInfo.id === c.id);
    await patchContactInfoName(props.contactRequest.id, contactInfo.id, contactInfo.name);
    const updatedContactInfo = (await getContactInfo(props.contactRequest.id, contactInfo.id)).data;
    contactInfos.value[contactInfoIndex] = updatedContactInfo;
  }
};
const handleDeleteContactInfo = async (contactInfoId: string): Promise<void> => {
  if (props.contactRequest) {
    resetError();
    await removeContactInfo(props.contactRequest.id, contactInfoId);
    contactInfos.value = contactInfos.value.filter((c: ContactInfo) => c.id !== contactInfoId);
  }
};
const handleCreateContactInfo = async (contactInfo: ContactInfo): Promise<void> => {
  if (props.contactRequest) {
    resetError();
    const response = await createContactInfo(props.contactRequest.id, contactInfo);
    const contactInfoId = response.headers.location.split('/').pop() as string;
    const createdContactInfo = (await getContactInfo(props.contactRequest.id, contactInfoId)).data;
    contactInfos.value.push(createdContactInfo);
  }
};
const handleForwardRequest = async (contactInfoId: string): Promise<void> => {
  if (props.contactRequest) {
    resetError();
    const contactInfoIndex = contactInfos.value
      .findIndex((c: ContactInfo) => contactInfoId === c.id);
    await forwardContactRequest(props.contactRequest.id, contactInfoId);
    const updatedContactInfo = (await getContactInfo(props.contactRequest.id, contactInfoId)).data;
    contactInfos.value[contactInfoIndex] = updatedContactInfo;
  }
};
onErrorCaptured((error: unknown) => {
  handleError(error);
  return false;
});
</script>

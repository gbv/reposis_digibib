<template>
  <Modal v-if="ticket" :show="showModal"
    :title="$t('digibib.contact.frontend.manager.forwardRequestModal.header')"
    size="xl" ok-only scrollable @close="emit('close')" okTitle="Close" @ok="emit('close')">
    <div class="container-fluid">
      <div v-if="errorCode" class="alert alert-danger" role="alert">
        {{ $t(`digibib.contact.frontend.manager.error.${errorCode}`) }}
      </div>
      <div v-if="infoCode" class="alert alert-success" role="alert">
        {{ $t(`digibib.contact.frontend.manager.info.${infoCode}`) }}
      </div>
      <p>{{ $t('digibib.contact.frontend.manager.forwardRequestModal.info') }}</p>
      <ContactInfoTable :ticketId="ticket.id" :ticketStatus=ticket.status
        :contactInfos="contactInfos" :contact-attemps="contactAttempts"
        @update-contact-info="handleUpdateContactInfo"
        @delete-contact-info="handleDeleteContactInfo"
        @create-contact-info="handleCreateContactInfo"
        @forward-request="handleForwardRequest" />
    </div>
  </Modal>
</template>

<script setup lang="ts">
import { PropType, watch, ref } from 'vue';
import Modal from '@/components/Modal.vue';
import { ContactInfo, Ticket } from '@/utils';
import {
  createContactInfo,
  getContactAttemps,
  getContactInfos,
  getContactInfo,
  patchContactInfoName,
  removeContactInfo,
  forwardRequest,
} from '@/api/service';
import ContactInfoTable from './ContactInfoTable.vue';

const props = defineProps({
  showModal: {
    type: Boolean,
    default: false,
  },
  ticket: {
    type: Object as PropType<Ticket>,
  },
});

const emit = defineEmits(['close']);

const contactInfos = ref();
const errorCode = ref();
const infoCode = ref();
const contactAttempts = ref();

const handleError = (error): void => {
  errorCode.value = error instanceof Error ? error.message : 'unknown';
};

const resetError = (): void => {
  errorCode.value = undefined;
};
watch(() => props.showModal, async (val) => {
  if (val) {
    const ticketId = props.ticket?.id;
    if (ticketId) {
      try {
        contactInfos.value = (await getContactInfos(ticketId)).data;
        contactAttempts.value = (await getContactAttemps(ticketId)).data;
      } catch (error) {
        handleError(error);
      }
    }
  }
});
const handleUpdateContactInfo = async (contactInfo: ContactInfo): Promise<void> => {
  if (props.ticket) {
    resetError();
    const contactInfoIndex = contactInfos.value
      .findIndex((c: ContactInfo) => contactInfo.id === c.id);
    try {
      await patchContactInfoName(props.ticket.id, contactInfo.id, contactInfo.name);
      const updatedContactInfo = (await getContactInfo(props.ticket.id, contactInfo.id)).data;
      contactInfos.value[contactInfoIndex] = updatedContactInfo;
    } catch (error) {
      handleError(error);
    }
  }
};
const handleDeleteContactInfo = async (contactInfoId: string): Promise<void> => {
  if (props.ticket) {
    resetError();
    try {
      await removeContactInfo(props.ticket.id, contactInfoId);
      contactInfos.value = contactInfos.value.filter((c: ContactInfo) => c.id !== contactInfoId);
    } catch (error) {
      handleError(error);
    }
  }
};
const handleCreateContactInfo = async (contactInfo: ContactInfo): Promise<void> => {
  if (props.ticket) {
    resetError();
    try {
      const response = await createContactInfo(props.ticket.id, contactInfo);
      const contactInfoId = response.headers.location.split('/').pop() as string;
      const createdContactInfo = (await getContactInfo(props.ticket.id, contactInfoId)).data;
      contactInfos.value.push(createdContactInfo);
    } catch (error) {
      handleError(error);
    }
  }
};
const handleForwardRequest = async (contactInfoId: string): Promise<void> => {
  if (props.ticket) {
    resetError();
    const contactInfoIndex = contactInfos.value
      .findIndex((c: ContactInfo) => contactInfoId === c.id);
    try {
      await forwardRequest(props.ticket.id, contactInfoId);
      const updatedContactInfo = (await getContactInfo(props.ticket.id, contactInfoId)).data;
      contactInfos.value[contactInfoIndex] = updatedContactInfo;
    } catch (error) {
      handleError(error);
    }
  }
};
</script>

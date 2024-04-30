<template>
  <table class="table">
    <colgroup>
      <col style="width: 3%">
      <col style="width: 30%">
      <col style="width: 10%">
      <col style="width: 47%">
      <col style="width: 10%">
    </colgroup>
    <thead>
      <tr>
        <th></th>
        <th>{{ $t('digibib.contact.frontend.manager.forwardRequest.name') }}</th>
        <th>{{ $t('digibib.contact.frontend.manager.forwardRequest.origin') }}</th>
        <th>{{ $t('digibib.contact.frontend.manager.forwardRequest.email') }}</th>
        <th></th>
      </tr>
    </thead>
    <tbody>
      <template v-for="(contactInfo, index) in contactInfos" :key="index">
        <ContactInfoRow :contact="contactInfo" :attempts="getAttempsForContactId(contactInfo.id)"
          :is-editing="editingRowIndex == index" :is-ticket-opened="isTicketOpened"
          @start-edit="handleStartEdit(index)" @cancel-edit="handleCancelEdit"
          @delete="handleDeleteContactInfo(contactInfo)" @update="handleUpdateContactInfo"
          @forward="handleForwardRequest(contactInfo)" />
      </template>
      <AddContactInfoRow v-if="ticketStatus === TicketStatus.OPEN"
        :disabled="editingRowIndex !== null" @add="handleCreateContactInfo" />
    </tbody>
  </table>
  <ConfirmModal ref="confirmModal" />
</template>

<script setup lang="ts">
import { computed, PropType, ref } from 'vue';
import { useI18n } from 'vue-i18n';
import { ContactAttempt, ContactInfo, TicketStatus } from '@/utils';
import ConfirmModal from '@/components/ConfirmModal.vue';
import AddContactInfoRow from './AddContactInfoRow.vue';
import ContactInfoRow from './ContactInfoRow.vue';

const props = defineProps({
  ticketId: {
    type: String,
    required: true,
  },
  ticketStatus: {
    type: Number,
  },
  contactAttemps: {
    type: Object as PropType<Array<ContactAttempt>>,
  },
  contactInfos: {
    type: Object as PropType<Array<ContactInfo>>,
  },
});
const emit = defineEmits(['create-contact-info', 'delete-contact-info', 'update-contact-info',
  'forward-request']);
const { t } = useI18n();
const confirmModal = ref();
const editingRowIndex = ref<number | null>(null);
const isTicketOpened = computed((): boolean => props.ticketStatus === TicketStatus.OPEN);

const getAttempsForContactId = (contactInfoId: string) => {
  if (props.contactAttemps) {
    return props.contactAttemps.filter((c: ContactAttempt) => contactInfoId === c.contactInfoId);
  }
  return [];
};
const handleStartEdit = (index: number): void => {
  editingRowIndex.value = index;
};
const handleCancelEdit = (): void => {
  editingRowIndex.value = null;
};
const handleCreateContactInfo = (contactInfo: ContactInfo): void => {
  emit('create-contact-info', contactInfo);
};
const handleUpdateContactInfo = (contactInfo: ContactInfo): void => {
  emit('update-contact-info', contactInfo);
  editingRowIndex.value = null;
};
const handleDeleteContactInfo = async (contactInfo: ContactInfo): Promise<void> => {
  const ok = await confirmModal.value.show({
    title: t('digibib.contact.frontend.manager.deleteContactInfo.header'),
    message: t('digibib.contact.frontend.manager.deleteContactInfo.info', {
      email: contactInfo.email,
    }),
  });
  if (ok) {
    emit('delete-contact-info', contactInfo.id);
  }
};
const handleForwardRequest = async (contactInfo: ContactInfo): Promise<void> => {
  if (props.contactInfos) {
    const attempts = getAttempsForContactId(contactInfo.id);
    if (attempts.length === 0) {
      emit('forward-request', contactInfo.id);
    } else {
      const ok = await confirmModal.value.show({
        title: t('digibib.contact.frontend.manager.forwardRequestToContact.header'),
        message: t('digibib.contact.frontend.manager.forwardRequestToContact.info', {
          email: contactInfo.email,
        }),
      });
      if (ok) {
        emit('forward-request', contactInfo.id);
      }
    }
  }
};
</script>

<template>
  <table class="table table-striped ">
    <colgroup>
      <col style="width: 35%">
      <col style="width: 10%">
      <col style="width: 45%">
      <col style="width: 10%">
    </colgroup>
    <thead>
      <tr>
        <th>{{ $t('digibib.contactRequest.frontend.manager.contactTable.name') }}</th>
        <th>{{ $t('digibib.contactRequest.frontend.manager.contactTable.origin') }}</th>
        <th>{{ $t('digibib.contactRequest.frontend.manager.contactTable.email') }}</th>
        <th />
      </tr>
    </thead>
    <tbody>
      <template
        v-for="(contactInfo, index) in contactInfos"
        :key="index"
      >
        <ContactInfoRow
          :contact="contactInfo"
          :is-editing="editingRowIndex === index"
          :is-contact-request-opened="isContactRequestOpened"
          @start-edit="handleStartEdit(index)"
          @cancel-edit="handleCancelEdit"
          @delete="handleDeleteContactInfo(contactInfo)"
          @update="handleUpdateContactInfo"
          @forward="handleForwardRequest(contactInfo)"
        />
      </template>
      <AddContactInfoRow
        v-if="contactRequestStatus === ContactRequestStatus.OPEN"
        :disabled="editingRowIndex !== null"
        @add="handleCreateContactInfo"
      />
    </tbody>
  </table>
  <ConfirmModal ref="confirmModal" />
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { useI18n } from 'vue-i18n';

import { ContactRequestStatus } from '@/model/ContactRequest';
import ContactInfo from '@/model/ContactInfo';
import ConfirmModal from '@/components/ConfirmModal.vue';
import AddContactInfoRow from './AddContactInfoRow.vue';
import ContactInfoRow from './ContactInfoRow.vue';

const props = defineProps<{
  contactRequestId: string;
  contactRequestStatus: ContactRequestStatus;
  contactInfos: ContactInfo[];
}>();

const emit = defineEmits(['create-contact-info', 'delete-contact-info', 'update-contact-info',
  'forward-request']);
const { t } = useI18n();
const confirmModal = ref();
const editingRowIndex = ref<number | null>(null);
const isContactRequestOpened = computed(
  (): boolean =>props.contactRequestStatus === ContactRequestStatus.OPEN
);
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
    title: t('digibib.contactRequest.frontend.manager.deleteContactInfo.header'),
    message: t('digibib.contactRequest.frontend.manager.deleteContactInfo.info', {
      email: contactInfo.email,
    }),
  });
  if (ok) {
    emit('delete-contact-info', contactInfo.id);
  }
};
const handleForwardRequest = async (contactInfo: ContactInfo): Promise<void> => {
  if (props.contactInfos) {
    const ok = await confirmModal.value.show({
      title: t('digibib.contactRequest.frontend.manager.forwardRequestToContact.header'),
      message: t('digibib.contactRequest.frontend.manager.forwardRequestToContact.info', {
        email: contactInfo.email,
      }),
    });
    if (ok) {
      emit('forward-request', contactInfo.id);
    }
  }
};
</script>

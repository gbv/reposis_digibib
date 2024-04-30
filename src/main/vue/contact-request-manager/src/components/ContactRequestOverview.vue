<template>
  <div class="row">
    <div class="col-3">
      <ListGroup>
        <template
          v-for="(contactRequest, index) in contactRequests"
          :key="index"
        >
          <ContactRequestListGroupItem
            :contact-request="contactRequest"
            :active="activeIndex === index"
            @click="handleActiveIndexChanged(index)"
          />
        </template>
      </ListGroup>
    </div>
    <div class="col-9 border-left">
      <template v-if="currentContactRequest">
        <div class="row mb-2 border-bottom">
          <div class="col-8">
            <h3>{{ currentContactRequest.id }}</h3>
          </div>
          <div class="col-4">
            <div class="d-flex">
              <Dropdown :text="getStatusText(ContactRequestStatus[currentContactRequest.status])">
                <DropdownItem
                  v-if="currentContactRequest.status !== ContactRequestStatus.OPEN"
                  @click="handleStatusChanged(ContactRequestStatus.OPEN)"
                >
                  {{ t('digibib.contactRequest.frontend.manager.status.open') }}
                </DropdownItem>
                <DropdownItem
                  v-if="currentContactRequest.status !== ContactRequestStatus.CLOSED"
                  @click="handleStatusChanged(ContactRequestStatus.CLOSED)"
                >
                  {{ t('digibib.contactRequest.frontend.manager.status.closed') }}
                </DropdownItem>
              </Dropdown>
              <Dropdown
                class="pl-2"
                :text="t('digibib.contactRequest.frontend.manager.actions')"
              >
                <DropdownItem @click="openContactInfoModal">
                  {{ t('digibib.contactRequest.frontend.manager.actions.forwardRequest') }}
                </DropdownItem>
                <DropdownItem @click="deleteCurrentContactRequest">
                  {{ t('digibib.contactRequest.frontend.manager.actions.deleteRequest') }}
                </DropdownItem>
              </Dropdown>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-8">
            <div class="row">
              <div class="col-12">
                <div class="form-group">
                  <label for="messageTextArea">
                    {{ t('digibib.contactRequest.frontend.manager.message') }}
                  </label>
                  <textarea
                    id="messageTextArea"
                    class="form-control"
                    rows="8"
                    readonly
                    :value="currentContactRequest.body.message"
                  />
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-12">
                <hr class="my-2">
              </div>
            </div>
            <div class="row">
              <div class="col-12">
                <div class="form-group">
                  <label for="inputComment">
                    {{ $t('digibib.contactRequest.frontend.manager.comment') }}
                  </label>
                  <EditTextArea
                    id="inputComment"
                    :comment="currentContactRequest.comment"
                    :enabled="currentContactRequest.status === ContactRequestStatus.OPEN"
                    @save="handleCommentSave"
                  />
                </div>
              </div>
            </div>
          </div>
          <div class="col-4">
            <div class="row">
              <div class="col-12">
                <ContactRequestPersonCard :contact-request="currentContactRequest" />
              </div>
            </div>
            <div class="row mt-2">
              <div class="col-12">
                <ContactRequestDetailCard :contact-request="currentContactRequest" />
              </div>
            </div>
          </div>
        </div>
      </template>
    </div>
  </div>
  <ContactInfoModal
    :show-modal="showContactInfoModal"
    :contact-request="currentContactRequest"
    @close="closeContactInfoModal()"
  />
  <ConfirmModal ref="confirmModal" />
</template>

<script setup lang="ts">
import { PropType, ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import ContactInfoModal from '@/components/contact_info/ContactInfoModal.vue';
import EditTextArea from '@/components/EditTextArea.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import ContactRequestDetailCard from '@/components/ContactRequestDetailCard.vue';
import ContactRequestPersonCard from '@/components/ContactRequestPersonCard.vue';
import ListGroup from '@/components/ListGroup.vue';
import ContactRequestListGroupItem from '@/components/ContactRequestListGroupItem.vue';
import Dropdown from '@/components/Dropdown.vue';
import DropdownItem from '@/components/DropdownItem.vue';
import ContactRequest, { ContactRequestStatus } from '@/model/ContactRequest';

const props = defineProps({
  contactRequests: {
    type: Object as PropType<Array<ContactRequest>>,
    default() {
      return [];
    },
  },
});

const emit = defineEmits(['update-contact-request-comment', 'update-contact-request-status', 'remove-contact-request']);
const { t } = useI18n();
const currentContactRequest = ref<ContactRequest>();
const showContactInfoModal = ref(false);
const confirmModal = ref();
const activeIndex = ref(0);
const getStatusText = (status: string) => t(`digibib.contactRequest.frontend.manager.status.${status.toString().toLowerCase()}`);
watch(() => props.contactRequests, (contactRequests: ContactRequest[] | undefined) => {
  if (contactRequests && contactRequests.length > 0) {
    // eslint-disable-next-line prefer-destructuring
    currentContactRequest.value = contactRequests[0];
  }
});
const handleActiveIndexChanged = (index: number) => {
  if (props.contactRequests && index >= 0) {
    activeIndex.value = index;
    currentContactRequest.value = props.contactRequests[index];
  } else {
    currentContactRequest.value = undefined;
  }
};
const openContactInfoModal = () => {
  showContactInfoModal.value = true;
};
const closeContactInfoModal = () => {
  showContactInfoModal.value = false;
};
const handleStatusChanged = (status: ContactRequestStatus) => {
  if (currentContactRequest.value) {
    emit('update-contact-request-status', currentContactRequest.value.id, status);
  }
};
const handleCommentSave = (comment: string) => {
  if (currentContactRequest.value) {
    emit('update-contact-request-comment', currentContactRequest.value.id, comment);
  }
};
const deleteCurrentContactRequest = async () => {
  if (currentContactRequest.value) {
    const ok = await confirmModal.value.show({
      title: t('digibib.contactRequest.frontend.manager.deleteContactRequest.header'),
      message: t('digibib.contactRequest.frontend.manager.deleteContactRequest.info', {
        requestId: currentContactRequest.value.id,
      }),
    });
    if (ok) {
      emit('remove-contact-request', currentContactRequest.value.id);
    }
  }
};

</script>
<style>
#overview {
  max-height: 480px;
}
</style>

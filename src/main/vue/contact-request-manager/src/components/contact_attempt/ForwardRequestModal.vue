<template>
  <Modal
    v-if="contactRequest && contactInfos !== null && contactAttempts !== null"
    :show="showModal"
    :title="$t('digibib.contactRequest.frontend.manager.forwardModal.header')"
    size="lg"
    ok-only
    :busy="isBusy"
    scrollable
    @close="emit('close')"
  >
    <div class="container">
      <div
        v-if="errorCode"
        class="alert alert-danger"
        role="alert"
      >
        {{ $t(`digibib.contactRequest.frontend.manager.error.${errorCode}`) }}
      </div>
      <p>{{ $t('digibib.contactRequest.frontend.manager.forwardModal.info') }}</p>
      <form>
        <div class="form-group">
          <label for="recipientInput">
            {{ $t('digibib.contactRequest.frontend.manager.forwardModal.recipient') }}
          </label>
          <select
            id="recipientInput"
            v-model="selectedContactInfo"
            class="form-control"
          >
            <option
              value=""
              disabled
            >
              {{ $t('digibib.contactRequest.frontend.manager.forwardModal.selectOption') }}
            </option>
            <option
              v-for="(contactInfo, index) in contactInfos"
              :key="index"
              :value="contactInfo.id"
            >
              {{ `${contactInfo.name} (${contactInfo.email})` }}
            </option>
          </select>
        </div>
        <div class="form-group">
          <label for="commentTextArea">
            {{ $t('digibib.contactRequest.frontend.manager.comment') }}
          </label>
          <textarea
            id="commentTextArea"
            v-model="comment"
            class="form-control"
            rows="4"
          />
        </div>
      </form>
    </div>
    <template #footer>
      <button
        class="btn btn-primary"
        :disabled="selectedContactInfo === ''"
        @click="forwardRequest"
      >
        <span
          v-if="isBusy"
          class="spinner-border spinner-border-sm"
          role="status"
          aria-hidden="true"
        />
        {{ $t('digibib.contactRequest.frontend.manager.forwardModal.forward') }}
      </button>
    </template>
  </Modal>
  <ConfirmModal ref="confirmModal" />
</template>

<script setup lang="ts">
import { ref, onErrorCaptured } from 'vue';
import { useI18n } from 'vue-i18n';
import Modal from '@/components/Modal.vue';
import ContactRequest from '@/model/ContactRequest';
import ContactInfo from '@/model/ContactInfo';
import ContactRequestService, { CreateContactAttemptDto } from '@/api/service';
import ContactAttempt from '@/model/ContactAttempt';
import ConfirmModal from '@/components/ConfirmModal.vue';

const props = withDefaults(
  defineProps<{
    showModal?: boolean;
    contactRequest?: ContactRequest;
    contactInfos?: ContactInfo[] | null;
    contactAttempts?: ContactAttempt[] | null;
  }>(),
  {
    showModal: false,
    contactRequest: undefined,
    contactInfos: () => null,
    contactAttempts: () => null,
  }
);
const emit = defineEmits(['close', 'forwarded']);
const { t } = useI18n();
const confirmModal = ref();
const errorCode = ref<string>();
const selectedContactInfo = ref<string>("");
const comment = ref<string>();
const isBusy = ref<boolean>(false);
const resetModal = () => {
  selectedContactInfo.value = "";
  comment.value = "";
}
const handleError = (error: unknown): void => {
  errorCode.value = error instanceof Error ? error.message : 'unknown';
};
const resetError = (): void => {
  errorCode.value = undefined;
};
const checkAlreadyForwarded = (contactInfo: ContactInfo) => {
  if (props.contactAttempts) {
    return props.contactAttempts.filter(
      (a: ContactAttempt) => contactInfo.email === a.recipientReference
    ).length > 0;
  }
}
const doForwardRequest = async (
  contactRequestId: string,
  contactAttemptDto: CreateContactAttemptDto
) => {
  isBusy.value = true;
  resetError();
  try {
    const contactAttemptId: string = await ContactRequestService.createContactAttempt(
      contactRequestId,
      contactAttemptDto
    );
    const contactAttempt: ContactAttempt = await ContactRequestService.getContactAttemptById(
      contactRequestId,
      contactAttemptId
    );
    emit('forwarded', contactAttempt);
    emit('close');
    resetModal();
  } finally {
    isBusy.value = false;
  }
}
const forwardRequest = async (): Promise<void> => {
  if (props.contactRequest &&
    selectedContactInfo.value !== undefined &&
    props.contactInfos &&
    props.contactAttempts
  ) {
    const contactInfoIndex: number = props.contactInfos.findIndex(
      (c: ContactInfo) => selectedContactInfo.value === c.id
    );
    const contactInfo: ContactInfo = props.contactInfos[contactInfoIndex];
    const contactAttemptDto: CreateContactAttemptDto = {
      type: "EMAIL",
      recipientName: contactInfo.name,
      recipientReference: contactInfo.email,
    };
    if (comment.value !== '') {
      contactAttemptDto.comment = comment.value;
    }
    if (checkAlreadyForwarded(contactInfo)) {
      const ok = await confirmModal.value.show({
        title: t('digibib.contactRequest.frontend.manager.forwardRequestToContact.header'),
        message: t('digibib.contactRequest.frontend.manager.forwardRequestToContact.info', {
          email: props.contactInfos[contactInfoIndex].email,
        }),
      });
      if (ok) {
        doForwardRequest(props.contactRequest.id, contactAttemptDto);
      }
    } else {
      doForwardRequest(props.contactRequest.id, contactAttemptDto);
    }
  }
};
onErrorCaptured((error: unknown) => {
  handleError(error);
  return false;
});
</script>

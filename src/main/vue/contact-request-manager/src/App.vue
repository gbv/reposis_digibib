<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-12">
        <h2>{{ $t('digibib.contactRequest.frontend.manager.header') }}</h2>
      </div>
    </div>
    <div
      v-if="loading"
      class="overlay"
    >
      <div class="d-flex justify-content-center">
        <div
          class="spinner-grow text-primary"
          role="status"
          style="width: 3rem; height: 3rem; z-index: 20;"
        >
          <span class="sr-only">Loading...</span>
        </div>
      </div>
    </div>
    <div
      v-if="errorCode"
      class="row"
    >
      <div class="col-12">
        <div
          class="alert alert-danger text-center"
          role="alert"
        >
          {{ $t(`digibib.contactRequest.frontend.manager.error.${errorCode}`) }}
        </div>
      </div>
    </div>
    <template v-if="isBooted && errorCode !== 'unauthorizedError'">
      <div class="row">
        <div class="col">
          <ContactRequestOverview
            :contact-requests="contactRequests"
            @remove-contact-request="removeContactRequest"
            @update-contact-request-comment="updateContactRequestComment"
            @update-contact-request-status="updateContactRequestStatus"
          />
        </div>
      </div>
    </template>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, onErrorCaptured } from 'vue';
import axios from 'axios';
import ContactRequestService, { PaginatedContactRequests } from '@/api/service';
import ContactRequestOverview from '@/components/ContactRequestOverview.vue';
import ContactRequest, { ContactRequestStatus } from '@/model/ContactRequest';

const errorCode = ref<string>();
const loading = ref(true);
const isBooted = ref(false);
const totalCount = ref(0);
const contactRequests = ref<Array<ContactRequest>>([]);
const handleError = (error: unknown) => {
  errorCode.value = error instanceof Error ? error.message : 'unknown';
};
const fetch = async () => {
  const response: PaginatedContactRequests = await ContactRequestService
    .getAllContactRequests(0, 1024);
  contactRequests.value = response.contactRequests;
  totalCount.value = response.total;
};
onMounted(async () => {
  let authError = false;
  if (process.env.NODE_ENV === 'development') {
    axios.defaults.headers.common.Authorization = `Basic ${process.env.VUE_APP_API_TOKEN}`;
  } else {
    try {
      const jwtResponse = await axios.get('rsc/jwt');
      const jwtToken = jwtResponse.data.access_token;
      axios.defaults.headers.common.Authorization = `Bearer ${jwtToken}`;
    } catch (error) {
      authError = true;
    }
  }

  if (authError) {
    errorCode.value = 'unknown';
  }
  isBooted.value = true;
  loading.value = false;
  try {
    await fetch();
  } catch (error) {
    handleError(error);
  }
});
const updateContactRequestComment = async (contactRequestId: string, comment: string) => {
  await ContactRequestService.patchContactRequestCommentById(contactRequestId, comment);
  const updatedContactRequest = await ContactRequestService.getContactRequestById(contactRequestId);
  const contactRequestIndex = contactRequests.value.findIndex(
    (t: ContactRequest) => t.id === contactRequestId
  );
  if (contactRequestIndex >= 0) {
    Object.assign(contactRequests.value[contactRequestIndex], updatedContactRequest);
  }
};
const updateContactRequestStatus = async (
  contactRequestId: string,
  status: ContactRequestStatus
) => {
  await ContactRequestService.patchContactRequestStatusById(contactRequestId, status);
  const updatedContactRequest = await ContactRequestService.getContactRequestById(contactRequestId);
  const contactRequestIndex = contactRequests.value.findIndex(
    (t: ContactRequest) => t.id === contactRequestId
  );
  if (contactRequestIndex >= 0) {
    Object.assign(contactRequests.value[contactRequestIndex], updatedContactRequest);
  }
};
const removeContactRequest = async (contactRequestId: string) => {
  await ContactRequestService.removeContactRequestById(contactRequestId);
  contactRequests.value = contactRequests.value.filter(
    (t: ContactRequest) => t.id !== contactRequestId
  );
};
onErrorCaptured((error: unknown) => {
  handleError(error);
  return false;
});
</script>
<style scoped>
.overlay {
  position: fixed;
  width: 100%;
  height: 100%;
  z-index: 1000;
  top: 40%;
  left: 0px;
  opacity: 0.5;
  filter: alpha(opacity=50);
}
</style>

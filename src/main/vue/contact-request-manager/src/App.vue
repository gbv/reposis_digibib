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
            @remove-contact-request="handleRemoveContactRequest"
            @update-contact-request-comment="handleUpdateContactRequestComment"
            @update-contact-request-status="handleUpdateContactRequestStatus"
          />
        </div>
      </div>
    </template>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted, onErrorCaptured } from 'vue';
import axios from 'axios';
import {
  getContactRequests,
  getContactRequestById,
  removeContactRequestById,
  patchContactRequestCommentById,
  patchContactRequestStatusById,
} from '@/api/service';
import ContactRequestOverview from '@/components/ContactRequestOverview.vue';
import ContactRequest from '@/model/ContactRequest';

const errorCode = ref<string>();
const loading = ref(true);
const isBooted = ref(false);
const totalCount = ref(0);
const contactRequests = ref<Array<ContactRequest>>([]);
const handleError = (error: unknown) => {
  errorCode.value = error instanceof Error ? error.message : 'unknown';
};
const fetch = async () => {
  const response = await getContactRequests(0, 1024);
  contactRequests.value = response.data;
  totalCount.value = Number(response.headers['x-total-count']);
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
    console.log('D');
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
const handleUpdateContactRequestComment = async (contactRequestId: string, comment: string) => {
  await patchContactRequestCommentById(contactRequestId, comment);
  const updatedContactRequest = (await getContactRequestById(contactRequestId)).data;
  const contactRequestIndex = contactRequests.value.findIndex((t: ContactRequest) => t.id === contactRequestId);
  if (contactRequestIndex >= 0) {
    Object.assign(contactRequests.value[contactRequestIndex], updatedContactRequest);
  }
};
const handleUpdateContactRequestStatus = async (contactRequestId: string, status: number) => {
  await patchContactRequestStatusById(contactRequestId, status);
  const updatedContactRequest = (await getContactRequestById(contactRequestId)).data;
  const contactRequestIndex = contactRequests.value.findIndex((t: ContactRequest) => t.id === contactRequestId);
  if (contactRequestIndex >= 0) {
    Object.assign(contactRequests.value[contactRequestIndex], updatedContactRequest);
  }
};
const handleRemoveContactRequest = async (contactRequestId: string) => {
  await removeContactRequestById(contactRequestId);
  contactRequests.value = contactRequests.value.filter((t: ContactRequest) => t.id !== contactRequestId);
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

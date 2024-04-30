<template>
  <div class="container-fluid">
    <div class="row">
      <div class="col-12">
        <h2>{{ $t('digibib.contact.frontend.manager.header') }}</h2>
      </div>
    </div>
    <div v-if="loading" class="overlay">
      <div class="d-flex justify-content-center">
        <div class="spinner-grow text-primary" role="status"
            style="width: 3rem; height: 3rem; z-index: 20;">
          <span class="sr-only">Loading...</span>
        </div>
      </div>
    </div>
    <div v-if="errorCode" class="row">
      <div class="col-12">
        <div class="alert alert-danger text-center" role="alert">
          {{ $t(`digibib.contact.frontend.manager.error.${errorCode}`) }}
        </div>
      </div>
    </div>
    <template v-if="isBooted && errorCode !== 'unauthorizedError'">
      <div class="row">
        <div class="col">
          <Tickets :tickets="tickets" @remove-ticket="handleRemoveTicket"
            @update-ticket-comment="handleUpdateTicketComment"
            @update-ticket-status="handleUpdateTicketStatus" />
        </div>
      </div>
    </template>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from 'vue';
import axios from 'axios';
import {
  getTickets,
  getTicket,
  removeTicket,
  patchTicketComment,
  patchTicketStatus,
} from '@/api/service';
import Tickets from '@/components/Tickets.vue';
import { Ticket } from '@/utils';

const errorCode = ref();
const loading = ref(true);
const isBooted = ref(false);

const totalCount = ref(0);
const tickets = ref();

const handleError = (error) => {
  errorCode.value = error instanceof Error ? error.message : 'unknown';
};

const fetch = async () => {
  const response = await getTickets(0, 1024);
  tickets.value = response.data;
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

const handleUpdateTicketComment = async (ticketId: string, comment: string) => {
  try {
    await patchTicketComment(ticketId, comment);
    const updatedTicket = (await getTicket(ticketId)).data;
    const ticketIndex = tickets.value.findIndex((t: Ticket) => t.id === ticketId);
    if (ticketIndex >= 0) {
      Object.assign(tickets.value[ticketIndex], updatedTicket);
    }
  } catch (error) {
    handleError(error);
  }
};

const handleUpdateTicketStatus = async (ticketId: string, status: number) => {
  try {
    await patchTicketStatus(ticketId, status);
    const updatedTicket = (await getTicket(ticketId)).data;
    const ticketIndex = tickets.value.findIndex((t: Ticket) => t.id === ticketId);
    if (ticketIndex >= 0) {
      Object.assign(tickets.value[ticketIndex], updatedTicket);
    }
  } catch (error) {
    handleError(error);
  }
};

const handleRemoveTicket = async (ticketId) => {
  try {
    await removeTicket(ticketId);
    tickets.value = tickets.value.filter((t: Ticket) => t.id !== ticketId);
  } catch (error) {
    handleError(error);
  }
};

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

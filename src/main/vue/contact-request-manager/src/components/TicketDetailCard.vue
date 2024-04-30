<template>
  <div v-if="ticket" class="card">
    <div class="card-header">{{ $t('digibib.contact.frontend.manager.detailCard.header') }}</div>
    <div class="card-body">
      <dl>
        <dt>{{ $t('digibib.contact.frontend.manager.detailCard.reference') }}</dt>
        <dd>
          <a :href="`/receive/${ticket.objectId}`" target="_blank">{{ ticket.objectId }}</a>
        </dd>
        <dt>{{ $t('digibib.contact.frontend.manager.detailCard.created') }}</dt>
        <dd>{{ getFormatedDate(new Date(ticket.created)) }}</dd>
        <dt>{{ $t('digibib.contact.frontend.manager.detailCard.createdBy') }}</dt>
        <dd>{{ ticket.createdBy }}</dd>
      </dl>
    </div>
  </div>
</template>

<script setup lang="ts">
import { PropType } from 'vue';
import { Ticket, getFormatedDate } from '@/utils';

defineProps({
  ticket: {
    type: Object as PropType<Ticket>,
  },
});

</script>

<template>
  <div v-if="ticket" class="card">
    <div class="card-header">
      {{ $t('digibib.contact.frontend.manager.personCard.header') }}
    </div>
    <div class="card-body">
      <dl>
        <dt>{{ $t('digibib.contact.frontend.manager.personCard.name') }}</dt>
        <dd>{{ ticket.request.name }}</dd>
        <dt>{{ $t('digibib.contact.frontend.manager.personCard.email') }}</dt>
        <dd>{{ ticket.request.email }}</dd>
        <template v-if="ticket.request.orcid">
          <dt>{{ $t('digibib.contact.frontend.manager.personCard.orcid') }}</dt>
          <dd>{{ ticket.request.orcid }}</dd>
        </template>
      </dl>
    </div>
  </div>
</template>

<script setup lang="ts">
import { PropType } from 'vue';
import { Ticket } from '@/utils';

defineProps({
  ticket: {
    type: Object as PropType<Ticket>,
  },
});

</script>

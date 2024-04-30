<template>
  <div
    v-if="contactRequest"
    class="card"
  >
    <div class="card-header">
      {{ $t('digibib.contactRequest.frontend.manager.personCard.header') }}
    </div>
    <div class="card-body">
      <dl>
        <dt>{{ $t('digibib.contactRequest.frontend.manager.personCard.name') }}</dt>
        <dd>{{ contactRequest.body.name }}</dd>
        <dt>{{ $t('digibib.contactRequest.frontend.manager.personCard.email') }}</dt>
        <dd>{{ contactRequest.body.email }}</dd>
        <template v-if="contactRequest.body.orcid">
          <dt>{{ $t('digibib.contactRequest.frontend.manager.personCard.orcid') }}</dt>
          <dd>{{ contactRequest.body.orcid }}</dd>
        </template>
      </dl>
    </div>
  </div>
</template>

<script setup lang="ts">
import { PropType } from 'vue';
import ContactRequest from '@/model/ContactRequest';

defineProps({
  contactRequest: {
    type: Object as PropType<ContactRequest>,
    default() {
      return undefined;
    },
  },
});
</script>

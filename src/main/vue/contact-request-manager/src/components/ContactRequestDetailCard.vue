<template>
  <div
    v-if="contactRequest"
    class="card"
  >
    <div class="card-header">
      {{ $t('digibib.contactRequest.frontend.manager.detailCard.header') }}
    </div>
    <div class="card-body">
      <dl>
        <dt>{{ $t('digibib.contactRequest.frontend.manager.detailCard.reference') }}</dt>
        <dd>
          <a
            :href="`/receive/${contactRequest.objectId}`"
            target="_blank"
            rel="noopener noreferrer"
          >{{ contactRequest.objectId }}</a>
        </dd>
        <dt>{{ $t('digibib.contactRequest.frontend.manager.detailCard.created') }}</dt>
        <dd>{{ getFormatedDate(new Date(contactRequest.created)) }}</dd>
        <dt>{{ $t('digibib.contactRequest.frontend.manager.detailCard.createdBy') }}</dt>
        <dd>{{ contactRequest.createdBy }}</dd>
      </dl>
    </div>
  </div>
</template>

<script setup lang="ts">
import { getFormatedDate } from '@/utils';
import ContactRequest from '@/model/ContactRequest';

withDefaults(defineProps<{
  contactRequest?: ContactRequest;
}>(), {
  contactRequest: undefined,
});
</script>

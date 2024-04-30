<template>
  <table class="table table-sm">
    <colgroup>
      <col style="width: 20%">
      <col style="width: 20%">
      <col style="width: 60%">
    </colgroup>
    <thead>
      <tr>
        <th>{{ $t('digibib.contact.frontend.manager.contactAttempts.sendDate') }}</th>
        <th>{{ $t('digibib.contact.frontend.manager.contactAttempts.errorDate') }}</th>
        <th>{{ $t('digibib.contact.frontend.manager.contactAttempts.confirmationDate') }}</th>
      </tr>
    </thead>
    <tbody>
      <tr v-for="(attempt, key) in attempts" :key='key'>
        <td>{{ attempt.sendDate ? getFormatedDate(new Date(attempt.sendDate)) : '-' }}</td>
        <td>{{ attempt.errorDate ? getFormatedDate(new Date(attempt.errorDate)) : '-' }}</td>
        <td>{{ attempt.confirmationDate
        ? getFormatedDate(new Date(attempt.confirmationDate)) : '-' }}</td>
      </tr>
    </tbody>
  </table>
</template>
<script setup lang="ts">
import { PropType } from 'vue';
import { ContactAttempt, getFormatedDate } from '@/utils';

defineProps({
  attempts: {
    type: Object as PropType<ContactAttempt[]>,
    required: true,
  },
});
</script>

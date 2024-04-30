<template>
  <table class="table-sm">
    <colgroup>
      <col style="width: 33%">
      <col style="width: 33%">
      <col style="width: 34%">
    </colgroup>
    <thead>
      <tr>
        <th>{{ $t('digibib.contactRequest.frontend.manager.contactAttempts.sendDate') }}</th>
        <th>{{ $t('digibib.contactRequest.frontend.manager.contactAttempts.errorDate') }}</th>
        <th>{{ $t('digibib.contactRequest.frontend.manager.contactAttempts.confirmationDate') }}</th>
      </tr>
    </thead>
    <tbody>
      <tr
        v-for="(attempt, key) in attempts"
        :key="key"
      >
        <td>{{ attempt.sendDate ? getFormatedDate(new Date(attempt.sendDate)) : '-' }}</td>
        <td>{{ attempt.errorDate ? getFormatedDate(new Date(attempt.errorDate)) : '-' }}</td>
        <td>{{ attempt.successDate ? getFormatedDate(new Date(attempt.successDate)) : '-' }}</td>
      </tr>
    </tbody>
  </table>
</template>
<script setup lang="ts">
import { PropType } from 'vue';
import ContactAttempt from '@/model/ContactAttempt';
import { getFormatedDate } from '@/utils';

defineProps({
  attempts: {
    type: Object as PropType<ContactAttempt[]>,
    required: true,
  },
});
</script>

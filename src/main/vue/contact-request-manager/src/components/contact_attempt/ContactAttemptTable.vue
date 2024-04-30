<template>
  <table class="table table-striped ">
    <colgroup>
      <col style="width: 25%">
      <col style="width: 30%">
      <col style="width: 15%">
      <col style="width: 15%">
      <col style="width: 15%">
    </colgroup>
    <thead>
      <tr>
        <th>{{ $t('digibib.contactRequest.frontend.manager.contactAttempts.recipientName') }}</th>
        <th>
          {{ $t('digibib.contactRequest.frontend.manager.contactAttempts.recipientReference') }}
        </th>
        <th>{{ $t('digibib.contactRequest.frontend.manager.contactAttempts.sendDate') }}</th>
        <th>{{ $t('digibib.contactRequest.frontend.manager.contactAttempts.errorDate') }}</th>
        <th>
          {{ $t('digibib.contactRequest.frontend.manager.contactAttempts.confirmationDate') }}
        </th>
      </tr>
    </thead>
    <tbody>
      <tr
        v-for="(attempt, key) in contactAttempts"
        :key="key"
      >
        <td>{{ attempt.recipientName }}</td>
        <td>{{ attempt.recipientReference }}</td>
        <td>{{ attempt.sendDate ? getFormatedDate(new Date(attempt.sendDate)) : '-' }}</td>
        <td>{{ attempt.errorDate ? getFormatedDate(new Date(attempt.errorDate)) : '-' }}</td>
        <td>{{ attempt.successDate ? getFormatedDate(new Date(attempt.successDate)) : '-' }}</td>
      </tr>
    </tbody>
  </table>
</template>
<script setup lang="ts">
import ContactAttempt from '@/model/ContactAttempt';
import { getFormatedDate } from '@/utils';

defineProps<{
  contactAttempts: ContactAttempt[];
}>();
</script>

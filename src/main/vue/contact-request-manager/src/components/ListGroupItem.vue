<template>
  <a class="list-group-item list-group-item-action" href="#"
    :class="[active ? 'active' : '']" @click="emit('item-clicked')">
    <div class="d-flex w-100 justify-content-between">
      <p class="mb-1">{{ ticket.id }}</p>
      <small> {{ getAgeInDaysInfo(getAgeInDays(new Date(ticket.created))) }} </small>
    </div>
    <div>{{ ticket.objectId }}</div>
    <small>{{ `${ticket.request.name} (${ticket.request.email})` }}</small>
  </a>
</template>

<script setup lang="ts">
import { PropType } from 'vue';
import { Ticket } from '@/utils';
import { useI18n } from 'vue-i18n';

const { t } = useI18n();

defineProps({
  active: {
    type: Boolean,
    required: false,
  },
  ticket: {
    type: Object as PropType<Ticket>,
    required: true,
  },
});

const getAgeInDays = (date: Date) => {
  const diff = new Date().getTime() - date.getTime();
  return Math.round(diff / (1000 * 3600 * 24));
};

const getAgeInDaysInfo = (days: number) => {
  if (days > 0) {
    return t('digibib.contact.frontend.manager.beforeDays', {
      days: String(days),
    });
  }
  return t('digibib.contact.frontend.manager.new');
};

const emit = defineEmits(['item-clicked']);
</script>

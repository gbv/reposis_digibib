<template>
  <a
    class="list-group-item list-group-item-action"
    :class="[active ? 'active' : '']"
    href="#"
    @click="emit('item-clicked')"
  >
    <div class="d-flex w-100 justify-content-between">
      <p class="mb-1">{{ contactRequest.id }}</p>
      <small> {{ getAgeInDaysInfo(getAgeInDays(new Date(contactRequest.created))) }} </small>
    </div>
    <div>{{ contactRequest.objectId }}</div>
    <small>{{ `${contactRequest.body.name} (${contactRequest.body.email})` }}</small>
  </a>
</template>

<script setup lang="ts">
import { PropType } from 'vue';
import { useI18n } from 'vue-i18n';
import ContactRequest from '@/model/ContactRequest';

const { t } = useI18n();
defineProps({
  active: {
    type: Boolean,
    required: false,
  },
  contactRequest: {
    type: Object as PropType<ContactRequest>,
    required: true,
  },
});
const emit = defineEmits(['item-clicked']);
const getAgeInDays = (date: Date) => {
  const diff = new Date().getTime() - date.getTime();
  return Math.round(diff / (1000 * 3600 * 24));
};
const getAgeInDaysInfo = (days: number) => {
  if (days > 0) {
    return t('digibib.contactRequest.frontend.manager.beforeDays', {
      days: String(days),
    });
  }
  return t('digibib.contactRequest.frontend.manager.new');
};
</script>

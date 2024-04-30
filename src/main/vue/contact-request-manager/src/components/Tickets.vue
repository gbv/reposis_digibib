<template>
  <div class="row">
    <div class="col-3">
      <ListGroup>
        <template v-for="(ticket, index) in tickets" :key="index">
          <ListGroupItem :ticket="ticket" :active="activeIndex === index"
            @click="handleActiveIndexChanged(index)">
          </ListGroupItem>
        </template>
      </ListGroup>
    </div>
    <div class="col-9 border-left">
      <template v-if="currentTicket">
        <div class="row mb-2 border-bottom">
          <div class="col-8">
            <h3>{{ currentTicket.id }}</h3>
          </div>
          <div class="col-4">
            <div class="d-flex">
              <Dropdown :text="getStatusText(TicketStatus[currentTicket.status])">
                <DropdownItem v-if="currentTicket.status !== TicketStatus.OPEN"
                  @click="handleStatusChanged(TicketStatus.OPEN)">
                  {{ t('digibib.contact.frontend.manager.status.open') }}
                </DropdownItem>
                <DropdownItem v-if="currentTicket.status !== TicketStatus.CLOSED"
                  @click="handleStatusChanged(TicketStatus.CLOSED)">
                  {{ t('digibib.contact.frontend.manager.status.closed') }}
                </DropdownItem>
              </Dropdown>
              <Dropdown class="pl-2" :text="t('digibib.contact.frontend.manager.actions')">
                <DropdownItem @click="openContactInfoModal">
                  {{ t('digibib.contact.frontend.manager.actions.forwardRequest') }}
                </DropdownItem>
                <DropdownItem @click="deleteCurrentTicket">
                  {{ t('digibib.contact.frontend.manager.actions.deleteRequest') }}
                </DropdownItem>
              </Dropdown>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-8">
            <div class="row">
              <div class="col-12">
                <div class="form-group">
                  <label for="messageTextArea">
                    {{ t('digibib.contact.frontend.manager.message')}}
                  </label>
                  <textarea class="form-control" rows="8" readonly id="messageTextArea"
                    :value="currentTicket.request.message"></textarea>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col-12">
                <hr class="my-2" />
              </div>
            </div>
            <div class="row">
              <div class="col-12">
                <div class="form-group">
                  <label for="inputComment">
                    {{ $t('digibib.contact.frontend.manager.comment') }}
                  </label>
                  <EditTextArea id="inputComment" :comment="currentTicket.comment"
                    :enabled="currentTicket.status === TicketStatus.OPEN"
                    @save="handleCommentSave"></EditTextArea>
                </div>
              </div>
            </div>
          </div>
          <div class="col-4">
            <div class="row">
              <div class="col-12">
                <TicketPersonCard :ticket="currentTicket"></TicketPersonCard>
              </div>
            </div>
            <div class="row mt-2">
              <div class="col-12">
                <TicketDetailCard :ticket="currentTicket"></TicketDetailCard>
              </div>
            </div>
          </div>
        </div>
      </template>
    </div>
  </div>
  <ContactInfoModal :showModal="showContactInfoModal" :ticket="currentTicket"
    @close="closeContactInfoModal()" />
  <ConfirmModal ref="confirmModal" />
</template>

<script setup lang="ts">
import {
  PropType,
  computed,
  ref,
  watch,
} from 'vue';
import ContactInfoModal from '@/components/contact_info/ContactInfoModal.vue';
import EditTextArea from '@/components/EditTextArea.vue';
import ConfirmModal from '@/components/ConfirmModal.vue';
import TicketDetailCard from '@/components/TicketDetailCard.vue';
import TicketPersonCard from '@/components/TicketPersonCard.vue';
import ListGroup from '@/components/ListGroup.vue';
import ListGroupItem from '@/components/ListGroupItem.vue';
import Dropdown from '@/components/Dropdown.vue';
import DropdownItem from '@/components/DropdownItem.vue';
import { Ticket, TicketStatus } from '@/utils';
import { useI18n } from 'vue-i18n';

const props = defineProps({
  tickets: {
    type: Object as PropType<Array<Ticket>>,
  },
});

const emit = defineEmits(['update-ticket-comment', 'update-ticket-status', 'remove-ticket',
  'error']);
const { t } = useI18n();

const currentTicket = ref();
const showContactInfoModal = ref(false);
const confirmModal = ref();
const activeIndex = ref(0);
const getStatusText = (status: string) => t(`digibib.contact.frontend.manager.status.${status.toString().toLowerCase()}`);

watch(() => props.tickets, (tickets: Ticket[] | undefined) => {
  if (tickets && tickets.length > 0) {
    // eslint-disable-next-line prefer-destructuring
    currentTicket.value = tickets[0];
  }
});

const handleActiveIndexChanged = (index: number) => {
  if (props.tickets && index >= 0) {
    activeIndex.value = index;
    currentTicket.value = props.tickets[index];
  } else {
    currentTicket.value = undefined;
  }
};

const openContactInfoModal = () => {
  showContactInfoModal.value = true;
};

const closeContactInfoModal = () => {
  showContactInfoModal.value = false;
};

const handleStatusChanged = (status: TicketStatus) => {
  emit('update-ticket-status', currentTicket.value.id, status);
};

const handleCommentSave = (comment: string) => {
  emit('update-ticket-comment', currentTicket.value.id, comment);
};

const deleteCurrentTicket = async () => {
  const ok = await confirmModal.value.show({
    title: t('digibib.contact.frontend.manager.deleteTicket.header'),
    message: t('digibib.contact.frontend.manager.deleteTicket.info', {
      ticketId: currentTicket.value.id,
    }),
  });
  if (ok) {
    emit('remove-ticket', currentTicket.value.id);
  }
};

</script>

<style>
#overview {
  max-height: 480px;
}
</style>

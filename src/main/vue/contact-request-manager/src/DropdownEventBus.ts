import { ref } from 'vue';

const currentOpenDropdown = ref<string | null>(null);

const EventBus = {
  currentOpenDropdown,
  setOpenDropdown(id: string | null) {
    currentOpenDropdown.value = id;
  },
};

export default EventBus;

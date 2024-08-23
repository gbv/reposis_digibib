<template>
  <div
    ref="dropdown"
    class="dropdown"
  >
    <button
      class="btn btn-sm btn-secondary dropdown-toggle"
      type="button"
      :aria-expanded="isOpen"
      @click.stop="toggleDropdown"
    >
      {{ text }}
    </button>
    <ul
      class="dropdown-menu"
      :class="{ show: isOpen }"
    >
      <slot />
    </ul>
  </div>
</template>

<script setup lang="ts">
import { v4 as uuidv4 } from 'uuid';
import {
  onBeforeUnmount,
  onMounted,
  ref,
  watch,
} from 'vue';
import DropdownEventBus from '@/DropdownEventBus';

defineProps<{
  text: string;
}>();

const dropdown = ref<HTMLDivElement | null>(null);
const isOpen = ref(false);
const id = uuidv4();
const toggleDropdown = () => {
  if (isOpen.value) {
    isOpen.value = false;
    DropdownEventBus.setOpenDropdown(null);
  } else {
    isOpen.value = true;
    DropdownEventBus.setOpenDropdown(id);
  }
};
const closeDropdown = () => {
  isOpen.value = false;
  DropdownEventBus.setOpenDropdown(null);
};
const handleOutsideClick = () => {
  if (dropdown.value) {
    closeDropdown();
  }
};
onMounted(() => {
  document.addEventListener('click', handleOutsideClick);
  watch(DropdownEventBus.currentOpenDropdown, (newVal) => {
    if (newVal !== id) {
      isOpen.value = false;
    }
  });
});
onBeforeUnmount(() => {
  document.removeEventListener('click', handleOutsideClick);
});

</script>

<template>
  <div class="d-flex flex-column float-right" style="width: 225px">
    <div class="d-flex flex-row">
      <img style="width: 200px; height: auto;" :src="captchaUrl"
          alt="graphical access code" />
      <div class="d-flex flex-column justify-content-md-center"
          style="width: 20px; margin-left: 5px;">
        <i @click="shuffleCaptcha" class="fas fa-redo" aria-hidden="true"></i>
        <!--<i class="fas fa-volume-up "></i> TODO-->
      </div>
    </div>
    <div class="pt-2">
      <input type="text" v-model="captchaSecret" class="form-control-sm form-control w-100"
          v-on:input="captchaState = null" :class="captchaState === false ? 'is-invalid' : ''" />
    </div>
  </div>
</template>
<script setup lang="ts">
import { onMounted, ref } from 'vue';

const props = defineProps({
  baseUrl: {
    type: String,
    required: true,
  },
});
const captchaSecret = ref('');
const captchaState = ref();
const captchaUrl = ref('');
const shuffleCaptcha = () => {
  captchaUrl.value = `${props.baseUrl}?${Date.now()}`;
  captchaSecret.value = '';
};
onMounted(() => {
  shuffleCaptcha();
});
const verifyCaptcha = async () => {
  if (captchaSecret.value.length === 0) {
    captchaState.value = false;
    return null;
  }
  const response = await fetch(`${props.baseUrl}/userverify`, {
    method: 'POST',
    headers: {
      'Content-Type': 'text/plain',
    },
    body: captchaSecret.value,
  });
  if (response.ok) {
    const result = await response.json();
    if (result.verified === true && result.verifiedToken.length !== 0) {
      return result.verifiedToken;
    }
  }
  captchaState.value = false;
  shuffleCaptcha();
  return null;
};
defineExpose({
  verifyCaptcha,
});
</script>

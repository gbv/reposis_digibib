<template>
  <modal :title="$t('digibib.contact.frontend.form.title')" scrollable @close="emit('close')"
      :busy="busy" size="lg">
    <div class="container-fluid">
      <div class="row">
        <div class="col-12">
          <div v-if="showError" class="alert alert-danger" role="alert">
            {{ $t('digibib.contact.frontend.form.error') }}
          </div>
        </div>
      </div>
      <div class="row pb-4">
        <div class="col-12">{{ $t('digibib.contact.frontend.form.text') }}</div>
      </div>
      <form @submit.stop.prevent="handleSubmit">
        <div class="form-row">
          <div class="col-6">
            <div class="form-group required">
              <label class="control-label" for="nameInput">
                {{ $t('digibib.contact.frontend.form.label.name') }}
              </label>
              <input type="text" class="form-control" id="nameInput" :disabled="busy"
                :class="v.name.$error ? 'is-invalid' : ''" v-model="name" />
              <div class="invalid-feedback" :class="v.name.$error ? 'd-block' : ''">
                {{ $t('digibib.contact.frontend.form.validation.name') }}
              </div>
            </div>
          </div>
          <div class="col-6">
            <div class="form-group required">
              <label class="control-label" for="mailInput">
                {{ $t('digibib.contact.frontend.form.label.email') }}
              </label>
              <input type="email" class="form-control" id="mailInput" :disabled="busy"
                :class="v.mail.$error ? 'is-invalid' : ''" v-model="mail" />
              <div class="invalid-feedback" :class="v.mail.$error ? 'd-block' : ''">
                {{ $t('digibib.contact.frontend.form.validation.email') }}
              </div>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-12">
            <div class="form-group">
              <label for="orcidInput">
                {{ $t('digibib.contact.frontend.form.label.orcid') }}
              </label>
              <input type="text" class="form-control" id="orcidInput" :disabled="busy"
                :class="v.orcid.$error ? 'is-invalid' : ''" v-model="orcid" />
              <div class="invalid-feedback" :class="v.orcid.$error ? 'd-block' : ''">
                {{ $t('digibib.contact.frontend.form.validation.orcid') }}
              </div>
            </div>
          </div>
        </div>
        <div class="row">
          <div class="col-12">
            <div class="form-group required">
              <label class="control-label" for="messageInput">
                {{ $t('digibib.contact.frontend.form.label.message') }}
              </label>
              <textarea type="text" class="form-control" id="messageInput" rows="8"
                :class="v.message.$error ? 'is-invalid' : ''" v-model="message" :disabled="busy"
                required />
              <div class="invalid-feedback" :class="v.message.$error ? 'd-block' : ''">
                {{ $t('digibib.contact.frontend.form.validation.message') }}
              </div>
            </div>
          </div>
        </div>
        <input id="website" name="website" type="text" v-model="website"  />
      </form>
      <div class="row">
        <div class="col-12">
          <div class="form-check required">
            <input class="form-check-input" type="checkbox" v-model="terms" :disabled="busy"
              id="termsCheck" :class="v.terms.$error ? 'is-invalid' : ''">
            <label class="control-label form-check-label" for="termsCheck">
              {{ $t('digibib.contact.frontend.form.label.acceptTerms') }}
            </label>
          </div>
        </div>
      </div>
      <div class="row pt-4">
        <div class="col">
          <cage-captcha ref="captcha" :baseUrl="baseUrl + 'rsc/captchaCage'"/>
        </div>
      </div>
      <span class="small font-weight-light" id="required-label">
       {{ $t('digibib.contact.frontend.form.label.required') }}
      </span>
    </div>
    <template v-slot:footer>
      <button type="button" class="btn btn-primary" @click="handleSubmit" :disabled="busy">
        <span v-if="busy" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
        Absenden
      </button>
    </template>
  </modal>
</template>
<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import validateOrcid from '@/utils';
import useVuelidate from '@vuelidate/core';
import { required, email as validMail, helpers } from '@vuelidate/validators';
import Modal from './Modal.vue';
import CageCaptcha from './CageCaptcha.vue';

const props = defineProps({
  baseUrl: {
    type: String,
    required,
  },
  objectId: {
    type: String,
    required,
  },
});

const emit = defineEmits(['close', 'success']);
const busy = ref(false);
const showError = ref(false);
const name = ref();
const orcid = ref();
const mail = ref();
const message = ref();
const terms = ref();
const website = ref('');
const captcha = ref();
const validOrcid = (value: string) => !helpers.req(value) || validateOrcid(value);
const rules = computed(() => ({
  name: {
    required,
  },
  mail: {
    required,
    validMail,
  },
  message: {
    required,
  },
  terms: {
    required,
  },
  orcid: {
    validOrcid,
  },
}));
const v = useVuelidate(rules, {
  name,
  mail,
  message,
  terms,
});
const resetForm = () => {
  busy.value = false;
  showError.value = false;
  v.value.$reset();
};
onMounted(() => {
  resetForm();
});
const handleSubmit = async () => {
  busy.value = true;
  v.value.$validate();
  if (!v.value.$error && website.value.length === 0) {
    const token = await captcha.value.verifyCaptcha();
    if (token) {
      try {
        const response = await fetch(`${props.baseUrl}rsc/contact-request/create?objId=${props.objectId}`, {
          method: 'POST',
          headers: {
            Accept: 'application/json, text/plain, */*',
            'Content-Type': 'application/json',
            'X-Captcha': token,
          },
          body: JSON.stringify({
            name: name.value,
            email: mail.value,
            orcid: orcid.value,
            message: message.value,
          }),
        });
        busy.value = false;
        if (response.ok) {
          emit('success');
        } else {
          showError.value = true;
        }
      } catch (error) {
        showError.value = true;
        busy.value = false;
      }
    }
  }
  busy.value = false;
};
</script>
<style scoped>
input#website {
  display: none;
}
#required-label:before {
  content:"*\00a0";
}
</style>

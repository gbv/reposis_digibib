import { createApp } from 'vue';
import { createI18n } from 'vue-i18n';
import axios from 'axios';
import ContactManager from './App.vue';

if (process.env.NODE_ENV === 'development') {
  axios.defaults.baseURL = 'http://134.169.20.124:8291/mir/';
} else {
  // eslint-disable-next-line
  axios.defaults.baseURL = (window as any).webApplicationBaseURL;
}

(async () => {
  const app = createApp(ContactManager);
  const i18nResponse = await axios.get('rsc/locale/translate/digibib.contactRequest.frontend.*');
  const i18n = createI18n({
    legacy: false,
    locale: '_',
    messages: {
      _: i18nResponse.data,
    },
  });
  app.use(i18n);
  app.mount('#app');
})();

import { createApp } from 'vue';
import { createI18n } from 'vue-i18n';
import { useConfigStore } from '@/stores';
import { createPinia } from 'pinia';
import {
  getObjectId,
  getWebApplicationBaseURL,
  fetchI18n,
} from '@/utils';
import objectIdKey from '@/keys';
import ContactManager from '@/App.vue';

const webApplicationBaseURL = getWebApplicationBaseURL() as string;

(async () => {
  const data = await fetchI18n(webApplicationBaseURL);
  const i18n = createI18n({
    legacy: false,
    locale: '_',
    messages: {
      _: data,
    },
    warnHtmlInMessage: 'off',
  });
  const app = createApp(ContactManager);
  app.use(createPinia());
  const configStore = useConfigStore();
  app.provide(objectIdKey, getObjectId());
  configStore.webApplicationBaseURL = webApplicationBaseURL;
  app.use(i18n);
  app.config.errorHandler = (err, instance, info) => {
    // eslint-disable-next-line
    console.error('Global error:', err);
    // eslint-disable-next-line
    console.log('Vue instance:', instance);
    // eslint-disable-next-line
    console.log('Error info:', info);
  };
  app.mount('#app');
})();

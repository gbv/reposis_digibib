import { createApp } from 'vue';
import { createI18n } from 'vue-i18n';
import App from './App.vue';

(async () => {
  const app = createApp(App, {
    objectId: 'mir_mods_00000025',
    baseUrl: 'http://localhost:8291/mir/',
  });
  const i18nResponse = await fetch('http://localhost:8291/mir/rsc/locale/translate/digibib.contact.frontend.*');
  const i18nData = await i18nResponse.json();
  const i18n = createI18n({
    locale: '_',
    messages: {
      _: i18nData,
    },
  });
  app.use(i18n);
  app.mount('#app');
})();

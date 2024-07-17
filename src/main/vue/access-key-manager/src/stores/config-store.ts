import { defineStore } from 'pinia';
import { fetchConfig } from '@/utils';

interface State {
  webApplicationBaseURL: string
  config: Record<string, string>
}

const ALLOWED_SESSION_TYPES = 'MCR.ACL.AccessKey.Strategy.AllowedSessionPermissionTypes';

// eslint-disable-next-line import/prefer-default-export
export const useConfigStore = defineStore('config', {
  state: (): State => ({
    webApplicationBaseURL: '',
    config: { },
  }),
  getters: {
    isSessionEnabled: (state): boolean => state.config[ALLOWED_SESSION_TYPES] !== undefined
      && state.config[ALLOWED_SESSION_TYPES].length > 0,
    getAllowedSessionPermissionTypes: (state): string[] => {
      if (!state.config[ALLOWED_SESSION_TYPES]) {
        return [];
      }
      return state.config[ALLOWED_SESSION_TYPES].split(',');
    },
  },
  actions: {
    async fetchConfig(): Promise<void> {
      try {
        this.config = await fetchConfig(this.webApplicationBaseURL);
      } catch (error) {
        throw new Error('component.acl.accesskey.frontend.error.fatal');
      }
    },
  },
});
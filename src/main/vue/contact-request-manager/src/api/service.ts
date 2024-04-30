import axios from 'axios';
import ContactInfo from '@/model/ContactInfo';

interface ErrorResponse {
  errorCode: string;
}

axios.interceptors.response.use((response) => response, (error) => {
  if (axios.isAxiosError(error) && error.response) {
    if (error.response.status === 401 || error.response.status === 403) {
      return Promise.reject(new Error('unauthorizedError'));
    }
    if (!error.response || !error.response.data) {
      return Promise.reject(new Error('unknown'));
    }
    const { errorCode } = (error.response.data as ErrorResponse);
    if (errorCode && errorCode !== 'UNKNOWN') {
      return Promise.reject(new Error(errorCode));
    }
  }
  return Promise.reject(new Error('unknown'));
});

const BASE_API_PATH = 'api/v2/contact-requests';

const getContactRequests = (offset: number, limit: number) => axios.get(`${BASE_API_PATH}?offset=${offset}&limit=${limit}`);
const removeContactRequestById = (contactRequestId: string) => axios.delete(`${BASE_API_PATH}/${contactRequestId}`);
const patchContactRequestCommentById = (contactRequestId: string, comment: string) => axios.patch(`${BASE_API_PATH}/${contactRequestId}`, {
  comment,
});
const patchContactRequestStatusById = (contactRequestId: string, status: number) => axios.patch(`${BASE_API_PATH}/${contactRequestId}`, {
  status,
});
const forwardContactRequest = (contactRequestId: string, contactId: string) => axios.post(`${BASE_API_PATH}/${contactRequestId}/forwardRequest?contactId=${contactId}`);
const createContactInfo = (contactRequestId: string, contact: ContactInfo) => axios.post(`${BASE_API_PATH}/${contactRequestId}/contacts`, contact);
const patchContactInfoName = (contactRequestId: string, contactId: string, name: string) => axios.patch(`${BASE_API_PATH}/${contactRequestId}/contacts/${contactId}`, {
  name,
});
const removeContactInfo = (contactRequestId: string, contactInfoId: string) => axios.delete(`${BASE_API_PATH}/${contactRequestId}/contacts/${contactInfoId}`);
const getContactInfos = (contactRequestId: string) => axios.get(`${BASE_API_PATH}/${contactRequestId}/contacts`);
const getContactInfo = (contactRequestId: string, contactInfoId: string) => axios.get(`${BASE_API_PATH}/${contactRequestId}/contacts/${contactInfoId}`);
const getContactRequestById = (contactRequestId: string) => axios.get(`${BASE_API_PATH}/${contactRequestId}`);
const getContactAttemps = (contactRequestId: string) => axios.get(`${BASE_API_PATH}/${contactRequestId}/attempts`);

export {
  getContactAttemps,
  getContactRequests,
  getContactRequestById,
  getContactInfos,
  patchContactRequestCommentById,
  patchContactRequestStatusById,
  getContactInfo,
  removeContactRequestById,
  forwardContactRequest,
  createContactInfo,
  patchContactInfoName,
  removeContactInfo,
};

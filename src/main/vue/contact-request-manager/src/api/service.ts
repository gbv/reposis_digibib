import axios from 'axios';
import { ErrorResponse, ContactInfo } from '../utils';

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

const getTickets = (offset: number, limit: number) => axios.get(`api/v2/contact-tickets?offset=${offset}&limit=${limit}`);
const removeTicket = (ticketId: string) => axios.delete(`api/v2/contact-tickets/${ticketId}`);
const patchTicketComment = (ticketId: string, comment: string) => axios.patch(`api/v2/contact-tickets/${ticketId}`, {
  comment,
});
const patchTicketStatus = (ticketId: string, status: number) => axios.patch(`api/v2/contact-tickets/${ticketId}`, {
  status,
});
const forwardRequest = (ticketId: string, contactId: string) => axios.post(`api/v2/contact-tickets/${ticketId}/forwardRequest?contactId=${contactId}`);
const createContactInfo = (ticketId: string, contact: ContactInfo) => axios.post(`api/v2/contact-tickets/${ticketId}/contacts`, contact);
const patchContactInfoName = (ticketId: string, contactId: string, name: string) => {
  axios.patch(`api/v2/contact-tickets/${ticketId}/contacts/${contactId}`, {
    name,
  });
};
const removeContactInfo = (ticketId: string, contactInfoId: string) => axios.delete(`api/v2/contact-tickets/${ticketId}/contacts/${contactInfoId}`);
const getContactInfos = (ticketId: string) => axios.get(`api/v2/contact-tickets/${ticketId}/contacts`);
const getContactInfo = (ticketId: string, contactInfoId: string) => axios.get(`api/v2/contact-tickets/${ticketId}/contacts/${contactInfoId}`);
const getTicket = (ticketId: string) => axios.get(`api/v2/contact-tickets/${ticketId}`);
const closeTicket = (ticketId: string) => axios.post(`api/v2/contact-tickets/closeTicket?ticketId=${ticketId}`);
const getContactAttemps = (ticketId: string) => axios.get(`api/v2/contact-tickets/${ticketId}/attempts`);

export {
  getContactAttemps,
  getTickets,
  getTicket,
  closeTicket,
  getContactInfos,
  patchTicketComment,
  patchTicketStatus,
  getContactInfo,
  removeTicket,
  forwardRequest,
  createContactInfo,
  patchContactInfoName,
  removeContactInfo,
};

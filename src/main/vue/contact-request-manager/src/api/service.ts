import axios from 'axios';
import ContactInfo from '@/model/ContactInfo';
import ContactRequest, { ContactRequestStatus } from '@/model/ContactRequest';
import ContactAttempt from '@/model/ContactAttempt';

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

const BASE_API_PATH = 'api/contact-requests';

interface ContactRequestBodyDto {
  name: string;
  email: string;
  message: string;
  orcid?: string;
}

interface ContactRequestDto {
  id: string;
  objectId: string;
  body: ContactRequestBodyDto;
  created: Date;
  createdBy: Date;
  status: string;
  comment: string;
}

interface PaginatedContactRequests{
  contactRequests: ContactRequest[];
  total: number;
}

interface ContactAttemptDto {
  id: string;
  type: string;
  recipientName: string;
  recipientReference: string;
  sendDate: Date;
  errorDate: Date;
  successDate: Date;
  comment: string;
}

interface ContactInfoDto {
  id: string;
  name: string;
  email: string;
  origin: string;
  reference: string;
}

const mapStatusFromString = (status: string): ContactRequestStatus => {
  switch (status) {
  case 'OPEN':
    return ContactRequestStatus.OPEN;
  case 'CLOSED':
    return ContactRequestStatus.CLOSED;
  default:
    throw new Error(`Unknown status: ${status}`);
  }
}

interface CreateContactAttemptDto {
  type: string;
  recipientName: string;
  recipientReference: string;
  comment?: string;
}

class ContactRequestService {

  async getAllContactRequests(offset: number, limit: number): Promise<PaginatedContactRequests> {
    const response = await axios.get(`${BASE_API_PATH}?offset=${offset}&limit=${limit}`);
    const contactRequests: ContactRequest[] = response.data.map(
      (contactRequestDto: ContactRequestDto) => ({
        ...contactRequestDto,
        status: mapStatusFromString(contactRequestDto.status)
      })
    );
    return {
      contactRequests,
      total: Number(response.headers['x-total-count'])
    };
  }

  async removeContactRequestById(contactRequestId: string): Promise<void> {
    await axios.delete(`${BASE_API_PATH}/${contactRequestId}`);
  }

  async patchContactRequestCommentById(contactRequestId: string, comment: string): Promise<void> {
    await axios.patch(`${BASE_API_PATH}/${contactRequestId}`, {
      comment,
    });
  }

  async patchContactRequestStatusById(
    contactRequestId: string,
    status: ContactRequestStatus
  ): Promise<void> {
    await axios.patch(`${BASE_API_PATH}/${contactRequestId}`, {
      status: ContactRequestStatus[status],
    });
  }

  async createContactAttempt(
    contactRequestId: string,
    contactAttemptDto: CreateContactAttemptDto
  ): Promise<string> {
    const url = `${BASE_API_PATH}/${contactRequestId}/attempts`
    const response = await axios.post<CreateContactAttemptDto>(url, contactAttemptDto);
    return response.headers.location.split('/').pop() as string;
  }

  async createContactInfo (contactRequestId: string, contact: ContactInfo): Promise<string> {
    const response = await axios.post(`${BASE_API_PATH}/${contactRequestId}/contacts`, contact)
    return response.headers.location.split('/').pop() as string;
  }

  async patchContactInfoName(
    contactRequestId: string,
    contactId: string,
    name: string
  ): Promise<void> {
    await axios.patch(`${BASE_API_PATH}/${contactRequestId}/contacts/${contactId}`, {
      name,
    });
  }

  async removeContactInfo(contactRequestId: string, contactInfoId: string): Promise<void> {
    await axios.delete(`${BASE_API_PATH}/${contactRequestId}/contacts/${contactInfoId}`);
  }

  async getContactInfos (contactRequestId: string): Promise<ContactInfo[]> {
    const response = await axios
      .get<ContactInfoDto[]>(`${BASE_API_PATH}/${contactRequestId}/contacts`);
    return response.data.map((contactInfo: ContactInfoDto) => ({
      ...contactInfo,
    }));
  }

  async getContactInfo(contactRequestId: string, contactInfoId: string): Promise<ContactInfo> {
    const response = await axios
      .get<ContactInfoDto>(`${BASE_API_PATH}/${contactRequestId}/contacts/${contactInfoId}`);
    const contactInfo: ContactInfo = {
      ...response.data,
    }
    return contactInfo;
  }

  async getContactRequestById(contactRequestId: string): Promise<ContactRequest> {
    const response = await axios.get<ContactRequestDto>(`${BASE_API_PATH}/${contactRequestId}`);
    const contactRequest: ContactRequest = {
      ...response.data,
      status: mapStatusFromString(response.data.status)
    };
    return contactRequest;
  }

  async getContactAttempts(contactRequestId: string): Promise<ContactAttempt[]> {
    const response = await axios
      .get<ContactAttemptDto[]>(`${BASE_API_PATH}/${contactRequestId}/attempts`);
    return response.data.map((contactAttempt: ContactAttemptDto) => ({
      ...contactAttempt,
    }));
  }

  async getContactAttemptById(
    contactRequestId: string,
    contactAttemptId: string
  ): Promise<ContactAttempt> {
    const response = await axios
      .get<ContactAttemptDto>(`${BASE_API_PATH}/${contactRequestId}/attempts/${contactAttemptId}`);
    const contactAttempt: ContactAttempt = {
      ...response.data,
    };
    return contactAttempt;
  }
}

export default new ContactRequestService();

export {
  CreateContactAttemptDto,
  PaginatedContactRequests,
};

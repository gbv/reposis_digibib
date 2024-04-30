import moment from 'moment';

export const ORIGIN_MANUAL = 'manual';

export const ORIGIN_FALLBACK = 'fallback';

export enum TicketStatus {
  OPEN = 0,
  CLOSED = 10,
}

export namespace TicketStatus {
  export function toString(state: TicketStatus): string {
    return TicketStatus[state];
  }
}

export type ContactAttempt = {
  id: string;
  sendDate: Date;
  errorDate: Date;
  confirmationDate: Date;
  contactInfoId: string;
}

export type ContactInfo = {
  id: string;
  ticketId: string;
  name: string;
  email: string;
  origin: string;
  reference: string;
}

export type ContactRequest = {
  name: string;
  email: string;
  message: string;
  orcid?: string;
}

export type Ticket = {
  id: string;
  objectId: string;
  request: ContactRequest;
  created: Date;
  createdBy: Date;
  status: TicketStatus;
  comment: string;
}

export type ErrorResponse = {
  errorCode: string;
}

export const getFormatedDate = (date: Date) => moment(date).format('DD.MM.YYYY, hh:mm');

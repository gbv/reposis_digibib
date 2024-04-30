export interface ContactRequestBody {
  name: string;
  email: string;
  message: string;
  orcid?: string;
}
export enum ContactRequestStatus {
  OPEN = 0,
  CLOSED = 10,
}
// eslint-disable-next-line
export namespace Status {
  export function toString(state: ContactRequestStatus): string {
    return Status[state];
  }
}
interface ContactRequest {
  id: string;
  objectId: string;
  body: ContactRequestBody;
  created: Date;
  createdBy: Date;
  status: ContactRequestStatus;
  comment: string;
}
export default ContactRequest;

interface ContactAttempt {
  id: string;
  type: string;
  recipientName: string;
  recipientReference: string;
  sendDate: Date;
  errorDate: Date;
  successDate: Date;
}
export default ContactAttempt;

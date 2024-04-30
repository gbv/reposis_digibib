import ContactInfo from './ContactInfo';

interface ContactAttempt {
  id: string;
  sendDate: Date;
  errorDate: Date;
  successDate: Date;
  contactInfo: ContactInfo;
}
export default ContactAttempt;

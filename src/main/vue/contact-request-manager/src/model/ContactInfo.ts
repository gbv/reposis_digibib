export const ORIGIN_MANUAL = 'manual';
export const ORIGIN_FALLBACK = 'fallback';
interface ContactInfo {
  id: string;
  name: string;
  email: string;
  origin: string;
  reference: string;
}
export default ContactInfo;

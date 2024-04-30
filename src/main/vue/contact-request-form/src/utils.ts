// https://support.orcid.org/hc/en-us/articles/360006897674-Structure-of-the-ORCID-Identifier
export default function validatOrcid(orcid: string): boolean {
  if (orcid.length < 16) {
    return false;
  }
  const checkDigit = orcid.slice(-1);
  if (!/^\d+$/.test(checkDigit) && checkDigit.toUpperCase() !== 'X') {
    return false;
  }
  const digits = orcid.slice(0, -1).match(/\d/g);
  /* eslint-disable  @typescript-eslint/no-non-null-assertion */
  const digitString = digits!.join('');
  if (digitString.length !== 15) {
    return false;
  }
  let total = 0;
  for (let i = 0; i < 15; i += 1) {
    total = (total + parseInt(digitString.charAt(i), 10)) * 2;
  }
  const remainder = total % 11;
  const result = (12 - remainder) % 11;
  if (checkDigit === 'X') {
    return result === 10;
  }
  return result === Number(checkDigit);
}

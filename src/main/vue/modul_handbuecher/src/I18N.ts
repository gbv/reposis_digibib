export async function i18n(baseurl: string, language: string, key: string): Promise<string> {
    const response = await fetch(`${baseurl}rsc/locale/translate/${language}/${key}`);
    return await response.text();
}
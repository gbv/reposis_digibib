async function getTranslation(locale, name) {
  const response = await fetch(`../rsc/locale/translate/${locale}/${name}`);
  if (!response.ok) {
    throw new Error(`HTTP error ${response.status}`);
  }
  return await response.text();
}

async function getDocumentCount() {
  const response = await fetch('../api/v1/search?q=objectType:mods AND state:published&rows=0&wt=json');
  if (!response.ok) {
    throw new Error(`HTTP error ${response.status}`);
  }
  const data = await response.json();
  return data?.response?.numFound;
}

document.addEventListener('DOMContentLoaded', async () => {
  document.getElementById('bs-searchMainPage')?.addEventListener('submit', ignoreEmptyFieldsOnSubmit);
  const input = document.getElementById('bs-searchInput');
  if (input) {
    try {
      const placeholder = await getTranslation(currentLang, 'digibib.index.search.placeholder');
      const count = await getDocumentCount();
      input.placeholder = placeholder.replace('{0}', count.toLocaleString('de-DE'));
    } catch {
      console.error('Error updating search placeholder:', err);
    }
  }
});

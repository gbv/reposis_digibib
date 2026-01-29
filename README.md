
# digibib

## Installation Instructions

* run `mvn clean install`
* copy jar to ~/.mycore/(dev-)mir/lib/

## Development

You can add these to your ~/.mycore/(dev-)mir/.mycore.properties
```
MCR.Developer.Resource.Override=/path/to/digibib/src/main/resources
MCR.LayoutService.LastModifiedCheckPeriod=0
MCR.UseXSLTemplateCache=false
MCR.SASS.DeveloperMode=true
```


## Agreement Feature

The Agreement feature allows attaching a consent form (Agreement) to publications. It is integrated into the **XEditor**, validated during submission, and then generated as a PDF and transmitted.

---

### Activation in the Editor

The template is integrated into XEditor forms via:

```properties
MIR.EditorForms.CustomIncludes=%MIR.EditorForms.CustomIncludes%,xslStyle:editor/mir2xeditor:webapp:editor/editor-agreement-customization.xed
```

To activate:

```properties
Digibib.Agreement.DefaultAgreementId=default
```

* If the property is empty, no Agreement will be shown.
* `default` is the standard ID for Agreements.
* Agreements can also be activated per genre:

```properties
Digibib.Agreement.Genres.<genre>.AgreementId=test
```

To load an Agreement, a PDF file with the corresponding ID and language must be available under `/resources/content/publish/` (e.g. `default_de.pdf`).

---

### Workflow

1. The Agreement checkbox in the editor must be selected (✔), otherwise submission is not possible.

2. Upon confirmation, a `servflag` is added to the XML, for example:

   ```xml
   <servflag type="confirmedAgreement" inherited="0" form="plain">default</servflag>
   ```

3. An event handler listens for this flag and creates/transfers the Agreement if:

   * no Agreement has been transferred yet,
   * the object is published,
   * the object has no DOI (the DOI is used as the Agreement filename),
   * a valid Agreement `confirmedAgreement` flag exists.

4. A PDF is then generated for the object and transferred to the configured endpoint.

5. Once an Agreement has been successfully transmitted, an `agreementTransmissionInfo` flag is added. This flag serves to check whether an Agreement has already been transferred. Simultaneously, the `confirmedAgreement` flag is removed.

---

### PDF Generation

#### FormData Creation

For each Agreement ID, a transformer must be configured. Example for `default`:

```properties
MCR.ContentTransformer.mycoreobject-default-agreement-formdata.Stylesheet=xslt/agreement/mycoreobject-default-formdata.xsl
MCR.ContentTransformer.mycoreobject-default-agreement-formdata.TransformerFactoryClass=net.sf.saxon.TransformerFactoryImpl
```

The result is a `<formData>` element containing the relevant fields, e.g.:

```xml
<formData>
  <field name="title" type="text">Title</field>
  <field name="license" type="radio">cc_by_4.0</field>
</formData>
```

Missing fields in the AcroForm will cause errors.

#### Author Resolver

An author resolver (`agreementauthor:<userid>`) extracts author information. By default, information is taken from the `created` flag in metadata. Specific resolvers can be configured per realm.

* Example: LDAP resolver for TU Braunschweig

```properties
Digibib.Agreement.AuthorService.ResolverProvider.Class=de.vzg.reposis.digibib.agreement.service.author.AuthorResolverProvider
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Class=de.vzg.reposis.digibib.agreement.service.author.AuthorResolverEntry
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Realm=tu-bs.de
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Resolver.Class=de.vzg.reposis.digibib.agreement.service.author.ldap.LdapAuthorService
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Resolver.LdapClient.Class=de.vzg.reposis.digibib.ldap.LdapClient
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Resolver.LdapClient.ProviderUrl=
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Resolver.LdapClient.SecurityProtocol=SSL
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Resolver.LdapClient.SecurityAuthentication=none
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Resolver.LdapClient.ConnectTimeout=5000
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Resolver.LdapClient.ReadTimeout=1000
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Resolver.BaseDn=ou=people,dc=tu-bs,dc=de
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Resolver.UidFilter=(uid=%s)
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Resolver.AuthorMapping.Class=de.vzg.reposis.digibib.agreement.service.author.ldap.LdapAuthorMapping
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Resolver.AuthorMapping.Name=cn
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Resolver.AuthorMapping.Email=mail
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Resolver.AuthorMapping.Phone=telephoneNumber
Digibib.Agreement.AuthorService.ResolverProvider.Resolvers.tubs.Resolver.AuthorMapping.Institute=ou
```

#### Processor Configuration

Each Agreement ID requires a processor configuration. Example for `default`:

```properties
Digibib.Agreement.Service.PdfService.ProcessorProvider.Processors.default.Class=de.vzg.reposis.digibib.pdf.processor.PdfFormProcessor
Digibib.Agreement.Service.PdfService.ProcessorProvider.Processors.default.FileLoader.Class=de.vzg.reposis.digibib.pdf.util.ResourceFileLoader
Digibib.Agreement.Service.PdfService.ProcessorProvider.Processors.default.FileLoader.File=/META-INF/resources/content/publish/deposit-single_draft.pdf
Digibib.Agreement.Service.PdfService.ProcessorProvider.Processors.default.Filler.Class=de.vzg.reposis.digibib.pdf.processor.FormDataPdfFiller
```

---

### Agreement Transmission

The generated Agreement PDF can be transferred using a transmitter. By default, an **email transmitter** is available:

```properties
Digibib.Agreement.Service.Transmitter.Class=de.vzg.reposis.digibib.agreement.transport.PdfAgreementMailTransmitter
Digibib.Agreement.Service.Transmitter.Recipient=<email>
Digibib.Agreement.Service.Transmitter.EmailClient=default
```

A corresponding **email client** must be configured (see email client configuration).

---


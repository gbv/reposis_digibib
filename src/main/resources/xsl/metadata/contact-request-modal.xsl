<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:i18n="xalan://org.mycore.services.i18n.MCRTranslation" exclude-result-prefixes="i18n">
  <xsl:import href="xslImport:modsmeta:metadata/contact-request-modal.xsl" />
  <xsl:param name="Digibib.ContactRequest.RequestForm.EnabledGenres" />
  <xsl:variable name="objectId" select="/mycoreobject/@ID" />

  <xsl:template match="/">
    <xsl:if test="contains($Digibib.ContactRequest.RequestForm.EnabledGenres, $mods-type)">
      <div id="createContactRequestModalDiv">
        <xsl:call-template name="print-create-contact-request-modal" />
        <xsl:call-template name="print-open-create-contact-request-form-button" />
      </div>
    </xsl:if>
    <xsl:apply-imports />
  </xsl:template>

  <xsl:template name="print-create-contact-request-modal">
    <div class="modal fade" id="createContactRequestModal" tabindex="-1"
      aria-labelledby="createContactRequestModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="createContactRequestModalLabel">
              <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.title')" />
            </h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Schließen">
              <span aria-hidden="true">&#215;</span>
            </button>
          </div>
          <div class="modal-body">
            <div class="container-fluid">
              <div class="row">
                <div class="col-12">
                  <div class="alert alert-danger d-none" id="contactAlert" role="alert">
                    <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.error')" />
                  </div>
                </div>
              </div>
              <div class="row pb-4">
                <div class="col-12">
                  <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.text')" />
                </div>
              </div>
              <form id="form" novalidate="">
                <div class="form-row">
                  <div class="col-6">
                    <div class="form-group">
                      <label class="control-label" for="nameInput">
                        <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.label.name')" />*
                      </label>
                      <input type="text" class="form-control" id="nameInput" name="name" required="" />
                      <div class="invalid-feedback">
                        <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.validation.name')" />
                      </div>
                    </div>
                  </div>
                  <div class="col-6">
                    <div class="form-group">
                      <label class="control-label" for="emailInput">
                        <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.label.email')" />*
                      </label>
                      <input type="email" class="form-control" id="emailInput" name="email" required="" />
                      <div class="invalid-feedback">
                        <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.validation.email')" />
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-12">
                    <div class="form-group">
                      <label for="orcidInput">
                        <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.label.orcid')" />
                      </label>
                      <input type="text" class="form-control" id="orcidInput" name="orcid" />
                      <div class="invalid-feedback">
                        <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.validation.orcid')" />
                      </div>
                    </div>
                  </div>
                </div>
                <div class="row">
                  <div class="col-12">
                    <div class="form-group">
                      <label class="control-label" for="messageInput">
                        <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.label.message')" />*
                      </label>
                      <textarea type="text" class="form-control" id="messageInput" name="message" rows="8" required=""></textarea>
                      <div class="invalid-feedback">
                        <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.validation.message')" />
                      </div>
                    </div>
                  </div>
                </div>
                <input id="website" name="website" type="text" class="d-none" />
              </form>
              <div class="row">
                <div class="col-12">
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" id="termsCheck" />
                    <label class="control-label form-check-label" for="termsCheck">
                      <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.label.acceptTerms')" />*
                    </label>
                  </div>
                </div>
              </div>
              <div class="row pt-4">
                <div class="col">
                  <div class="d-flex flex-column float-right" style="width: 225px">
                    <div class="d-flex flex-row">
                      <img id="captchaImage" style="width: 200px; height: auto;" alt="graphical access code" />
                      <div class="d-flex flex-column justify-content-md-center" style="width: 20px; margin-left: 5px;">
                        <i id="reloadCaptcha" class="fas fa-redo" aria-hidden="true"></i>
                      </div>
                    </div>
                    <div class="pt-2">
                      <input type="text" id="captchaInput" class="form-control-sm form-control w-100" />
                    </div>
                  </div>
                </div>
              </div>
              <span class="small font-weight-light">
                *<xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.label.required')" />
              </span>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">
              <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.button.cancel')" />
            </button>
            <button type="button" class="btn btn-primary" id="submitButton">
              <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.form.button.submit')" />
            </button>
          </div>
        </div>
      </div>
    </div>
    <div class="modal fade" id="contactRequestConfirmModal" tabindex="-1"
        aria-labelledby="contactRequestConfirmModalLabel" aria-hidden="true">
      <div class="modal-dialog">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="contactRequestConfirmModalLabel">
              <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.confirmation.title')" />
            </h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Schließen">
              <span aria-hidden="true">&#215;</span>
            </button>
          </div>
          <div class="modal-body">
            <p>
              <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.confirmation.text')" />
            </p>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Ok</button>
          </div>
        </div>
      </div>
    </div>
    <script>
      document.addEventListener('DOMContentLoaded', () => {
        const modal = document.querySelector('#createContactRequestModal');
        const form = modal.querySelector('#form');
        const nameInput = modal.querySelector('#nameInput');
        const emailInput = modal.querySelector('#emailInput');
        const messageInput = modal.querySelector('#messageInput');
        const orcidInput = modal.querySelector('#orcidInput');
        const termsCheck = modal.querySelector('#termsCheck');
        const captchaImage = modal.querySelector('#captchaImage')
        const captchaInput = modal.querySelector('#captchaInput');
        const submitButton = modal.querySelector('#submitButton');
        const reloadCaptchaButton = modal.querySelector('#reloadCaptcha');
        const alert = modal.querySelector('#contactAlert');
        let objectId = '';

        const resetModal = () => {
          form.reset();
          nameInput.classList.remove('is-valid');
          emailInput.classList.remove('is-valid');
          messageInput.classList.remove('is-valid');
          orcidInput.classList.remove('is-valid');
          nameInput.classList.remove('is-invalid');
          emailInput.classList.remove('is-invalid');
          messageInput.classList.remove('is-invalid');
          orcidInput.classList.remove('is-invalid');
          termsCheck.classList.remove('is-invalid');
          captchaInput.classList.remove('is-invalid');
          termsCheck.checked = false;
          alert.classList.add('d-none');
        };

        const shuffleCaptcha = () => {
          captchaImage.src = `${webApplicationBaseURL}rsc/captcha/generate?${Date.now()}`;
          captchaInput.value = '';
        };

        $('#createContactRequestModal').on('show.bs.modal', (event) => {
          objectId = event.relatedTarget.dataset.objectId;
          shuffleCaptcha();
        });

        $('#createContactRequestModal').on('hidden.bs.modal', (event) => {
          resetModal();
        });

        reloadCaptchaButton.addEventListener('click', () => {
          shuffleCaptcha();
        });

        const validateEmail = (email) => {
          const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
          return re.test(String(email).toLowerCase());
        };

        const validateOrcid = (orcid) => {
          if (orcid.length &lt; 16) {
            return false;
          }
          const checkDigit = orcid.slice(-1);
          if (!/^\d+$/.test(checkDigit) &amp;&amp; checkDigit.toUpperCase() !== 'X') {
            return false;
          }
          const digits = orcid.slice(0, -1).match(/\d/g);
          const digitString = digits.join('');
          if (digitString.length !== 15) {
            return false;
          }
          let total = 0;
          for (let i = 0; i &lt; 15; i += 1) {
            total = (total + parseInt(digitString.charAt(i), 10)) * 2;
          }
          const remainder = total % 11;
          const result = (12 - remainder) % 11;
          if (checkDigit === 'X') {
            return result === 10;
          }
          return result === Number(checkDigit);
        };

        const validateCaptcha = async (value) => {
          const response = await fetch(`${webApplicationBaseURL}rsc/captcha/validate`, {
            method: 'POST',
            headers: {
              'Content-Type': 'text/plain',
            },
            body: value,
          });
          if (!response.ok) {
            throw new Error('captchaError');
          }
        };

        const createContactRequest = async (contactRequest) => {
          const response = await fetch(`${webApplicationBaseURL}rsc/contact-request/create?objId=${objectId}`, {
            method: 'POST',
            headers: {
              Accept: 'application/json, text/plain, */*',
              'Content-Type': 'application/json',
            },
            body: JSON.stringify(contactRequest),
          });
          if (!response.ok) {
            throw new Error();
          }
        };

        const validateForm = () => {
          let isFormValid = true;
          if (nameInput.value.trim() === '') {
            nameInput.classList.add('is-invalid');
            isFormValid = false;
          } else {
            nameInput.classList.remove('is-invalid');
            nameInput.classList.add('is-valid');
          }
          if (emailInput.value.trim() === '') {
            emailInput.classList.add('is-invalid');
            isFormValid = false;
          } else if (!validateEmail(emailInput.value.trim())) {
            emailInput.classList.add('is-invalid');
            isFormValid = false;
          } else {
            emailInput.classList.remove('is-invalid');
            emailInput.classList.add('is-valid');
          }
          if (messageInput.value.trim() === '') {
            messageInput.classList.add('is-invalid');
            isFormValid = false;
          } else {
            messageInput.classList.remove('is-invalid');
            messageInput.classList.add('is-valid');
          }
          if (orcidInput.value.trim().length > 0) {
            if (!validateOrcid(orcidInput.value.trim())) {
              orcidInput.classList.add('is-invalid');
              isFormValid = false;
            } else {
              orcidInput.classList.remove('is-invalid');
              orcidInput.classList.add('is-valid');
            }
          }
          if (termsCheck.checked === false) {
            isFormValid = false;
            termsCheck.classList.add('is-invalid');
          } else {
            termsCheck.classList.remove('is-invalid');
          }
          return isFormValid;
        };

        submitButton.addEventListener('click', async () => {
          alert.classList.add('d-none');
          if (validateForm()) {
            if (captchaInput.value.trim() === '') {
              captchaInput.classList.add('is-invalid');
            } else {
              captchaInput.classList.remove('is-invalid');
              try {
                await validateCaptcha(captchaInput.value.trim());
                const contactRequest = {
                  name: nameInput.value,
                  email: emailInput.value,
                  message: messageInput.value,
                };
                if (orcidInput.value.trim().length > 0) {
                  contactRequest.orcid = orcidInput.value.trim();
                }
                await createContactRequest(contactRequest);
                $('#createContactRequestModal').modal('hide');
                $('#contactRequestConfirmModal').modal('show');
              } catch(e) {
                if (e.message === 'captchaError') {
                  captchaInput.classList.add('is-invalid');
                  await shuffleCaptcha();
                } else {
                  alert.classList.remove('d-none');
                  console.error(e);
                }
              }
            }
          }
        });
      });
    </script>
  </xsl:template>

  <xsl:template name="print-open-create-contact-request-form-button">
    <li>
      <a id="createContactRequestMenuItem" class="dropdown-item" role="menuitem" tabindex="-1"
        data-toggle="modal" data-target="#createContactRequestModal" data-object-id="{$objectId}">
        <xsl:value-of select="i18n:translate('digibib.contactRequest.frontend.button.contact')" />
      </a>
    </li>
    <script>
      document.addEventListener('DOMContentLoaded', () => {
        const menu = document.querySelector('div#mir-edit-div ul.dropdown-menu');
        const item = document.querySelector('#createContactRequestMenuItem');
        menu.append(item);
      });
    </script>
  </xsl:template>
</xsl:stylesheet>

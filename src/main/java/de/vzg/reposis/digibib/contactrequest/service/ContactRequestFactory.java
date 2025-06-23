/*
 * This file is part of ***  M y C o R e  ***
 * See http://www.mycore.de/ for details.
 *
 * MyCoRe is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MyCoRe is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MyCoRe.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.vzg.reposis.digibib.contactrequest.service;

import de.vzg.reposis.digibib.contactrequest.repository.ContactAttemptRepository;
import de.vzg.reposis.digibib.contactrequest.repository.ContactInfoRepository;
import de.vzg.reposis.digibib.contactrequest.repository.ContactRequestRepository;
import de.vzg.reposis.digibib.contactrequest.validation.ContactAttemptValidator;
import de.vzg.reposis.digibib.contactrequest.validation.ContactInfoValidator;
import de.vzg.reposis.digibib.contactrequest.validation.ContactRequestValidator;
import de.vzg.reposis.digibib.contactrequest.validation.ValidatorFacade;
import de.vzg.reposis.digibib.email.EmailClientFactory;

/**
 * Factory class for creating and providing singleton instances of services and repositories
 * related to contact requests, contact information, and contact attempts.
 */
public class ContactRequestFactory {

    private static final Object LOCK = new Object();

    private static volatile ContactRequestService requestService;

    private static volatile ContactInfoService infoService;

    private static volatile ContactAttemptService attemptService;

    private static volatile ContactRequestRepository requestRepo;

    private static volatile ContactInfoRepository infoRepo;

    private static volatile ContactAttemptRepository attemptRepo;

    private static volatile ContactRequestValidator requestValidator;

    private static volatile ContactInfoValidator infoValidator;

    private static volatile ContactAttemptValidator attemptValidator;

    private static volatile EmailService emailService;

    /**
     * Returns the singleton instance of {@link ContactRequestService}.
     *
     * @return the request service instance
     */
    public static ContactRequestService getContactRequestService() {
        if (requestService == null) {
            synchronized (LOCK) {
                if (requestService == null) {
                    final ValidatorFacade validatorFacade
                        = new ValidatorFacade(getRequestValidator(), getInfoValidator(), getAttemptValidator());
                    requestService
                        = new ContactRequestServiceImpl(getRequestRepo(), getInfoRepo(), getAttemptRepo(),
                            validatorFacade);
                }
            }
        }
        return requestService;
    }

    /**
     * Returns the singleton instance of {@link ContactInfoService}.
     *
     * @return the info service instance
     */
    public static ContactInfoService getContactInfoService() {
        if (infoService == null) {
            synchronized (LOCK) {
                if (infoService == null) {
                    infoService = new ContactInfoServiceImpl(getInfoRepo(), getInfoValidator());
                }
            }
        }
        return infoService;
    }

    /**
     * Returns the singleton instance of {@link ContactAttemptService}.
     *
     * @return the attempt service instance
     */
    public static ContactAttemptService getContactAttemptService() {
        if (attemptService == null) {
            synchronized (LOCK) {
                if (attemptService == null) {
                    attemptService = new ContactAttemptServiceImpl(getAttemptRepo(), getAttemptValidator());
                }
            }
        }
        return attemptService;
    }

    /**
     * Returns the singleton instance of {@link EmailService}.
     *
     * @return the email service instance
     */
    public static EmailService getContactEmailService() {
        if (emailService == null) {
            synchronized (LOCK) {
                if (emailService == null) {
                    emailService = createEmailService();
                }
            }
        }
        return emailService;
    }

    private static ContactInfoValidator getInfoValidator() {
        if (infoValidator == null) {
            synchronized (LOCK) {
                if (infoValidator == null) {
                    infoValidator = new ContactInfoValidator();
                }
            }
        }
        return infoValidator;
    }

    private static ContactAttemptValidator getAttemptValidator() {
        if (attemptValidator == null) {
            synchronized (LOCK) {
                if (attemptValidator == null) {
                    attemptValidator = new ContactAttemptValidator();
                }
            }
        }
        return attemptValidator;
    }

    private static ContactRequestValidator getRequestValidator() {
        if (requestValidator == null) {
            synchronized (LOCK) {
                if (requestValidator == null) {
                    requestValidator = new ContactRequestValidator();
                }
            }
        }
        return requestValidator;
    }

    private static ContactRequestRepository getRequestRepo() {
        if (requestRepo == null) {
            synchronized (LOCK) {
                if (requestRepo == null) {
                    requestRepo = new ContactRequestRepository();
                }
            }
        }
        return requestRepo;
    }

    private static ContactInfoRepository getInfoRepo() {
        if (infoRepo == null) {
            synchronized (LOCK) {
                if (infoRepo == null) {
                    infoRepo = new ContactInfoRepository();
                }
            }
        }
        return infoRepo;
    }

    private static ContactAttemptRepository getAttemptRepo() {
        if (attemptRepo == null) {
            synchronized (LOCK) {
                if (attemptRepo == null) {
                    attemptRepo = new ContactAttemptRepository();
                }
            }
        }
        return attemptRepo;
    }

    private static EmailService createEmailService() {
        return new EmailServiceImpl(EmailClientFactory.obstainInstance("contactrequest"));
    }

}

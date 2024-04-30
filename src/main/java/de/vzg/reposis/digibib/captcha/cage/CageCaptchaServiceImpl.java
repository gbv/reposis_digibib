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

package de.vzg.reposis.digibib.captcha.cage;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.mycore.common.MCRCache;
import org.mycore.common.MCRException;
import org.mycore.crypt.MCRCipher;
import org.mycore.crypt.MCRCipherManager;
import org.mycore.crypt.MCRCryptKeyFileNotFoundException;
import org.mycore.crypt.MCRCryptKeyNoPermissionException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cage.GCage;

import de.vzg.reposis.digibib.captcha.CaptchaService;

/**
 * This class implements a CAPTCHA service and uses Cage.
 */
public class CageCaptchaServiceImpl implements CaptchaService {

    private final static long TOKEN_LIFETIME = TimeUnit.MINUTES.toMillis(1);

    private final static int CACHE_SIZE = 1024;

    private final static String CIPHER_NAME = "captcha";

    private final MCRCache<String, Boolean> seenSecretsCache;

    private final GCage cage;

    private CageCaptchaServiceImpl() {
        seenSecretsCache = new MCRCache<>(CACHE_SIZE, "Cage CAPTCHA seen secrets cache");
        cage = new GCage();
    }

    /**
     * Returns singleton instance of CAPTCHA service.
     *
     * @return CAPTCHA service
     */
    public static CageCaptchaServiceImpl getInstance() {
        return ServiceHolder.INSTANCE;
    }

    /**
     * Returns {@link GCage} instance.
     *
     * @return instance
     */
    public GCage getCage() {
        return cage;
    }

    /**
     * Creates and returns new challenge.
     *
     * @return challenge
     */
    public String createChallenge() {
        final String secret = cage.getTokenGenerator().next();
        seenSecretsCache.put(secret, Boolean.FALSE);
        return secret;
    }

    /**
     * Verifies secret and returns token if valid.
     *
     * @param secret secret
     * @return token
     */
    public synchronized String verifyAndCreateToken(String secret) {
        if (Objects.equals(Boolean.FALSE, seenSecretsCache.getIfUpToDate(secret, TOKEN_LIFETIME))) {
            final Token token = new Token(secret);
            return encryptToken(token);
        }
        throw new MCRException("secret does not exist or is expired");
    }

    @Override
    public synchronized boolean checkToken(String encryptedTokenString) {
        Token token = null;
        try {
            token = decryptToken(encryptedTokenString);
        } catch (MCRException e) {
            return false;
        }
        if (Objects.equals(Boolean.TRUE, seenSecretsCache.getIfUpToDate(token.secret(), TOKEN_LIFETIME))) {
            return false;
        }
        seenSecretsCache.put(token.secret(), Boolean.TRUE);
        return true;
    }

    private static String encryptToken(Token token) {
        try {
            final String json = new ObjectMapper().writeValueAsString(token);
            final MCRCipher cipher = MCRCipherManager.getCipher(CIPHER_NAME);
            return cipher.encrypt(json);
        } catch (JsonProcessingException | MCRCryptKeyFileNotFoundException | MCRCryptKeyNoPermissionException e) {
            throw new MCRException(e);
        }
    }

    private static Token decryptToken(String encryptedTokenString) {
        try {
            final MCRCipher cipher = MCRCipherManager.getCipher(CIPHER_NAME);
            final String json = cipher.decrypt(encryptedTokenString);
            return new ObjectMapper().readValue(json, Token.class);
        } catch (MCRCryptKeyFileNotFoundException | MCRCryptKeyNoPermissionException | JsonProcessingException e) {
            throw new MCRException(e);
        }
    }

    private static class ServiceHolder {
        static final CageCaptchaServiceImpl INSTANCE = new CageCaptchaServiceImpl();
    }

    private record Token(String secret) {
    }

}

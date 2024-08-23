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

package de.vzg.reposis.digibib.email;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.mycore.common.config.MCRConfiguration2;

import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;

/**
 * A factory class for creating and managing instances of {@link EmailClient}.
 * <p>
 * This factory provides a singleton-like approach for obtaining {@link EmailClient}
 * instances based on a unique name. It caches the created instances to avoid redundant creation
 * and to ensure consistent use of the same instance for a given name.
 * </p>
 */
public class EmailClientFactory {

    private static final String CONF_PREFIX = "Digibib.EmailClient.";

    private static final String PROP_AUTH_USER = "Auth.User";

    private static final String PROP_AUTH_PASSWORD = "Auth.Password";

    private static final String PROP_SESSION_PREFIX = "Session.";

    private static ConcurrentHashMap<String, EmailClient> sessions = new ConcurrentHashMap<>();

    private EmailClientFactory() {

    }

    /**
     * Returns an {@link EmailClient} instance for the specified name.
     * If an instance does not already exist for the given name, it will be created and initialized.
     * The instance is cached for future use.
     *
     * @param name the unique name associated with the email client instance
     * @return the email client instance for the specified name
     */
    public static EmailClient getInstance(String name) {
        return sessions.computeIfAbsent(name, n -> new EmailClient(createSession(n)));
    }

    private static Session createSession(String name) {
        final Map<String, String> propertiesMap = MCRConfiguration2.getSubPropertiesMap(CONF_PREFIX + name + ".");
        Authenticator authenticator = null;
        if (propertiesMap.containsKey(PROP_AUTH_USER) && propertiesMap.containsKey(PROP_AUTH_PASSWORD)) {
            authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(propertiesMap.get(PROP_AUTH_USER),
                        propertiesMap.get(PROP_AUTH_PASSWORD));
                }
            };
        }
        return Session.getDefaultInstance(createSessionPropertiesMap(propertiesMap), authenticator);
    }

    private static Properties createSessionPropertiesMap(Map<String, String> propertiesMap) {
        final Properties properties = new Properties();
        propertiesMap.entrySet().stream().filter(p -> p.getKey().startsWith(PROP_SESSION_PREFIX)).forEach(e -> {
            final String key = "mail." + e.getKey().substring(PROP_SESSION_PREFIX.length());
            properties.setProperty(key, e.getValue());
        });
        return properties;
    }
}

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

package de.vzg.reposis.digibib.contactrequest.restapi;

import org.apache.logging.log4j.LogManager;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.mycore.common.config.MCRConfiguration2;
import org.mycore.frontend.jersey.access.MCRRequestScopeACLFilter;
import org.mycore.restapi.MCRCORSResponseFilter;
import org.mycore.restapi.MCRIgnoreClientAbortInterceptor;
import org.mycore.restapi.MCRSessionFilter;
import org.mycore.restapi.MCRTransactionFilter;

import de.vzg.reposis.digibib.contactrequest.ContactRequestConstants;

/**
 * Contact request REST app.
 */
public class ContactRestApp extends ResourceConfig {

    /**
     * Constructs new {@code ContactRestApp}.
     */
    public ContactRestApp() {
        super();
        initAppName();
        property(ServerProperties.APPLICATION_NAME, getApplicationName());
        packages(getRestPackages());
        property(ServerProperties.RESPONSE_SET_STATUS_OVER_SEND_ERROR, true);
        register(MCRSessionFilter.class);
        register(MCRTransactionFilter.class);
        register(ContactRestAuthorizationFilter.class);
        register(ContactRestFeature.class);
        register(ContactRestExceptionMapper.class);
        register(JsonMappingExceptionMapper.class);
        register(JsonParseExceptionMapper.class);
        register(MCRCORSResponseFilter.class);
        register(MCRRequestScopeACLFilter.class);
        register(MCRIgnoreClientAbortInterceptor.class);
    }

    private void initAppName() {
        setApplicationName("contact request API " + getVersion());
        LogManager.getLogger().info("Initiialize {}", getApplicationName());
    }

    private String getVersion() {
        return "1.0";
    }

    private String[] getRestPackages() {
        return MCRConfiguration2
            .getOrThrow(ContactRequestConstants.CONF_PREFIX + "API.Resource.Packages", MCRConfiguration2::splitValue)
            .toArray(String[]::new);
    }
}

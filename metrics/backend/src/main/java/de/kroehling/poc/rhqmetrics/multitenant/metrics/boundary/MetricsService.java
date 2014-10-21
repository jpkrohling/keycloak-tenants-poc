/*
 * Copyright (C) 2014 Juraci Paixão Kröhling <juraci at kroehling.de>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package de.kroehling.poc.rhqmetrics.multitenant.metrics.boundary;

import de.kroehling.poc.rhqmetrics.multitenant.metrics.entity.MetricsResponse;
import java.security.Identity;
import java.security.Principal;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.keycloak.KeycloakPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Juraci Paixão Kröhling <juraci at kroehling.de>
 */
@Stateless
@Path("/metrics")
@RolesAllowed({"agent"})
public class MetricsService {

    @Resource
    SessionContext sessionContext;

    Logger logger = LoggerFactory.getLogger(MetricsService.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MetricsResponse retrieve() {
        Principal principal = sessionContext.getCallerPrincipal();
        KeycloakPrincipal keycloakPrincipal = (KeycloakPrincipal) principal;
        String realm = keycloakPrincipal.getKeycloakSecurityContext().getToken().getIssuer();
        logger.warn("User for this call is: " + principal.getName());
        logger.warn("User belongs to this realm: " + realm);

        return new MetricsResponse("Success");
    }

}

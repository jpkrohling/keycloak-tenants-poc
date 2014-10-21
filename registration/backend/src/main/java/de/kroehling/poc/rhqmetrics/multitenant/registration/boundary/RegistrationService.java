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
package de.kroehling.poc.rhqmetrics.multitenant.registration.boundary;

import de.kroehling.poc.rhqmetrics.multitenant.registration.entity.RegistrationRequest;
import de.kroehling.poc.rhqmetrics.multitenant.registration.entity.RegistrationResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.util.Scanner;
import java.util.UUID;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import org.codehaus.jackson.io.JsonStringEncoder;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.util.BasicAuthHelper;
import org.keycloak.util.JsonSerialization;
import org.keycloak.util.KeycloakUriBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Juraci Paixão Kröhling <juraci at kroehling.de>
 */
@Stateless
@Path("/registration")
public class RegistrationService {

    private static final String KEYCLOAK_BASE_URL = "http://localhost:8180/auth";
    private static final Logger logger = LoggerFactory.getLogger(RegistrationService.class);
    private static final JsonStringEncoder jsonEncoder = JsonStringEncoder.getInstance();

    @POST
    public RegistrationResponse register(RegistrationRequest bean) throws MalformedURLException, IOException, URISyntaxException {
        String oauthSecret = UUID.randomUUID().toString();
        String nodeUsername = UUID.randomUUID().toString();
        String nodePassword = new BigInteger(130, new SecureRandom()).toString(32);

        InputStream realmJsonIs = getClass().getResourceAsStream("/realm-template.json");
        Scanner scanner = new java.util.Scanner(realmJsonIs).useDelimiter("\\A");
        String realmJsonTemplate = scanner.hasNext() ? scanner.next() : "";

        String realmJsonFormatted = String.format(
                realmJsonTemplate,
                new String(jsonEncoder.quoteAsString(bean.getAccountName())),
                new String(jsonEncoder.quoteAsString(bean.getAdminUsername())),
                new String(jsonEncoder.quoteAsString(bean.getAdminPassword())),
                nodeUsername,
                nodePassword,
                oauthSecret);

        AccessTokenResponse token = getToken();

        RegistrationResponse response;
        if (null == token) {
            return new RegistrationResponse("Couldn't contact the Keycloak server. Try again later.");
        }

        if (!accountNameAvailable(token, bean.getAccountName())) {
            return new RegistrationResponse("There's already an account with this name. Please, choose a different name");
        }

        String authorization = String.format("Bearer %s", token.getToken());

        URL createRealmURL = new URL(KEYCLOAK_BASE_URL + "/admin/realms");
        HttpURLConnection connection = (HttpURLConnection) (createRealmURL.openConnection());
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setReadTimeout(5000); // 5 seconds
        connection.setConnectTimeout(5000); // 5 seconds
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", authorization);
        connection.setRequestProperty("Content-Type", MediaType.APPLICATION_JSON);

        try (OutputStream output = connection.getOutputStream(); OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8")) {
            writer.write(realmJsonFormatted);
        }

        connection.connect();

        int code = connection.getResponseCode();

        if (code == 201) {
            response = new RegistrationResponse(nodeUsername, nodePassword, oauthSecret);
        } else {
            response = new RegistrationResponse(String.format("Couldn't register a new realm. Reason: %s", connection.getResponseMessage()));
        }

        return response;
    }

    private boolean accountNameAvailable(AccessTokenResponse token, String accountName) throws MalformedURLException, IOException {
        URL createRealmURL = new URL(String.format("%s/admin/realms/%s", KEYCLOAK_BASE_URL, URLEncoder.encode(accountName, "UTF-8")));
        String authorization = String.format("Bearer %s", token.getToken());

        HttpURLConnection connection = (HttpURLConnection) (createRealmURL.openConnection());
        connection.setReadTimeout(5000); // 5 seconds
        connection.setConnectTimeout(5000); // 5 seconds
        connection.setRequestProperty("Authorization", authorization);
        connection.connect();

        int code = connection.getResponseCode();
        connection.disconnect();
        // being conservative here: the only status code we are interested is 404,
        // anything else we consider an error and we report as if the account exists
        return code == 404;
    }

    private AccessTokenResponse getToken() throws IOException {
        String keycloakUsername = "registration";
        String keycloakPassword = "registration";
        String keycloakOAuthClient = "registration";
        String keycloakOAuthSecret = "dbd9f70a-f7a7-4575-80d9-5ac9ba908052";

        URI keycloakUri = KeycloakUriBuilder.fromUri(KEYCLOAK_BASE_URL).path("/realms/{realm-name}/tokens/grants/access").build("master");
        String authorization = BasicAuthHelper.createHeader(keycloakOAuthClient, keycloakOAuthSecret);

        HttpURLConnection connection = (HttpURLConnection) (keycloakUri.toURL().openConnection());
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setReadTimeout(5000); // 5 seconds
        connection.setConnectTimeout(5000); // 5 seconds
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", authorization);

        try (OutputStream output = connection.getOutputStream(); OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8")) {
            writer.write(String.format("username=%s&password=%s", keycloakUsername, keycloakPassword));
        }

        connection.connect();

        int code = connection.getResponseCode();

        if (code == 200) {
            try (InputStream keycloakResponse = connection.getInputStream()) {
                return JsonSerialization.readValue(keycloakResponse, AccessTokenResponse.class);
            }
        } else {
            logger.error("Error while trying to retrieve the access token: {}", connection.getResponseMessage());
        }

        return null;
    }

}

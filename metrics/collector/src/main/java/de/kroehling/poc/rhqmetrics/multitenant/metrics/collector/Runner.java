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
package de.kroehling.poc.rhqmetrics.multitenant.metrics.collector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
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
public class Runner {
    // change from here:
    private final String accountName = "finances";
    private final String keycloakUsername = "7be9abd0-0bf3-4ce9-9bc7-7be35df57be1";
    private final String keycloakPassword = "tchfsgvhcr4i7d82t1nah9vd84";
    private final String keycloakOAuthSecret = "57fcdd33-d5a5-497e-8b6d-9b0234ff0f51";
    // to here

    private final String keycloakOAuthClient = "metrics-collector";

    private static final String KEYCLOAK_BASE_URL = "http://localhost:8180/auth";
    private static final String METRICS_BASE_URL = "http://localhost:8080/metrics-backend/metrics";
    Logger logger = LoggerFactory.getLogger(Runner.class);

    public static void main(String args[]) throws IOException {
        Runner runner = new Runner();
        runner.run();
    }

    public void run() throws IOException {
        AccessTokenResponse token = getToken();
        String authorization = String.format("Bearer %s", token.getToken());
        URL recordMetricsUrl = new URL(METRICS_BASE_URL);
        HttpURLConnection connection = (HttpURLConnection) (recordMetricsUrl.openConnection());
        connection.setReadTimeout(500000); // 5 seconds
        connection.setConnectTimeout(500000); // 5 seconds
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-RHQ-Realm", accountName);
        connection.setRequestProperty("Authorization", authorization);
        connection.connect();

        int code = connection.getResponseCode();

        if (code == 200) {
            logger.info("Worked");
        } else {
            logger.info("Didn't work: " + connection.getResponseMessage());
        }

        connection.disconnect();
    }

    private AccessTokenResponse getToken() throws IOException {
        URI keycloakUri = KeycloakUriBuilder.fromUri(KEYCLOAK_BASE_URL).path("/realms/{realm-name}/tokens/grants/access").build(accountName);
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

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
package de.kroehling.poc.rhqmetrics.multitenant.registration.entity;

/**
 *
 * @author Juraci Paixão Kröhling <juraci at kroehling.de>
 */
public class RegistrationResponse {
    private String nodeUsername;
    private String nodePassword;
    private String oauthSecret;

    private String errorMessage;

    public RegistrationResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public RegistrationResponse(String nodeUsername, String nodePassword, String oauthSecret) {
        this.nodeUsername = nodeUsername;
        this.nodePassword = nodePassword;
        this.oauthSecret = oauthSecret;
    }

    public RegistrationResponse() {
    }

    public String getNodeUsername() {
        return nodeUsername;
    }

    public void setNodeUsername(String nodeUsername) {
        this.nodeUsername = nodeUsername;
    }

    public String getNodePassword() {
        return nodePassword;
    }

    public void setNodePassword(String nodePassword) {
        this.nodePassword = nodePassword;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getOauthSecret() {
        return oauthSecret;
    }

    public void setOauthSecret(String oauthSecret) {
        this.oauthSecret = oauthSecret;
    }
}

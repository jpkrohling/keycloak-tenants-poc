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
public class RegistrationRequest {
    private String accountName;
    private String adminUsername;
    private String adminPassword;

    public RegistrationRequest(String accountName, String adminUsername, String adminPassword) {
        this.accountName = accountName;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    public RegistrationRequest() {
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    
}

Registration module - Backend
=========

This module shows how to interact with the Keycloak REST API to create a Realm
on demand, based on the information entered on the "frontend" module, using the
template "realm-template.json". 

For this to work, you'll need a Keycloak server v. 1.0.1.Final running on
localhost:8180 and configured with the following:

- an user, named "registration" and password "registration"
- an OAuth client, named "registration" and with OAuth secret
"dbd9f70a-f7a7-4575-80d9-5ac9ba908052". As you can't create an OAuth secret with
this value, you can change the Registration service (keycloakOAuthSecret).
- The "master" realm should allow "Direct Grant API" (Settings -> Login)
- The "registration" user should have the "create-realm" role. (Users -> view
all -> registration -> Role Mappings)
- The OAuth client "registration" should have the "create-realm" scope (OAuth
clients -> registration -> Scope).

Note that this uses Keycloak 1.0.1.Final as the target server. The URLs *have*
changed for 1.1.0.Alpha1-SNAPSHOT, so, it will not work against a latest
Keycloak server.

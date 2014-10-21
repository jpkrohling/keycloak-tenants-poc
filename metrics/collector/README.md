Metrics module - Collector
=========

This is a sample third-party application that would access a business backend.
It is implemented as an OAuth client. To get it working, you'll need first to
"register" using the registration module. In the end of the registration,
there will be a set of values that you'll need to replace at the Runner:

- accountName : this is the account name (realm, in Keycloak)
- keycloakUsername : this is the "node's username" on the registration
- keycloakPassword : this is the "node's password" on the registration
- keycloakOAuthSecret : this is the "oauth's secret" on the registration

This first uses the Direct Grant API to get a token from the Keycloak server
based on a username/password combination. This token is then used in the
Authorization header of the request to be made to the business application. This
is a regular Bearer token. It also sends another HTTP header, named
"X-RHQ-Realm" with the Realm name (accountName), so that the server knows which
realm to authenticate this user. 
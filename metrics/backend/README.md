Metrics module - Backend
=========

This backend is a simple Java EE application. In a real application, it would be
the business backend. In our case, it's simply a JAX-RS endpoint that sends a
simple bean back as JSON, but prints who is the user that made the request
and to which realm it belongs.

The interesting part of this module is:

- RHQKeycloakConfigResolver : it's used by Keycloak to figure out the realm.
- WEB-INF/web.xml : with a context param, named "keycloak.config.resolver",
specifying the class name for the resolver.
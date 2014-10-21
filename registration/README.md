Registration module
=========

This module contains a very simple registration flow for new accounts. The
backend is a simple JAX-RS endpoint, which creates a bunch of passwords and uses
a JSON template to create a new Realm on Keycloak, based on the information the
user entered on the frontend. At the end of the process, the frontend displays
the passwords for the user. Those credentials should be used on the "Collector"
module. 

More details are available on each module's README.
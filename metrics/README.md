Metrics module
=========

This module contains the main business code for an application. In our case, we
have a Java EE backend secured by Keycloak and an agent that runs on nodes and
sends metrics to the backend. The agent sends a specific HTTP Header to the
backend, specifying which realm this agent belongs to.

Specific instructions on the README file for each module.
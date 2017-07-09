VPN Controller
==============

An application to manage local VPN connection written in Kotlin using Spring 5, Project Reactor and some other toys. 

**WARNING** The application was created in order to check out some fancy Java stuff. Currently
it is tailored for my environment, has limited functionality and is buggy.

Application exposes a REST API with following resources:
* `GET /vpn/country/{code}`
* `GET /vpn/country/{code}/sorted`
* `GET /vpn/country/{code}/best`
* `GET /vpn/active`
* `PUT /vpn/switch-to/better`
* `PUT /vpn/switch-to/country/{code}`

TODO list
---------

In order of importance:
* Gather all vpn provider related facts in a single adapter
* Remove hardcodes for specific vpn provider
* Implement simple GUI

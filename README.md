# DyDNS
Small project to keep DNS records updated on dynamic IP. Written originally for No-IP, but now supporting also DynDNS and DuckDNS.
Wrote it because No-IP's Dynamic Update Client does not support FreeBSD (my server's OS) and I'm trying to learn Java so it was convenient.

To build you will need OpenJDK 17 and Maven. Clone the project repo, cd into the project folder, and issue these commands:

mvn compile
mvn package

This will create a folder named target and the jar executable package will be inside.

To set up DyDNS on a system, just run it (as root on Unix-like systems) and it will prompt you for informations such as DDNS provider, IP lookup service, and authentication credentials. Then, manually set up a cron job or a task to run DyDNS with the desired frequency to check and update DNS records.

Recommended: on Unix systems, run as user nobody after setting it up as root. You will need to modify ownership for the log folder and log files inside it.

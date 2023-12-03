# dualboot-ddns
Small project to keep DNS records updated on dynamic IP. Written for No-IP, but might get support for others DDNS providers.
Wrote it because No-IP's Dynamic Update Client does not support FreeBSD (my server's OS) and I'm trying to learn Java so it was convenient.

To build, clone the project, cd into the folder, and issue these commands:

javac Main.java
jar cfm dydns.jar MANIFEST.MF *.class

This will generate the executable dydns.jar

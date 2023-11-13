# Hotel reservation app in Java with different database solutions

Build a simple Java app with a CLI frontend for PostgreSQL, ObjectDB and MongoDB versions. Add a static frontend for one of those lastly.

## Project structure

Find the sub-projects inside `hotel-res-app/` with the name according to the db stack used. No project manager was used except for Maven in the MongoDB app.

Libs aren't provided so you would need to fetch the proper  JDBC driver (4.2), postgresql(11.2) and objectdb (2.8.8) .jar files. For MongoDB, see the pom.xml file.

## Overview

You can either work in a local server/db or a remote. See each `Connexion.java` for details on how to connect.

CLI interface for pgsql, odb and mongodb accepts either interactive commands or a .txt file with a single command on each line (// for non executable lines). Connection to the db is ended when providing a `quitter` command.

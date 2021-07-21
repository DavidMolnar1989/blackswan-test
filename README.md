# blackswan-test
Black Swan Java test
author: David Molnar

---------------------------------------------------------------------------------------------------------------------------------
Specification
https://bitbucket.org/one-way/blackswan-tests/src/master/bs-mid-to-senior-java-dev.md
---------------------------------------------------------------------------------------------------------------------------------
Prerequsities before run the application
- JDK 11 for compilation
- mvn clean install
	- in case of prod profile please use docker
	- in case of test/local/dev profile please use standard application 
	
	- note:
		- possible MapStruct exception could occur in case of Intellij IDEA usage:
			https://stackoverflow.com/questions/65112406/intellij-idea-mapstruct-java-internal-error-in-the-mapping-processor-java-lang/65113549#65113549
		- if the exception occurs, please add "-Djps.track.ap.dependencies=false" to the compiler VM Options
---------------------------------------------------------------------------------------------------------------------------------
Bonus Task
- there is a scheduled job (TaskBackgroundService) which operates on every Pending, active, expired Task and moves them to Done state
- this job runs in every minute
	- note: in real world usage the job would wait longer time
---------------------------------------------------------------------------------------------------------------------------------
Production readiness
- The application can be started with 4 profiles:
   - local, test: the app creates the DDL's automatically at start and the database is stored in a file (testLocalDB)
   - dev: the app uses an existing DB (testDevDB - src/main/resources/db/testDevDB.mv.db), which was migrated from local: the Liquibase migration scripts can be found under src/main/resources/db/changelog/dev
   - prod: the app can be started using docker (Dockerfile provided), it uses the existing testDevDB to create migration script using Liquibase, then inserts it automatically.
		    - note: In case of real world usage, prod database would be a real RDBMS. Migration would be more realistic.
---------------------------------------------------------------------------------------------------------------------------------
Continerization and Data migration
- Dockerfile provided which starts the application with prod profile
- for Data migration Liquibase was used
- the provided testDevDB contains the business tables, which will be migrated to the testProdDB during the docker build
---------------------------------------------------------------------------------------------------------------------------------
Code Coverage
 - Jacoco was used to measure code coverage, which is currently above 90%
 - Jacoco report can be found under target/jacoco/index.html after mvn clean install
---------------------------------------------------------------------------------------------------------------------------------
Further development
- create a scheduled background job which permanently deletes the inactive Tasks (activeFlag = false)
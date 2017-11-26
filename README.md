# ECE658 Project

## PotLucky

### Build

From the root of the project, the project can be built and packaged in a `.war`
file using the following command:

	$ mvn package

This command will also run all test suites to verify that the project is ready
to be deployed. Failing a test case will halt the build. To skip testing, append
the `-DskipTests` flag to the build command:

	$ mvn package -DskipTests

This command will produce the packaged project `.war` file inside the child
`target` sub-directory.

### Deploy

To deploy the project application, first start the glassfish server:

	# asadmin start-domain --verbose

Note that the `asadmin` command requires administrative privileges to run.

Then, form the root of the project, deploy the application:

	# asadmin deploy --name potlucky --contextroot /potlucky target/*.war

You should then be able to access the deployed application by navigating to
`http://localhost:8080/potlucky/` in a web browser. If the application is
already deployed, and you wish to updated the deployed application to a newer
version, use the `redeploy` command:

	# asadmin redeploy --name potlucky --contextroot /potlucky target/*.war

# Spring Mobile

[Spring Mobile] is an extension to [Spring MVC] that aims to simplify the development of mobile web applications.


## Downloading Artifacts

See [downloading Spring artifacts] for Maven repository information. Unable to use Maven or other transitive dependency management tools? See [building a distribution with dependencies].

### Dependencies

	<dependency>
	    <groupId>org.springframework.mobile</groupId>
	    <artifactId>spring-mobile-device</artifactId>
	    <version>${org.springframework.mobile-version}</version>
	</dependency>

### Repositories

	<repository>
		<id>spring-repo</id>
		<name>Spring Repository</name>
		<url>http://repo.springsource.org/release</url>
	</repository>	
		
	<repository>
		<id>spring-milestone</id>
		<name>Spring Milestone Repository</name>
		<url>http://repo.springsource.org/milestone</url>
	</repository>
	
	<repository>
		<id>spring-snapshot</id>
		<name>Spring Snapshot Repository</name>
		<url>http://repo.springsource.org/snapshot</url>
	</repository>


## Documentation

See the current [Javadoc] and [reference docs].


## Sample Applications

Several example projects are available in the [samples repository].


## Issue Tracking

Report issues via the [Spring Mobile JIRA]. While JIRA is preferred, [GitHub issues] are also welcome. Understand our issue management process by reading about [the lifecycle of an issue].


## Build from Source

1. Clone the repository from GitHub:

		$ git clone git://github.com/SpringSource/spring-mobile.git

2. Navigate into the cloned repository directory:

		$ cd spring-mobile

3. The project uses [Gradle] to build:

		$ ./gradlew build
		
4. Install jars into your local Maven cache (optional)

		$ ./gradlew install


## Import Source into your IDE

### Eclipse

1. To generate Eclipse metadata (.classpath and .project files):

		$ ./gradlew eclipse

2. Once complete, you may then import the projects into Eclipse as usual:

		File -> Import -> Existing projects into workspace

> **Note:** [Spring Tool Suite] has built in support for [Gradle], and you can simply import as Gradle projects.

### IDEA

Generate IDEA metadata (.iml and .ipr files):

	$ ./gradlew idea


## Contributing

[Pull requests] are welcome. See the [contributor guidelines] for details.


## License

[Spring Mobile] is released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0).


[Spring Mobile]: http://www.springsource.org/spring-mobile
[Spring MVC]: http://static.springsource.org/spring/docs/current/spring-framework-reference/html/mvc.html
[downloading Spring artifacts]: https://github.com/SpringSource/spring-framework/wiki/Downloading-Spring-artifacts
[building a distribution with dependencies]: https://github.com/SpringSource/spring-framework/wiki/Building-a-distribution-with-dependencies
[Javadoc]: http://static.springsource.org/spring-mobile/docs/current/api/
[reference docs]: http://static.springsource.org/spring-mobile/docs/current/reference/html/
[samples repository]: https://github.com/SpringSource/spring-mobile-samples
[Spring Mobile JIRA]: http://jira.springsource.org/browse/MOBILE
[GitHub issues]: https://github.com/SpringSource/spring-mobile/issues?direction=desc&sort=created&state=open
[the lifecycle of an issue]: https://github.com/cbeams/spring-framework/wiki/The-Lifecycle-of-an-Issue
[Gradle]: http://gradle.org
[Spring Tool Suite]: http://www.springsource.com/developer/sts
[Pull requests]: http://help.github.com/send-pull-requests
[contributor guidelines]: https://github.com/SpringSource/spring-mobile/wiki/Contributor-Guidelines

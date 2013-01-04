# Spring Mobile

[Spring Mobile] is an extension to [Spring MVC] that aims to simplify the development of mobile web applications.

## Downloading artifacts
See [downloading Spring artifacts] for Maven repository information. Unable to
use Maven or other transitive dependency management tools? See [building a
distribution with dependencies].

## Documentation
See the current [Javadoc] and [reference docs].

## Issue Tracking
Report issues via the [Spring Mobile JIRA]. Understand our issue management
process by reading about [the lifecycle of an issue].

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

To generate Eclipse metadata (.classpath and .project files):

	$ ./gradlew eclipse

Once complete, you may then import the projects into Eclipse as usual:

	File -> Import -> Existing projects into workspace

Alternatively, [SpringSource Tool Suite] has built in support for [Gradle], and you can simply import as Gradle projects.

### IDEA

Generate IDEA metadata (.iml and .ipr files):

	$ ./gradlew idea

## Contributing
[Pull requests] are welcome. See the [contributor guidelines] for details.

## License
Spring Mobile is released under version 2.0 of the [Apache License](http://www.apache.org/licenses/LICENSE-2.0).

[Spring Mobile]: http://www.springsource.org/spring-mobile
[Spring MVC]: http://static.springsource.org/spring/docs/current/spring-framework-reference/html/mvc.html
[downloading Spring artifacts]: https://github.com/SpringSource/spring-framework/wiki/Downloading-Spring-artifacts
[building a distribution with dependencies]: https://github.com/SpringSource/spring-framework/wiki/Building-a-distribution-with-dependencies
[Javadoc]: http://static.springsource.org/spring-mobile/docs/1.1.x/api/
[reference docs]: http://static.springsource.org/spring-mobile/docs/1.1.x/reference/html/
[Spring Mobile JIRA]: http://jira.springsource.org/browse/MOBILE
[the lifecycle of an issue]: https://github.com/cbeams/spring-framework/wiki/The-Lifecycle-of-an-Issue
[Gradle]: http://gradle.org
[SpringSource Tool Suite]: http://www.springsource.com/developer/sts
[Pull requests]: http://help.github.com/send-pull-requests
[contributor guidelines]: https://github.com/SpringSource/spring-mobile/wiki/Contributor-Guidelines

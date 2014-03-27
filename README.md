# Spring Mobile

[!["Build Status"](https://build.spring.io/plugins/servlet/buildStatusImage/MOBILE-MASTER)](https://build.spring.io/browse/MOBILE-MASTER)

[Spring Mobile] is an extension of the [Spring Framework] and [Spring Web MVC] that aims to simplify the development of mobile web applications.


## Features

- A device resolver abstraction for server-side detection of mobile and tablet devices

- Site preference management that allows the user to indicate if he or she prefers a "normal", "mobile", or "tablet" experience

- A site switcher capable of switching the user to the most appropriate site, either mobile, tablet, or normal, based on his or her device and optionally indicated site preference

- Device aware view management for organizing and managing different views for specific devices


## Download Artifacts

See [downloading Spring artifacts] for Maven repository information. Unable to use Maven or other transitive dependency management tools? See [building a distribution with dependencies].

### Dependencies

```xml
<dependency>
    <groupId>org.springframework.mobile</groupId>
    <artifactId>spring-mobile-device</artifactId>
    <version>${org.springframework.mobile-version}</version>
</dependency>
```

### Repositories

```xml
<repository>
    <id>spring-repo</id>
    <name>Spring Repository</name>
    <url>http://repo.spring.io/release</url>
</repository>   
    
<repository>
    <id>spring-milestone</id>
    <name>Spring Milestone Repository</name>
    <url>http://repo.spring.io/milestone</url>
</repository>

<repository>
    <id>spring-snapshot</id>
    <name>Spring Snapshot Repository</name>
    <url>http://repo.spring.io/snapshot</url>
</repository>
```


## Documentation

See the current [Javadoc] and [reference docs].


## Sample Applications

Several example projects are available in the [samples repository].


## Issue Tracking

Report issues via the [Spring Mobile JIRA]. While JIRA is preferred, [GitHub issues] are also welcome. Understand our issue management process by reading about [the lifecycle of an issue].


## Build from Source

1. Clone the repository from GitHub:

    ```sh
    $ git clone https://github.com/spring-projects/spring-mobile.git
    ```

2. Navigate into the cloned repository directory:

    ```sh
    $ cd spring-mobile
    ```

3. The project uses [Gradle] to build:

    ```sh
    $ ./gradlew build
    ```
        
4. Install jars into your local Maven cache (optional)

    ```sh
    $ ./gradlew install
    ```


## Import Source into your IDE

### Eclipse

1. To generate Eclipse metadata (.classpath and .project files):

    ```sh
    $ ./gradlew eclipse
    ```

2. Once complete, you may then import the projects into Eclipse as usual:

   ```
   File -> Import -> Existing projects into workspace
   ```

> **Note**: [Spring Tool Suite][sts] has built in support for [Gradle], and you can simply import as Gradle projects.

### IDEA

Generate IDEA metadata (.iml and .ipr files):

```sh
$ ./gradlew idea
```


## Contribute

[Pull requests] are welcome. See the [contributor guidelines] for details.


## License

[Spring Mobile] is released under version 2.0 of the [Apache License].


[Spring Mobile]: http://projects.spring.io/spring-mobile
[Spring Framework]: http://projects.spring.io/spring-framework
[Spring Web MVC]: http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html
[downloading Spring artifacts]: https://github.com/spring-projects/spring-framework/wiki/Downloading-Spring-artifacts
[building a distribution with dependencies]: https://github.com/spring-projects/spring-framework/wiki/Building-a-distribution-with-dependencies
[Javadoc]: http://docs.spring.io/spring-mobile/docs/current/api/
[reference docs]: http://docs.spring.io/spring-mobile/docs/current/reference/html/
[samples repository]: https://github.com/spring-projects/spring-mobile-samples
[Spring Mobile JIRA]: http://jira.springsource.org/browse/MOBILE
[GitHub issues]: https://github.com/spring-projects/spring-mobile/issues
[the lifecycle of an issue]: https://github.com/spring-projects/spring-framework/wiki/The-Lifecycle-of-an-Issue
[Gradle]: http://gradle.org
[sts]: https://spring.io/tools
[Pull requests]: http://help.github.com/send-pull-requests
[contributor guidelines]: https://github.com/spring-projects/spring-mobile/wiki/Contributor-Guidelines
[Apache License]: http://www.apache.org/licenses/LICENSE-2.0

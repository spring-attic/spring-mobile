# Spring Mobile

[!["Build Status"](https://build.spring.io/plugins/servlet/wittified/build-status/MOBILE-MASTER)](https://build.spring.io/browse/MOBILE-MASTER)

[Spring Mobile] is an extension of the [Spring Framework] and [Spring Web MVC] that aims to simplify the development of mobile web applications.


## Features

- A device resolver abstraction for server-side detection of mobile and tablet devices

- Site preference management that allows the user to indicate if he or she prefers a "normal", "mobile", or "tablet" experience

- A site switcher capable of switching the user to the most appropriate site, either mobile, tablet, or normal, based on his or her device and optionally indicated site preference

- Device aware view management for organizing and managing different views for specific devices


## Download Artifacts

Include the `spring-mobile-starter` in your Spring Boot application to enable Spring Mobile's Auto-Configuration. See [downloading Spring artifacts] for Maven repository information. Unable to use Maven or other transitive dependency management tools? See [building a distribution with dependencies].


### Gradle

```groovy
dependencies {
    compile("org.springframework.mobile:spring-mobile-starter:2.0.0.M2")
}
repositories {
    maven { url "https://repo.spring.io/milestone" }
}
```

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.mobile</groupId>
        <artifactId>spring-mobile-starter</artifactId>
        <version>2.0.0.M2</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>spring-milestones</id>
        <name>Spring Milestones</name>
        <url>https://repo.spring.io/milestone</url>
    </repository>
</repositories>
```


## Documentation

See the current [Javadoc] and [reference docs].


## Sample Applications

Several example projects are available in the [samples repository].


## Getting Started Guides

The [spring.io] web site contains many [getting started guides][guides] that cover a broad range of topics. 

- [Serving Mobile Web Content with Spring MVC](https://spring.io/guides/gs/serving-mobile-web-content/)
- [Detecting a Device](https://spring.io/guides/gs/device-detection/)
- [Handling Web Site Preference](https://spring.io/guides/gs/handling-site-preference/)


## Support

Review the [spring-mobile][spring-mobile tag] tag on [Stack Overflow].


## Issue Tracking

Report issues via [GitHub issues]. Understand our issue management process by reading about [the lifecycle of an issue].


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


## Stay in Touch

Follow [@SpringCentral] as well as [@SpringFramework] on Twitter. In-depth articles can be found at [The Spring Blog], and releases are announced via our [news feed].


## License

[Spring Mobile] is released under version 2.0 of the [Apache License].


[Spring Mobile]: https://projects.spring.io/spring-mobile
[Spring Framework]: https://projects.spring.io/spring-framework
[Spring Web MVC]: https://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html
[downloading Spring artifacts]: https://github.com/spring-projects/spring-framework/wiki/Downloading-Spring-artifacts
[building a distribution with dependencies]: https://github.com/spring-projects/spring-framework/wiki/Building-a-distribution-with-dependencies
[Javadoc]: https://docs.spring.io/spring-mobile/docs/current/api/
[reference docs]: https://docs.spring.io/spring-mobile/docs/current/reference/html/
[samples repository]: https://github.com/spring-projects/spring-mobile-samples
[Spring forums]: https://forum.spring.io/forum/spring-projects/web/mobile
[spring-mobile tag]: https://stackoverflow.com/questions/tagged/spring-mobile
[Stack Overflow]: https://stackoverflow.com/faq
[spring.io]: https://spring.io
[guides]: https://spring.io/guides
[GitHub issues]: https://github.com/spring-projects/spring-mobile/issues
[the lifecycle of an issue]: https://github.com/spring-projects/spring-framework/wiki/The-Lifecycle-of-an-Issue
[Gradle]: https://gradle.org
[sts]: https://spring.io/tools
[Pull requests]: https://help.github.com/send-pull-requests
[contributor guidelines]: CONTRIBUTING.md
[@SpringFramework]: https://twitter.com/springframework
[@SpringCentral]: https://twitter.com/springcentral
[The Spring Blog]: https://spring.io/blog/
[news feed]: https://spring.io/blog/category/news
[Apache License]: https://www.apache.org/licenses/LICENSE-2.0

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

> **Note:** You do not need to include all three repositories, rather select the one that corresponds to the release type of the dependency.

### Gradle

```groovy
dependencies {
    compile("org.springframework.mobile:spring-mobile-device:${springMobileVersion}")
}
repositories {
    maven { url "http://repo.spring.io/release" }
    maven { url "http://repo.spring.io/milestone" }
    maven { url "http://repo.spring.io/snapshot" }
}
```

### Maven

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.mobile</groupId>
        <artifactId>spring-mobile-device</artifactId>
        <version>${org.springframework.mobile-version}</version>
    </dependency>
</dependencies>

<repositories>
    <repository>
        <id>spring-repo</id>
        <name>Spring Repository</name>
        <url>http://repo.spring.io/release</url>
    </repository>
    <repository>
        <id>spring-milestones</id>
        <name>Spring Milestones</name>
        <url>http://repo.spring.io/milestone</url>
    </repository>
    <repository>
        <id>spring-snapshots</id>
        <name>Spring Snapshots</name>
        <url>http://repo.spring.io/snapshot</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
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

Check out the [Spring forums] and the [spring-mobile][spring-mobile tag] tag on [Stack Overflow]. [Commercial support] is also available.


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


## Stay in Touch

Follow [@SpringCentral] as well as [@SpringFramework] on Twitter. In-depth articles can be found at [The Spring Blog], and releases are announced via our [news feed].


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
[Spring forums]: http://forum.spring.io/forum/spring-projects/web/mobile
[spring-mobile tag]: http://stackoverflow.com/questions/tagged/spring-mobile
[Stack Overflow]: http://stackoverflow.com/faq
[Commercial support]: http://spring.io/services
[Spring Mobile JIRA]: http://jira.spring.io/browse/MOBILE
[spring.io]: http://spring.io
[guides]: http://spring.io/guides
[GitHub issues]: https://github.com/spring-projects/spring-mobile/issues
[the lifecycle of an issue]: https://github.com/spring-projects/spring-framework/wiki/The-Lifecycle-of-an-Issue
[Gradle]: http://gradle.org
[sts]: https://spring.io/tools
[Pull requests]: http://help.github.com/send-pull-requests
[contributor guidelines]: CONTRIBUTING.md
[@SpringFramework]: https://twitter.com/springframework
[@SpringCentral]: https://twitter.com/springcentral
[The Spring Blog]: http://spring.io/blog/
[news feed]: http://spring.io/blog/category/news
[Apache License]: http://www.apache.org/licenses/LICENSE-2.0

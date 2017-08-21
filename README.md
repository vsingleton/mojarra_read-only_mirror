# Mojarra

Oracle's implementation of the JavaServer Faces specification 


## Minimum Requirements

- Java 1.8
- Servlet 3.0 (4.0 recommended)
- EL 3.0 (3.1 recommended)
- CDI 1.2 (2.0 recommended)
- JSTL 1.2
- JSONP 1.1 (optional, only when `<f:websocket>` is used)
- BV 1.1 (optional, only when `<f:validateBean>` or `<f:validateWholeBean>` is used; 2.0 recommended)

Servlet 4.0 will enable JSF 2.3 to serve resources via HTTP/2 push. CDI is explicitly required because since JSF 2.3 the `javax.faces.bean.*` annotations such as `@ManagedBean` are **deprecated**, and several implicit EL objects are produced via CDI producers, and `<f:websocket>` manages the WS sessions and events via CDI.


## Installation

Depending on the server used, JSF may already be built-in (full fledged Java EE containers such as [WildFly][1], [JBoss EAP][2], [TomEE][3], [Payara][4], [GlassFish][5], [Liberty][6], etc.), or not (barebones JSP/Servlet containers such as [Tomcat][7], [Jetty][8], etc.). If the server doesn't ship with JSF built-in, then you need to manually install JSF 2.3 along with CDI 1.2+, JSONP 1.1+ and JSTL 1.2+ as those servlet containers usually also don't even ship with those JSF dependencies.

### Non-Maven

In case you're manually carrying around JARs:

- **Java EE containers (WildFly, JBoss EAP, TomEE, Payara, GlassFish, Liberty, etc)**

    You don't need to add any JARs to `/WEB-INF/lib`!

- **Servletcontainers (Tomcat, Jetty, etc)**

    Add below JARs to `/WEB-INF/lib`:

    - [`javax.faces.2.3.x.jar`][9]
    - [`weld-servlet-shaded-3.0.0.Final.jar`][10]
    - [`jstl-1.2.jar`][11]
    - [`javax.json-api-1.1.jar`][12] (optional, only when `<f:websocket>` is used)
    - [`validation-api-2.0.0.Final.jar`][13] (optional, only when `<f:validateBean|validateWholeBean>` is used)
    - [`hibernate-validator-6.0.1.Final.jar`][14] (optional, only when `<f:validateBean|validateWholeBean>` is used)

    Substitute `x` with latest 2.3.x version number.

### Maven

In case you're using Maven, you can find below the necessary coordinates:

- **Java EE containers (WildFly, JBoss, TomEE, Payara, GlassFish, WebSphere, etc)**

    ```xml
    <dependency>
        <groupId>javax</groupId>
        <artifactId>javaee-web-api</artifactId>
        <version>7.0</version>
        <scope>provided</scope>
    </dependency>
    ```

    Note that Java EE 8.0 is currently not available yet. You should for now manually upgrade any JSF 2.2 library to JSF 2.3 depending on the server used.

- **Servletcontainers (Tomcat, Jetty, etc)**

    ```xml
    <dependency>
        <groupId>org.glassfish</groupId>
        <artifactId>javax.faces</artifactId>
        <version><!-- Use latest 2.3.x version. --></version>
    </dependency>
    <dependency>
        <groupId>org.jboss.weld.servlet</groupId>
        <artifactId>weld-servlet-shaded</artifactId>
        <version>3.0.0.Final</version>
    </dependency>
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>
        <version>1.2</version>
    </dependency>
    <dependency> <!-- Optional, only when <f:websocket> is used. -->
        <groupId>javax.json</groupId>
        <artifactId>javax.json-api</artifactId>
        <version>1.1</version>
    </dependency>
    <dependency> <!-- Optional, only when <f:validateBean> or <f:validateWholeBean> is used. -->
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-validator</artifactId>
        <version>6.0.1.Final</version>
    </dependency>
    ```

    You can check [`org.glassfish:javax.faces`][15] repository to find current latest Mojarra 2.3.x version.


## Hello World Example

We assume that you already know how to create an empty Maven WAR Project or Dynamic Web Project in your favourite IDE with a `/WEB-INF/web.xml` deployment descriptor file. Don't forget to add JARs or configure pom.xml if necessary, as instructed in previous chapter.

### Controller

Optionally, register the `FacesServlet` in `/WEB-INF/web.xml` as below:

```xml
<servlet>
    <servlet-name>facesServlet</servlet-name>
    <servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>facesServlet</servlet-name>
    <url-pattern>*.xhtml</url-pattern>
</servlet-mapping>
```

Noted should be that JSF 2.2+ is already "implicitly" registered and mapped on `*.jsf`, `*.faces` and `/faces/*` when running on a Servlet 3.0+ container. This will be overridden altogether when explicitly registering as above. [The `*.xhtml` URL pattern is preferred over above for security and clarity reasons][16]. JSF 2.3+ adds `*.xhtml` to set of default patterns, hence the `FacesServlet` registration being optional. But when you don't explicitly map it on `*.xhtml`, then people can still access JSF pages using `*.jsf`, `*.faces` or `/faces/*` URL patterns. This is not nice for SEO as JSF by design doesn't 301-redirect them to a single mapping.

### Model

Then create a backing bean class as below:

```java
package com.example;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class Hello {

    private String name;
    private String message;

    public void createMessage() {
        message = "Hello, " + name + "!";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

}
```

Noted should be that in reality in the average Java EE application the above "model" is further breakdown into a JPA entity, an EJB service and a smaller backing bean. The JPA entity and EJB service then basically act as a true "model" and the backing bean becomes a "controller" for that model. This may in first place be confusing to starters, but it all depends on the point of view. See also [What components are MVC in JSF MVC framework?][17] and [JSF Controller, Service and DAO][18].

### View

Finally create a [Facelets][19] file `/hello.xhtml` as below:

```xml
<!DOCTYPE html>
<html lang="en"
    xmlns="http://www.w3.org/1999/xhtml"
    xmlns:f="http://xmlns.jcp.org/jsf/core"
    xmlns:h="http://xmlns.jcp.org/jsf/html">
    <h:head>
        <title>Hello, World!</title>
    </h:head>
    <h:body>
        <h:form>
            <h:outputLabel for="name" value="Enter your name" required="true" />
            <h:inputText id="name" value="#{hello.name}" />
            <h:message for="name" />
            <br />
            <h:commandButton value="Say hello" action="#{hello.createMessage}">
                <f:ajax execute="@form" render="@form" />
            </h:commandButton>
            <br />
            #{hello.message}
        </h:form>
    </h:body>
</html>
```

Start the server and open it by `http://localhost:8080/contextname/hello.xhtml`.


## Resources

- [JSF 2.3 Specification (JSR 372)][20]
- [JSF 2.3 API documentation][21]
- [JSF 2.3 VDL documentation][22]
- [JSF 2.3 JS documentation][23]
- [Oracle Java EE 7 tutorial - JavaServer Faces Technology][24] (currently still JSF 2.2)
- [What's new in JSF 2.3?][25]
- [Java EE Kickoff Application][26]


  [1]: http://wildfly.org/
  [2]: https://developers.redhat.com/products/eap/overview/
  [3]: http://tomee.apache.org
  [4]: http://www.payara.fish
  [5]: https://javaee.github.io/glassfish/
  [6]: https://developer.ibm.com/wasdev/websphere-liberty/
  [7]: http://tomcat.apache.org
  [8]: http://www.eclipse.org/jetty/
  [9]: http://central.maven.org/maven2/org/glassfish/javax.faces/
  [10]: http://central.maven.org/maven2/org/jboss/weld/servlet/weld-servlet-shaded/3.0.0.Final/weld-servlet-shaded-3.0.0.Final.jar
  [11]: http://central.maven.org/maven2/javax/servlet/jstl/1.2/jstl-1.2.jar
  [12]: http://central.maven.org/maven2/javax/json/javax.json-api/1.1/javax.json-api-1.1.jar
  [13]: http://central.maven.org/maven2/javax/validation/validation-api/2.0.0.Final/validation-api-2.0.0.Final.jar
  [14]: http://central.maven.org/maven2/org/hibernate/validator/hibernate-validator/6.0.1.Final/hibernate-validator-6.0.1.Final.jar
  [15]: http://mvnrepository.com/artifact/org.glassfish/javax.faces
  [16]: https://stackoverflow.com/q/3008395/157882
  [17]: https://stackoverflow.com/q/5104094/157882
  [18]: https://stackoverflow.com/q/30639785/157882
  [19]: http://docs.oracle.com/javaee/7/tutorial/jsf-facelets.htm
  [20]: http://download.oracle.com/otn-pub/jcp/jsf-2_3-final-eval-spec/JSF_2.3.pdf
  [21]: https://javaserverfaces.github.io/docs/2.3/javadocs/index.html
  [22]: https://javaserverfaces.github.io/docs/2.3/vdldoc/index.html
  [23]: https://javaserverfaces.github.io/docs/2.3/jsdocs/index.html
  [24]: http://docs.oracle.com/javaee/7/tutorial/jsf-intro.htm
  [25]: http://arjan-tijms.omnifaces.org/p/jsf-23.html
  [26]: https://github.com/javaeekickoff/java-ee-kickoff-app

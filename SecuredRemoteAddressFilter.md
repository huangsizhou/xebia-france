# Description #

Some http requests are secured even if they don't use SSL. This is usually the case for http request emitted by applications that are located in the same data center / VLAN as the requested server. A typical scenario would be a web service consumed both by consumers located on the Internet and others located in the same data center. The first ones will use SSL when the second ones will not.

SecuredRemoteAddressFilter is a Java Servlet API filter to set `ServletRequest.isSecure() == true` for predefined remote addresses even if `ServletRequest.getScheme() == "http"`.

Thanks to this common java web security frameworks like Spring Security can still be used to enforce SSL for clients coming from non secured / non trusted networks like the Internet.

This filter is often preceded by the [XForwardedFilter](XForwardedFilter.md) to get the remote address of the client even if the request goes through load balancers (e.g. F5 Big IP, Nortel Alteon) or proxies (e.g. Apache mod\_proxy\_http).


# Filter Configuration #

| **XForwardedFilter property** | **Description** | **Format** | **Default value** |
|:------------------------------|:----------------|:-----------|:------------------|
| securedRemoteAddresses        | IP addresses for which `ServletRequest.isSecure()` must return `true` | Comma delimited list of regular expressions (in the syntax supported by the [java.util.regex.Pattern](http://java.sun.com/javase/6/docs/api/java/util/regex/Pattern.html) library) | Class A, B and C [private network IP address blocks](http://en.wikipedia.org/wiki/Private_network) : 10\.\d{1,3}\.\d{1,3}\.\d{1,3}, 192\.168\.\d{1,3}\.\d{1,3}, 172\\.(?:1[6-9]|2\\d|3[0-1]).\\d{1,3}.\\d{1,3}, 169\.254\.\d{1,3}\.\d{1,3}, 127\.\d{1,3}\.\d{1,3}\.\d{1,3} |

Note : the default configuration can usually be used as all internal servers are most of the time trusted.

# Sample of default configuration : trust request coming from private network address blocks #

```
<web-app ...>
   ...
   <filter>
      <filter-name>SecuredRemoteAddressFilter</filter-name>
      <filter-class>fr.xebia.servlet.filter.SecuredRemoteAddressFilter</filter-class>
   </filter>
 
   <filter-mapping>
      <filter-name>SecuredRemoteAddressFilter</filter-name>
      <url-pattern>/*</url-pattern>
      <dispatcher>REQUEST</dispatcher>
   </filter-mapping>
   ...
</web-app>
```

# Sample of configuration : only trust request coming from 192.168.0.10 and 192.168.0.11 #

```
<web-app ...>
   ...
   <filter>
      <filter-name>SecuredRemoteAddressFilter</filter-name>
      <filter-class>fr.xebia.servlet.filter.SecuredRemoteAddressFilter</filter-class>
      <init-param>
         <param-name>securedRemoteAddresses</param-name>
         <param-value>192\.168\.0\.10, 192\.168\.0\.11</param-value>
      </init-param>
   </filter>
 
   <filter-mapping>
      <filter-name>SecuredRemoteAddressFilter</filter-name>
      <url-pattern>/*</url-pattern>
      <dispatcher>REQUEST</dispatcher>
   </filter-mapping>
   ...
</web-app>
```

# Sample of default configuration associated with XForwardedFilter #

```
<web-app ...>
   ...
   <filter>
      <filter-name>XForwardedFilter</filter-name>
      <filter-class>fr.xebia.servlet.filter.XForwardedFilter</filter-class>
      <init-param>
         <param-name>protocolHeader</param-name>
         <param-value>x-forwarded-proto</param-value>
      </init-param>
   </filter>
   <filter>
      <filter-name>SecuredRemoteAddressFilter</filter-name>
      <filter-class>fr.xebia.servlet.filter.SecuredRemoteAddressFilter</filter-class>
   </filter>
 
   <filter-mapping>
      <filter-name>XForwardedFilter</filter-name>
      <url-pattern>/*</url-pattern>
      <dispatcher>REQUEST</dispatcher>
   </filter-mapping>
   <filter-mapping>
      <filter-name>SecuredRemoteAddressFilter</filter-name>
      <url-pattern>/*</url-pattern>
      <dispatcher>REQUEST</dispatcher>
   </filter-mapping>
   ...
</web-app>
```

# Install / Download #
  * Maven Project, add the following to `pom.xml`
```
<project ...>
   ...
   <repositories>
      <repository>
      	<id>xebia-france-googlecode-repository</id>
      	<url>http://xebia-france.googlecode.com/svn/repository/maven2/</url>
      </repository>
   </repositories>
   ...
   <dependencies>
      ...
      <dependency>
         <groupId>fr.xebia.web.extras</groupId>
         <artifactId>xebia-servlet-extras</artifactId>
         <version>1.0.1</version>
      </dependency>
      ...
   </dependencies>
   ...
</project>
```
  * Jar : Drop the jar [xebia-servlet-extras-1.0.1.jar](http://xebia-france.googlecode.com/files/xebia-servlet-extras-1.0.1.jar) ([sources](http://xebia-france.googlecode.com/files/xebia-servlet-extras-1.0.1-sources.jar)) in your web application classpath.
  * Java class : [SecuredRemoteAddressFilter.java](http://xebia-france.googlecode.com/svn/web/xebia-servlet-extras/tags/xebia-servlet-extras-1.0.1/src/main/java/fr/xebia/servlet/filter/SecuredRemoteAddressFilter.java)
  * Java project : `svn checkout http://xebia-france.googlecode.com/svn/web/xebia-servlet-extras/tags/xebia-servlet-extras-1.0.1/`

# Resources #

  * XForwardedFilter : servlet filter usually used before SecuredRemoteAddressFilter to handle `X-Forwarded-For` and `X-Forwarded-Proto` header to get the actual remote address of the calling client and the protocol used (http or https).
  * SecuredRemoteAddressValve : Tomcat valve port of the SecuredRemoteAddressFilter.
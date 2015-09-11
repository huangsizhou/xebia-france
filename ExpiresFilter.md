| **[ExpiresFilter has been integrated in Tomcat 7.0.2](http://tomcat.apache.org/tomcat-7.0-doc/config/filter.html#Expires_Filter) .** |
|:-------------------------------------------------------------------------------------------------------------------------------------|

Following documentation is inspired by `mod_expires` .

# Summary #

This filter controls the setting of the `Expires`  HTTP header and the `max-age` directive of the `Cache-Control` HTTP header in server responses. The expiration date can set to be relative to either the time the source file was last modified, or to the time of the client access.

These HTTP headers are an instruction to the client about the document's validity and persistence. If cached, the document may be fetched from the cache rather than from the source until this time has passed. After that, the cache copy is considered "expired" and invalid, and a new copy must be obtained from the source.

To modify `Cache-Control` directives other than `max-age` (see [RFC 2616 section 14.9](http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.9)), you can use other servlet filters or [Apache Httpd mod\_headers](http://httpd.apache.org/docs/2.2/mod/mod_headers.html) module.

# Filter Configuration #

## Basic configuration to add '`Expires`' and '`Cache-Control: max-age=`' headers to images, css and javascript ##

The filter will process the `x-forwarded-for` http header.

```
<web-app ...>
   ...
   <filter>
      <filter-name>ExpiresFilter</filter-name>
      <filter-class>fr.xebia.servlet.filter.ExpiresFilter</filter-class>
      <init-param>
         <param-name>ExpiresByType image</param-name>
         <param-value>access plus 10 minutes</param-value>
      </init-param>
      <init-param>
         <param-name>ExpiresByType text/css</param-name>
         <param-value>access plus 10 minutes</param-value>
      </init-param>
      <init-param>
         <param-name>ExpiresByType text/javascript</param-name>
         <param-value>access plus 10 minutes</param-value>
      </init-param>
   </filter>
   ...
   <filter-mapping>
      <filter-name>ExpiresFilter</filter-name>
      <url-pattern>/*</url-pattern>
      <dispatcher>REQUEST</dispatcher>
   </filter-mapping>
   ...
</web-app>
```

## Configuration Parameters ##

### `ExpiresActive` ###

This directive enables or disables the generation of the `Expires` and `Cache-Control` headers by this `ExpiresFilter`. If set to `Off`, the headers will not be generated for any HTTP response. If set to `On` or `true`, the headers will be added to served HTTP responses according to the criteria defined by the `ExpiresByType <content-type>` and `ExpiresDefault` directives.

Note that this directive does not guarantee that an `Expires` or `Cache-Control` header will be generated. If the criteria aren't met, no header will be sent, and the effect will be as though this directive wasn't even specified.

This parameter is optional, default value is `true`.

_Enable filter_
```
<init-param>
   <!-- supports case insensitive 'On' or 'true' -->
   <param-name>ExpiresActive</param-name><param-value>On</param-value>
</init-param>
```

_Disable filter_
```
<init-param>
   <!-- supports anything different from case insensitive 'On' and 'true' -->
   <param-name>ExpiresActive</param-name><param-value>Off</param-value>
</init-param>
```

### `ExpiresByType <content-type>` ###

This directive defines the value of the `Expires`  header and the `max-age` directive of the `Cache-Control` header generated for documents of the specified type (_e.g._, `text/html`). The second argument sets the number of seconds that will be added to a base time to construct the expiration date.  The `Cache-Control: max-age` is calculated by subtracting the request time from the expiration date and expressing the result in seconds.

The base time is either the last modification time of the file, or the time of the client's access to the document. Which should be used is specified by the `<code>` field; `M`  means that the file's last modification time should be used as the base time, and `A` means the client's access time should be used. The duration is expressed in seconds. `A2592000` stands for `access plus 30 days` in alternate syntax.

The difference in effect is subtle. If `M` (`modification` in alternate syntax) is used, all current copies of the document in all caches will expire at the same time, which can be good for something like a weekly notice that's always found at the same URL. If `A` (`access` or `now` in alternate syntax) is used, the date of expiration is different for each client; this can be good for image files that don't change very often, particularly for a set of related documents that all refer to the same images (_i.e._, the images will be accessed repeatedly within a relatively short timespan).

**Example:**
```
<init-param>
   <param-name>ExpiresByType text/html</param-name><param-value>access plus 1 month 15   days 2 hours</param-value>
</init-param>

<init-param>
   <!-- 2592000 seconds = 30 days -->
   <param-name>ExpiresByType image/gif</param-name><param-value>A2592000</param-value>
</init-param>
```

Note that this directive only has effect if `ExpiresActive On` has been specified. It overrides, for the specified MIME type _only_, any expiration date set by the `ExpiresDefault`  directive.

You can also specify the expiration time calculation using an alternate syntax, described earlier in this document.

### `ExpiresExcludedResponseStatusCodes` ###

This directive defines the http response status codes for which the `ExpiresFilter` will not generate expiration headers. By default, the `304` status code ("`Not modified`") is skipped. The value is a comma separated list of http status codes.

This directive is useful to ease usage of `ExpiresDefault` directive. Indeed, the behavior of `304 Not modified` (which does specify a `Content-Type` header) combined with `Expires` and `Cache-Control:max-age=` headers can be unnecessarily tricky to understand.

Configuration sample :
```
<init-param>
   <param-name>ExpiresExcludedResponseStatusCodes</param-name><param-value>302, 500, 503</param-value>
</init-param>
```

# Alternate Syntax #

The `ExpiresDefault` and `ExpiresByType` directives can also be defined in a more readable syntax of the form:
```
<init-param>
   <param-name>ExpiresDefault</param-name><param-value><base> [plus] {<num>   <type>}*</param-value>
</init-param>

<init-param>
   <param-name>ExpiresByType type/encoding</param-name><param-value><base> [plus]   {<num> <type>}*</param-value>
</init-param>
```

where `<base>` is one of:
  * `access`
  * `now` (equivalent to '`access`')
  * `modification`

The `plus` keyword is optional. `<num>` should be an integer value (acceptable to `Integer.parseInt()`), and `<type>` is one of:
  * `years`, `year`
  * `months`, `month`
  * `weeks`, `week`
  * `days`, `day`
  * `hours`, `hour`
  * `minutes`, {{minute}}}
  * `seconds`, `second`

For example, any of the following directives can be used to make documents expire 1 month after being accessed, by default:
```
<init-param>
   <param-name>ExpiresDefault</param-name><param-value>access plus 1 month</param-value>
</init-param>

<init-param>
   <param-name>ExpiresDefault</param-name><param-value>access plus 4 weeks</param-value>
</init-param>

<init-param>
   <param-name>ExpiresDefault</param-name><param-value>access plus 30 days</param-value>
</init-param>
```

The expiry time can be fine-tuned by adding several '`<num> <type>`' clauses:
```
<init-param>
   <param-name>ExpiresByType text/html</param-name><param-value>access plus 1 month 15   days 2 hours</param-value>
</init-param>

<init-param>
   <param-name>ExpiresByType image/gif</param-name><param-value>modification plus 5 hours 3   minutes</param-value>
</init-param>
```

Note that if you use a modification date based setting, the `Expires` header will **not** be added to content that does not come from a file on disk. This is due to the fact that there is no modification time for such content.

# Expiration headers generation eligibility #

A response is eligible to be enriched by `ExpiresFilter` if :
  1. no expiration header is defined (`Expires` header or the `max-age` directive of the `Cache-Control` header),
  1. the response status code is not excluded by the directive `ExpiresExcludedResponseStatusCodes`,
  1. The `Content-Type` of the response matches one of the types defined the in `ExpiresByType` directives or the `ExpiresDefault` directive is defined.

Note :
  * If `Cache-Control` header contains other directives than `max-age`, they are concatenated with the `max-age` directive that is added by the `ExpiresFilter`.


# Expiration configuration selection #

The expiration configuration if elected according to the following algorithm:
  1. `ExpiresByType` matching the exact content-type returned by `HttpServletResponse.getContentType()` possibly including the charset (e.g. '`text/xml;charset=UTF-8`'),
  1. `ExpiresByType` matching the content-type without the charset if `HttpServletResponse.getContentType()` contains a charset (e.g. '`text/xml;charset=UTF-8`' ->  '`text/xml`'),
  1. `ExpiresByType` matching the major type (e.g. substring before '`/`') of `HttpServletResponse.getContentType()` (e.g. '`text/xml;charset=UTF-8`' ->  '`text`'),
  1. `ExpiresDefault`


# Install / Download #

  * Maven Project, add the following to `pom.xml`
```
<project ...>
   <dependencies>
      <dependency>
         <groupId>fr.xebia.web</groupId>
         <artifactId>xebia-servlet-extras</artifactId>
         <version>1.0.7</version>
         <scope>runtime</scope>
      </dependency>
      ...
   </dependencies>
   ...
</project>
```
  * Jar : Drop the jar [xebia-servlet-extras-1.0.7.jar](http://repo1.maven.org/maven2/fr/xebia/web/xebia-servlet-extras/1.0.7/xebia-servlet-extras-1.0.7.jar) ([sources](http://repo1.maven.org/maven2/fr/xebia/web/xebia-servlet-extras/1.0.7/xebia-servlet-extras-1.0.7-sources.jar)) in your web application classpath.
  * Java class : [ExpiresFilter.java](https://github.com/xebia-france/xebia-servlet-extras/blob/xebia-servlet-extras-1.0.7/src/main/java/fr/xebia/servlet/filter/ExpiresFilter.java)
  * Java project : https://github.com/xebia-france/xebia-servlet-extras/


# Implementation Details #

## When to write the expiration headers ? ##

The `ExpiresFilter` traps the 'on before write response body' event to decide whether it should generate expiration headers or not.

To trap the 'before write response body' event, the `ExpiresFilter` wraps the http servlet response's writer and outputStream to intercept calls to the methods `write()`, `print()`, `close()` and `flush()`.
For empty response body (e.g. empty files), the `write()`, `print()`, `close()` and `flush()` methods are not called; to handle this case, the `ExpiresFilter`, at the end of its `doFilter()` method, manually triggers the `onBeforeWriteResponseBody()` method.

## Configuration syntax ##

The `ExpiresFilter` supports the same configuration syntax as Apache Httpd mod\_expires.

A challenge has been to choose the name of the `<param-name>` associated with `ExpiresByType` in the `<filter>` declaration. Indeed, Several `ExpiresByType` directives can be declared when `web.xml` syntax does not allow to declare several `<init-param>` with the same name.

The workaround has been to declare the content type in the `<param-name>` rather than in the `<param-value>`.

## Designed for extension : the open/close principle ##

The `ExpiresFilter` has been designed for extension following the open/close principle.

Key methods to override for extension are :
  * `protected boolean isEligibleToExpirationHeaderGeneration(HttpServletRequest request, XHttpServletResponse response)`
  * `protected Date getExpirationDate(HttpServletRequest request, XHttpServletResponse response)`

## Performances ##

The `ExpiresFilter` has been extensively profiled, the largest cost of the filter is now the invocation of `Response.setHeader(name, value)` to set the `Expires` and `Cache-Control:max-age=` headers. The core business logic of `ExpiresFilter`is largely cheaper than these `setHeader(...)` calls that are delegated to the underlying servlet engines.

## Tests ##

The `ExpiresFilter` has been unit tested with Jetty, profiled with Tomcat 6.0.24 and Tomcat 7.0 trunk and used with Tomcat 6.0.20.

# Troubleshooting #

To troubleshoot, enable logging on the `fr.xebia.servlet.filter.ExpiresFilter`. Logging relies on SLF4J.

Extract of log4j.properties
```
log4j.logger.fr.xebia.servlet.filter.ExpiresFilter=DEBUG
```

Sample of initialization log message :
```
2010/03/24 18:18:12,637  INFO [main] fr.xebia.servlet.filter.ExpiresFilter - Filter initialized with configuration ExpiresFilter[
   active=true, 
   excludedResponseStatusCode=[304], 
   default=null, 
   byType={
      image=ExpiresConfiguration[startingPoint=ACCESS_TIME, duration=[10 MINUTE]], 
      text/css=ExpiresConfiguration[startingPoint=ACCESS_TIME, duration=[10 MINUTE]], 
      text/javascript=ExpiresConfiguration[startingPoint=ACCESS_TIME, duration=[10 MINUTE]], 
      text/html=ExpiresConfiguration[startingPoint=ACCESS_TIME, duration=[5 MINUTE]]}]  
```

Sample of per-request log message where `ExpiresFilter` adds an expiration date
```
2010/03/24 18:43:34,586 DEBUG [http-8080-1] fr.xebia.servlet.filter.ExpiresFilter - 
   Request '/' with response status '200' content-type 'text/html‘, set expiration date Wed Mar 24 18:48:34 CET 2010  
```

Sample of per-request log message where `ExpiresFilter` does not add an expiration date
```
2010/03/24 18:43:34,564 DEBUG [http-8080-2] fr.xebia.servlet.filter.ExpiresFilter - 
   Request '/services/helloWorldService' with response status '200' content-type 'text/xml;charset=UTF-8‘ status , no expiration configured  
```

# Change log #

**1.0.7 : 2012/05/29**

**Migrate to github https://github.com/xebia-france/xebia-servlet-extras/** support `year` keyword
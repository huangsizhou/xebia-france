| XForwardedFilter is now [integrated in the forthcoming Tomcat 7 as RemoteIpFilter](http://svn.apache.org/viewvc/tomcat/trunk/java/org/apache/catalina/filters/RemoteIpFilter.java?annotate=833155), the XForwardedFilter provided on this project can be bundled in webapps deployed on any  Java application server (IBM Websphere, JBoss, Jetty, Oracle Weblogic, Tomcat, etc) |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|

# Description #
The XForwardedFilter is a Java Servlet API port of [mod\_remoteip](http://httpd.apache.org/docs/trunk/mod/mod_remoteip.html), this filter replaces the apparent client remote IP address and hostname for the request with the IP address list presented by a proxy or a load balancer via a request headers (e.g. "`X-Forwarded-For`").

Another feature of this filter is to replace the apparent scheme (`http`/`https`) and server port with the scheme presented by a proxy or a load balancer via a request header (e.g. "`X-Forwarded-Proto`").

# Filter Configuration #

**Note:** In most cases, the XForwardedFilter should precede all the other filter and in particular precede logging, authentication, authorization or remote IP related filters.

| **XForwardedFilter property** | **Description** | **Equivalent mod\_remoteip directive** | **Format** | **Default Value** |
|:------------------------------|:----------------|:---------------------------------------|:-----------|:------------------|
| remoteIPHeader                | Name of the Http Header read by this servlet filter that holds the list of traversed IP addresses starting from the requesting client | RemoteIPHeader                         | Compliant http header name like `x-forwarded-for` or `X-Client-IP` | x-forwarded-for   |
| allowedInternalProxies        | List of internal proxies ip adress. If they appear in the `remoteIpHeader` value, they will be trusted and will not appear in the `proxiesHeader` value | RemoteIPInternalProxy                  | Comma delimited list of regular expressions (in the syntax supported by the [java.util.regex.Pattern](http://java.sun.com/javase/6/docs/api/index.html?java/util/regex/Pattern.html) library) | 10\.\d{1,3}\.\d{1,3}\.\d{1,3}, 192\.168\.\d{1,3}\.\d{1,3}, 172\\.(?:1[6-9]|2\\d|3[0-1]).\\d{1,3}.\\d{1,3}, 169\.254\.\d{1,3}\.\d{1,3}, 127\.\d{1,3}\.\d{1,3}\.\d{1,3}. By default, 10/8, 192.168/16, 172.16/12, 169.254/16 and 127/8 are allowed |
| proxiesHeader                 | Name of the http header created by this servlet filter to hold the list of proxies that have been processed in the incoming `remoteIPHeader` | RemoteIPProxiesHeader                  | Compliant http header name | x-forwarded-by    |
| trustedProxies                | List of trusted proxies ip adress. If they appear in the `remoteIpHeader` value, they will be trusted and will appear in the `proxiesHeader` value | RemoteIPTrustedProxy                   | Comma delimited list of regular expressions (in the syntax supported by the [java.util.regex.Pattern](http://java.sun.com/javase/6/docs/api/index.html?java/util/regex/Pattern.html) library) |                   |
| protocolHeader                | Name of the http header read by this servlet filter that holds the flag that this request  | N/A                                    | Compliant http header name like `X-Forwarded-Proto`, `X-Forwarded-Ssl` or `Front-End-Https` | `null`            |
| protocolHeaderHttpsValue      | Value of the `protocolHeader` to indicate that it is an Https request | N/A                                    | String like `https` or `ON` | `https`           |
| httpServerPort                | Value returned by `ServletRequest.getServerPort()` when the `protocolHeader` indicates `http` protocol | N/A                                    | integer    | 80                |
| httpsServerPort               | Value returned by `ServletRequest.getServerPort()` when the `protocolHeader` indicates `https` protocol | N/A                                    | integer    | 443               |



## Basic configuration to handle `x-forwarded-for` ##

The filter will process the `x-forwarded-for` http header.

```
<web-app ...>
   ...
   <filter>
      <filter-name>XForwardedFilter</filter-name>
      <filter-class>fr.xebia.servlet.filter.XForwardedFilter</filter-class>
   </filter>
   
   <filter-mapping>
      <filter-name>XForwardedFilter</filter-name>
      <url-pattern>/*</url-pattern>
      <dispatcher>REQUEST</dispatcher>
   </filter-mapping>
   ...
</web-app>

```

## Basic configuration to handle `x-forwarded-for` and `x-forwarded-proto` ##

The filter will process `x-forwarded-for` and `x-forwarded-proto` http headers. Expected value for `x-forwarded-proto` https connections is `https` (case insensitive).

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
   
   <filter-mapping>
      <filter-name>XForwardedFilter</filter-name>
      <url-pattern>/*</url-pattern>
      <dispatcher>REQUEST</dispatcher>
   </filter-mapping>
   ...
</web-app>
```

# Advanced samples #

### Sample with internal proxies ###


XForwardedFilter configuration:

```
 <filter>
    <filter-name>XForwardedFilter</filter-name>
    <filter-class>fr.xebia.servlet.filter.XForwardedFilter</filter-class>
    <init-param>
       <param-name>allowedInternalProxies</param-name><param-value>192\.168\.0\.10, 192\.168\.0\.11</param-value>
    </init-param>
    <init-param>
       <param-name>remoteIPHeader</param-name><param-value>x-forwarded-for</param-value>
    </init-param>
    <init-param>
       <param-name>remoteIPProxiesHeader</param-name><param-value>x-forwarded-by</param-value>
    </init-param>
    <init-param>
       <param-name>protocolHeader</param-name><param-value>x-forwarded-proto</param-value>
    </init-param>
 </filter>
 
 <filter-mapping>
    <filter-name>XForwardedFilter</filter-name>
    <url-pattern>/*</url-pattern>
    <dispatcher>REQUEST</dispatcher>
 </filter-mapping>
```

Request values:
| **property** | **Value Before XForwardedFilter** | **Value After XForwardedFilter** |
|:-------------|:----------------------------------|:---------------------------------|
| request.remoteAddr | 192.168.0.10                      | 140.211.11.130                   |
| request.header`[`'x-forwarded-for'`]` | 140.211.11.130, 192.168.0.10      | null                             |
| request.header`[`'x-forwarded-by'`]` | null                              | null                             |
| request.header`[`'x-forwarded-proto'`]` | https                             | https                            |
| request.scheme | http                              | https                            |
| request.secure | false                             | true                             |
| request.serverPort | 80                                | 443                              |

Note : `x-forwarded-by` header is null because only internal proxies as been traversed by the request.
`x-forwarded-by` is null because all the proxies are trusted or internal.

### Sample with trusted proxies ###


XForwardedFilter configuration:

```
 <filter>
    <filter-name>XForwardedFilter</filter-name>
    <filter-class>fr.xebia.servlet.filter.XForwardedFilter</filter-class>
    <init-param>
       <param-name>allowedInternalProxies</param-name><param-value>192\.168\.0\.10, 192\.168\.0\.11</param-value>
    </init-param>
    <init-param>
       <param-name>remoteIPHeader</param-name><param-value>x-forwarded-for</param-value>
    </init-param>
    <init-param>
       <param-name>remoteIPProxiesHeader</param-name><param-value>x-forwarded-by</param-value>
    </init-param>
    <init-param>
       <param-name>trustedProxies</param-name><param-value>proxy1, proxy2</param-value>
    </init-param>
 </filter>
 
 <filter-mapping>
    <filter-name>XForwardedFilter</filter-name>
    <url-pattern>/*</url-pattern>
    <dispatcher>REQUEST</dispatcher>
 </filter-mapping>
```

Request values:
| **property** | **Value Before XForwardedFilter** | **Value After XForwardedFilter** |
|:-------------|:----------------------------------|:---------------------------------|
| request.remoteAddr | 192.168.0.10                      | 140.211.11.130                   |
| request.header`[`'x-forwarded-for'`]` | 140.211.11.130, proxy1, proxy2    | null                             |
| request.header`[`'x-forwarded-by'`]` | null                              | proxy1, proxy2                   |

Note : `proxy1` and `proxy2` are both trusted proxies that come in `x-forwarded-for` header, they both
are migrated in `x-forwarded-by` header. `x-forwarded-by` is null because all the proxies are trusted or internal.

### Sample with internal and trusted proxies ###


XForwardedFilter configuration:

```
 <filter>
    <filter-name>XForwardedFilter</filter-name>
    <filter-class>fr.xebia.servlet.filter.XForwardedFilter</filter-class>
    <init-param>
       <param-name>allowedInternalProxies</param-name><param-value>192\.168\.0\.10, 192\.168\.0\.11</param-value>
    </init-param>
    <init-param>
       <param-name>remoteIPHeader</param-name><param-value>x-forwarded-for</param-value>
    </init-param>
    <init-param>
       <param-name>remoteIPProxiesHeader</param-name><param-value>x-forwarded-by</param-value>
    </init-param>
    <init-param>
       <param-name>trustedProxies</param-name><param-value>proxy1, proxy2</param-value>
    </init-param>
 </filter>
 
 <filter-mapping>
    <filter-name>XForwardedFilter</filter-name>
    <url-pattern>/*</url-pattern>
    <dispatcher>REQUEST</dispatcher>
 </filter-mapping>
```

Request values:
| **property** | **Value Before XForwardedFilter** | **Value After XForwardedFilter** |
|:-------------|:----------------------------------|:---------------------------------|
| request.remoteAddr | 192.168.0.10                      | 140.211.11.130                   |
| request.header`[`'x-forwarded-for'`]` | 140.211.11.130, proxy1, proxy2, 192.168.0.10 | null                             |
| request.header`[`'x-forwarded-by'`]` | null                              | proxy1, proxy2                   |

Note : `proxy1` and `proxy2` are both trusted proxies that come in `x-forwarded-for` header, they both
are migrated in `x-forwarded-by` header. As `192.168.0.10` is an internal proxy, it does not appear in
`x-forwarded-by`. `x-forwarded-by` is null because all the proxies are trusted or internal.

### Sample with an untrusted proxy ###


XForwardedFilter configuration:

```
 <filter>
    <filter-name>XForwardedFilter</filter-name>
    <filter-class>fr.xebia.servlet.filter.XForwardedFilter</filter-class>
    <init-param>
       <param-name>allowedInternalProxies</param-name><param-value>192\.168\.0\.10, 192\.168\.0\.11</param-value>
    </init-param>
    <init-param>
       <param-name>remoteIPHeader</param-name><param-value>x-forwarded-for</param-value>
    </init-param>
    <init-param>
       <param-name>remoteIPProxiesHeader</param-name><param-value>x-forwarded-by</param-value>
    </init-param>
    <init-param>
       <param-name>trustedProxies</param-name><param-value>proxy1, proxy2</param-value>
    </init-param>
 </filter>
 
 <filter-mapping>
    <filter-name>XForwardedFilter</filter-name>
    <url-pattern>/*</url-pattern>
    <dispatcher>REQUEST</dispatcher>
 </filter-mapping>
```

Request values:
| **property** | **Value Before XForwardedFilter** | **Value After XForwardedFilter** |
|:-------------|:----------------------------------|:---------------------------------|
| request.remoteAddr | 192.168.0.10                      | untrusted-proxy                  |
| request.header`[`'x-forwarded-for'`]` | 140.211.11.130, untrusted-proxy, proxy1 | 140.211.11.130                   |
| request.header`[`'x-forwarded-by'`]` | null                              | proxy1                           |

Note : `x-forwarded-by` holds the trusted proxy `proxy1`. `x-forwarded-by` holds
`140.211.11.130` because `untrusted-proxy` is not trusted and thus, we can not trust that
`untrusted-proxy` is the actual remote ip. `request.remoteAddr` is `untrusted-proxy` that is an IP
verified by `proxy1`.

# Install / Download #
  * Maven Project, add the following to `pom.xml`
```
<project ...>
   ...
   <dependencies>
      ...
      <dependency>
         <groupId>fr.xebia.web</groupId>
         <artifactId>xebia-servlet-extras</artifactId>
         <version>1.0.8</version>
      </dependency>
      ...
   </dependencies>
   ...
</project>
```
  * Jar : Drop the jar [xebia-servlet-extras-1.0.8.jar](http://repo1.maven.org/maven2/fr/xebia/web/xebia-servlet-extras/1.0.8/xebia-servlet-extras-1.0.8.jar) ([sources](http://repo1.maven.org/maven2/fr/xebia/web/xebia-servlet-extras/1.0.8/xebia-servlet-extras-1.0.8-sources.jar)) in your web application classpath.
  * Java class : [XForwardedFilter.java](https://github.com/xebia-france/xebia-servlet-extras/blob/xebia-servlet-extras-1.0.8/src/main/java/fr/xebia/servlet/filter/XForwardedFilter.java)
  * Java project on GitHub: https://github.com/xebia-france/xebia-servlet-extras


# Implementation Details #

This filter proceeds as follows:

If the incoming `request.getRemoteAddr()` matches the filter's list of internal proxies :

  * Loop on the comma delimited list of IPs and hostnames passed by the preceding load balancer or proxy in the given request's Http header named `$remoteIPHeader` (default value `x-forwarded-for`). Values are processed in right-to-left order.
  * For each ip/host of the list:
    * if it matches the internal proxies list, the ip/host is swallowed
    * if it matches the trusted proxies list, the ip/host is added to the created proxies header
    * otherwise, the ip/host is declared to be the remote ip and looping is stopped.
  * If the request http header named `$protocolHeader` (e.g. `x-forwarded-for`) equals to the value of `protocolHeaderHttpsValue` configuration parameter (default `https`) then `request.isSecure = true`, `request.scheme = https` and `request.serverPort = 443`. Note that 443 can be overwritten with the `$httpsServerPort` configuration parameter.

**Regular expression vs. IP address blocks:** `mod_remoteip` allows to use address blocks (e.g. `192.168/16`) to configure `RemoteIPInternalProxy` and `RemoteIPTrustedProxy` ; as Tomcat doesn't have a library similar to [apr\_ipsubnet\_test](http://apr.apache.org/docs/apr/1.3/group__apr__network__io.html#gb74d21b8898b7c40bf7fd07ad3eb993d), `XForwardedFilter` uses regular expression to configure `allowedInternalProxies` and `trustedProxies` in the same fashion as [RequestFilterFilter](http://tomcat.apache.org/tomcat-6.0-doc/api/index.html?org/apache/catalina/filters/RequestFilterFilter.html) does.

# Change Log #

**1.0.6 - 2012/03/30:** response.sendRedirect(location) ignores the overwritten scheme. Fix provided by Marc van Andel.

**1.0.8 - 2012/08/19:** [(issue 17)](http://code.google.com/p/xebia-france/issues/detail?id=17) XForwardedFilter does not support '`protocolHeaderHttpsValue`' as stated in the doc but '`protocolHeaderSslValue`'.

# Resources #
  * [RemoteIpValve](RemoteIpValve.md) : the equivalent of XForwardedFilterintegrated in Tomcat. It has been proposed to the Tomcat project as [Bug 47330 - proposal : port of mod\_remoteip in Tomcat as RemoteIpValve](https://issues.apache.org/bugzilla/show_bug.cgi?id=47330).
  * Wikipedia [X-Forwarded-For](http://en.wikipedia.org/wiki/X-Forwarded-For) page.
  * How to enable the X-Forwarded-For http header on F5 BIGIP/Viprion : [Dev Central - Using "X-Forwarded-For" in Apache or PHP](http://devcentral.f5.com/weblogs/macvittie/archive/2008/06/02/3323.aspx)].
  * Apache mod\_proxy X-Forwarded http headers (X-Forwarded-For, X-Forwarded-Host and X-Forwarded-Server) : [Reverse Proxy Request Headers](http://httpd.apache.org/docs/2.2/mod/mod_proxy.html#x-headers).
  * Apache [mod\_remoteip](http://httpd.apache.org/docs/trunk/mod/mod_remoteip.html) module to handle X-Forwarded-For header in Apache.
  * Squid Proxy [X-Forwarded-For configuration](http://www.squid-cache.org/Doc/config/forwarded_for/).
  * Firefox [Modify Headers](https://addons.mozilla.org/en-US/firefox/addon/967) [X-Forwarded-For Spoofer](https://addons.mozilla.org/en-US/firefox/addon/5948) add-ons. Modify Headers allows you to test both X-Forwarded-For and X-Forwarded-Proto.
  * Jetty [connector configuration](http://docs.codehaus.org/display/JETTY/Configuring+Connectors) to handle X-Forwarded-For.
  * [Tomcat : Adresse IP de l’internaute, load balancer, reverse proxy et header Http X-Forwarded-For](http://blog.xebia.fr/2009/05/05/tomcat-adresse-ip-de-linternaute-load-balancer-reverse-proxy-et-header-http-x-forwarded-for/) : a french tutorial on X-Forwarded-For handling in Java.
  * [Tomcat, SSL, communications sécurisées et X-Forwarded-Proto](http://blog.xebia.fr/2009/11/13/tomcat-ssl-communications-securisees-et-x-forwarded-proto/) : a french tutorial on Tomcat, SSL, X-Forwarded-Proto and secured communications.
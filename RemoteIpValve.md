| **[RemoteIpValve has been integrated in Tomcat 6.0.24](http://svn.apache.org/viewvc/tomcat/tc6.0.x/trunk/java/org/apache/catalina/valves/RemoteIpValve.java?annotate=833535&pathrev=833536) changing its package to be named `org.apache.catalina.valves.RemoteIpValve`.** |
|:---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|


# Description #

The RemoteIpValve is a Tomcat port of [mod\_remoteip](http://httpd.apache.org/docs/trunk/mod/mod_remoteip.html), this valve replaces the apparent client remote IP address and hostname for the request with the IP address list presented by a proxy or a load balancer via a request headers (e.g. "`X-Forwarded-For`"). Moreover, RemoteIpValve replaces the apparent scheme (`http`/`https`) and server port with the scheme presented by a proxy or a load balancer via a request header (e.g. "`X-Forwarded-Proto`").

The RemoteIpValve is intented to be used when there are proxies or load balancers (F5 Big IP, Nortel Alteon, Apache mod\_proxy, etc) in front of a Tomcat server.

Presence of the `x-fowarded-for` http header will modify the behavior of `request.getRemoteAddr()` and `request.getRemoteHost()`.
Presence of `x-forwarded-proto` with value `https` will modify the behavior of `request.isSecure()`, `request.getScheme()` and `request.getServerPort()`.

# Valve Configuration #

**Note:** In most cases, the RemoteIpValve should precede all the other valves and in particular precede logging, authentication, authorization or remote IP related valves.

## Basic configuration to handle `x-forwarded-for` in Tomcat access logs and in `HttpServletRequest` ##

The valve will process the `x-forwarded-for` http header.

```
<Host ... >
   ...
   <Valve className="org.apache.catalina.connector.RemoteIpValve" />
   ...
   <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs" prefix="localhost_access_log."
      suffix=".txt" pattern="common" resolveHosts="false" />
   ...
</Host>
```

Note that the value of the `x-forwarded-for` header will be used in Tomcat's access log because the `AccessLogValve` is declared **after** the `RemoteIpValve`.

## Basic configuration to handle `x-forwarded-for` and `x-forwarded-proto` in Tomcat access logs and in `HttpServletRequest` ##

The valve will process `x-forwarded-for` and `x-forwarded-proto` http headers. Expected value for `x-forwarded-proto` https connections is `https` (case insensitive).

Note that the value of the `x-forwarded-for` header will be used in Tomcat's access log because the `AccessLogValve` is declared **after** the `RemoteIpValve`.

```
<Host ... >
   ...
   <Valve className="org.apache.catalina.connector.RemoteIpValve" protocolHeader="x-forwarded-proto" />
   <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs" prefix="localhost_access_log."
      suffix=".txt" pattern="common" resolveHosts="false" />
   ...
</Host>
```

## Advanced Configuration ##
| **RemoteIpValve property** | **Description** | **Equivalent mod\_remoteip directive** | **Format** | **Default Value** |
|:---------------------------|:----------------|:---------------------------------------|:-----------|:------------------|
| remoteIPHeader             | Name of the Http Header read by this servlet filter that holds the list of traversed IP addresses starting from the requesting client | RemoteIPHeader                         | Compliant http header name like `x-forwarded-for` or `X-Client-IP` | x-forwarded-for   |
| internalProxies            | List of internal proxies ip adress. If they appear in the `remoteIpHeader` value, they will be trusted and will not appear in the `proxiesHeader` value | RemoteIPInternalProxy                  | Comma delimited list of regular expressions (in the syntax supported by the [java.util.regex.Pattern](http://java.sun.com/javase/6/docs/api/index.html?java/util/regex/Pattern.html) library) | 10\.\d{1,3}\.\d{1,3}\.\d{1,3}, 192\.168\.\d{1,3}\.\d{1,3}, 169\.254\.\d{1,3}\.\d{1,3}, 127\.\d{1,3}\.\d{1,3}\.\d{1,3}. By default, 10/8, 192.168/16, 169.254/16 and 127/8 are allowed ; 172.16/12 has not been enabled by default because it is complex to describe with regular expressions |
| proxiesHeader              | Name of the http header created by this servlet filter to hold the list of proxies that have been processed in the incoming `remoteIPHeader` | RemoteIPProxiesHeader                  | Compliant http header name | x-forwarded-by    |
| trustedProxies             | List of trusted proxies ip adress. If they appear in the `remoteIpHeader` value, they will be trusted and will appear in the `proxiesHeader` value | RemoteIPTrustedProxy                   | Comma delimited list of regular expressions (in the syntax supported by the [java.util.regex.Pattern](http://java.sun.com/javase/6/docs/api/index.html?java/util/regex/Pattern.html) library) |                   |
| protocolHeader             | Name of the http header read by this servlet filter that holds the flag that this request  | N/A                                    | Compliant http header name like `X-Forwarded-Proto`, `X-Forwarded-Ssl` or `Front-End-Https` | `null`            |
| protocolHeaderHttpsValue   | Value of the `protocolHeader` to indicate that it is an Https request | N/A                                    | String like `https` or `ON` | `https`           |


This Valve may be attached to any Container, depending on the granularity of the filtering you wish to perform.

# Advanced samples #

### Sample with internal proxies ###


RemoteIpValve configuration:

```
<Valve 
  className="org.apache.catalina.connector.RemoteIpValve"
  internalProxies="192\.168\.0\.10, 192\.168\.0\.11"
  remoteIPHeader="x-forwarded-for"
  remoteIPProxiesHeader="x-forwarded-by"
  protocolHeader="x-forwarded-proto"
  />
```

Request values:
| **property** | **Value Before RemoteIpValve** | **Value After RemoteIpValve** |
|:-------------|:-------------------------------|:------------------------------|
| request.remoteAddr | 192.168.0.10                   | 140.211.11.130                |
| request.header`[`'x-forwarded-for'`]` | 140.211.11.130, 192.168.0.10   | null                          |
| request.header`[`'x-forwarded-by'`]` | null                           | null                          |
| request.header`[`'x-forwarded-proto'`]` | https                          | https                         |
| request.scheme | http                           | https                         |
| request.secure | false                          | true                          |
| request.serverPort | 80                             | 443                           |

Note : `x-forwarded-by` header is null because only internal proxies as been traversed by the request.
`x-forwarded-by` is null because all the proxies are trusted or internal.

### Sample with trusted proxies ###


RemoteIpValve configuration:

```
<Valve 
  className="org.apache.catalina.connector.RemoteIpValve"
  internalProxies="192\.168\.0\.10, 192\.168\.0\.11"
  remoteIPHeader="x-forwarded-for"
  remoteIPProxiesHeader="x-forwarded-by"
  trustedProxies="proxy1, proxy2"
  />
```

Request values:
| **property** | **Value Before RemoteIpValve** | **Value After RemoteIpValve** |
|:-------------|:-------------------------------|:------------------------------|
| request.remoteAddr | 192.168.0.10                   | 140.211.11.130                |
| request.header`[`'x-forwarded-for'`]` | 140.211.11.130, proxy1, proxy2 | null                          |
| request.header`[`'x-forwarded-by'`]` | null                           | proxy1, proxy2                |

Note : `proxy1` and `proxy2` are both trusted proxies that come in `x-forwarded-for` header, they both
are migrated in `x-forwarded-by` header. `x-forwarded-by` is null because all the proxies are trusted or internal.

### Sample with internal and trusted proxies ###


RemoteIpValve configuration:

```
<Valve 
  className="org.apache.catalina.connector.RemoteIpValve"
  internalProxies="192\.168\.0\.10, 192\.168\.0\.11"
  remoteIPHeader="x-forwarded-for"
  remoteIPProxiesHeader="x-forwarded-by"
  trustedProxies="proxy1, proxy2"
  />
```

Request values:
| **property** | **Value Before RemoteIpValve** | **Value After RemoteIpValve** |
|:-------------|:-------------------------------|:------------------------------|
| request.remoteAddr | 192.168.0.10                   | 140.211.11.130                |
| request.header`[`'x-forwarded-for'`]` | 140.211.11.130, proxy1, proxy2, 192.168.0.10 | null                          |
| request.header`[`'x-forwarded-by'`]` | null                           | proxy1, proxy2                |

Note : `proxy1` and `proxy2` are both trusted proxies that come in `x-forwarded-for` header, they both
are migrated in `x-forwarded-by` header. As `192.168.0.10` is an internal proxy, it does not appear in
`x-forwarded-by`. `x-forwarded-by` is null because all the proxies are trusted or internal.

### Sample with an untrusted proxy ###


RemoteIpValve configuration:

```
<Valve 
  className="org.apache.catalina.connector.RemoteIpValve"
  internalProxies="192\.168\.0\.10, 192\.168\.0\.11"
  remoteIPHeader="x-forwarded-for"
  remoteIPProxiesHeader="x-forwarded-by"
  trustedProxies="proxy1, proxy2"
  />
```

Request values:
| **property** | **Value Before RemoteIpValve** | **Value After RemoteIpValve** |
|:-------------|:-------------------------------|:------------------------------|
| request.remoteAddr | 192.168.0.10                   | untrusted-proxy               |
| request.header`[`'x-forwarded-for'`]` | 140.211.11.130, untrusted-proxy, proxy1 | 140.211.11.130                |
| request.header`[`'x-forwarded-by'`]` | null                           | proxy1                        |

Note : `x-forwarded-by` holds the trusted proxy `proxy1`. `x-forwarded-by` holds
`140.211.11.130` because `untrusted-proxy` is not trusted and thus, we can not trust that
`untrusted-proxy` is the actual remote ip. `request.remoteAddr` is `untrusted-proxy` that is an IP
verified by `proxy1`.

# Install / Download #
  * Jar : Copy the jar [xebia-tomcat-extras-1.0.0.jar](http://xebia-france.googlecode.com/files/xebia-tomcat-extras-1.0.0.jar) ([sources](http://xebia-france.googlecode.com/files/xebia-tomcat-extras-1.0.0-sources.jar)) in Tomcat's classpath (e.g. under `$TOMCAT_HOME/lib`)
  * Java class : [RemoteIpValve.java](http://xebia-france.googlecode.com/svn/tomcat/xebia-tomcat-extras/tags/xebia-tomcat-extras-1.0.0/src/main/java/org/apache/catalina/connector/RemoteIpValve.java)
  * Java project : `svn checkout http://xebia-france.googlecode.com/svn/tomcat/xebia-tomcat-extras/tags/xebia-tomcat-extras-1.0.0/`
  * A backport for Tomcat 5.5 is available at [xebia-tomcat-extras-tc55-1.0.0.jar](http://xebia-france.googlecode.com/files/xebia-tomcat-extras-tc55-1.0.0.jar), [xebia-tomcat-extras-tc55-1.0.0-sources.jar](http://xebia-france.googlecode.com/files/xebia-tomcat-extras-tc55-1.0.0-sources.jar), http://xebia-france.googlecode.com/svn/tomcat/xebia-tomcat-extras/tags/xebia-tomcat-extras-tc55-1.0.0/

# Implementation Details #

This valve proceeds as follows:

If the incoming `request.getRemoteAddr()` matches the valve's list of internal proxies :

  * Loop on the comma delimited list of IPs and hostnames passed by the preceding load balancer or proxy in the given request's Http header named `$remoteIPHeader` (default value `x-forwarded-for`). Values are processed in right-to-left order.
  * For each ip/host of the list:
    * if it matches the internal proxies list, the ip/host is swallowed
    * if it matches the trusted proxies list, the ip/host is added to the created proxies header
    * otherwise, the ip/host is declared to be the remote ip and looping is stopped.
  * If the request http header named `$protocolHeader` (e.g. `x-forwarded-for`) equals to the value of `protocolHeaderHttpsValue` configuration parameter (default `https`) then `request.isSecure = true`, `request.scheme = https` and `request.serverPort = 443`. Note that 443 can be overwritten with the `$httpsServerPort` configuration parameter.

**Regular expression vs. IP address blocks:** `mod_remoteip` allows to use address blocks (e.g. `192.168/16`) to configure `RemoteIPInternalProxy` and `RemoteIPTrustedProxy` ; as Tomcat doesn't have a library similar to [apr\_ipsubnet\_test](http://apr.apache.org/docs/apr/1.3/group__apr__network__io.html#gb74d21b8898b7c40bf7fd07ad3eb993d), `RemoteIpValve` uses regular expression to configure `internalProxies` and `trustedProxies` in the same fashion as [RequestFilterValve](http://tomcat.apache.org/tomcat-6.0-doc/api/index.html?org/apache/catalina/valves/RequestFilterValve.html) does.

**Package org.apache.catalina.connector vs. org.apache.catalina.valves**: This valve is temporarily located in `org.apache.catalina.connector` package instead of `org.apache.catalina.valves` because it uses `protected` visibility of [Request#remoteAddr](http://tomcat.apache.org/tomcat-6.0-doc/api/org/apache/catalina/connector/Request.html#remoteAddr) and [Request#remoteHost](http://tomcat.apache.org/tomcat-6.0-doc/api/org/apache/catalina/connector/Request.html#remoteHost). This valve could move to `org.apache.catalina.valves` if [Request#setRemoteAddr(String)](http://tomcat.apache.org/tomcat-6.0-doc/api/org/apache/catalina/connector/Request.html#setRemoteAddr(java.lang.String)) and [Request#setRemoteHost(String)](http://tomcat.apache.org/tomcat-6.0-doc/api/org/apache/catalina/connector/Request.html#setRemoteHost(java.lang.String)) were modified to no longer be no-op but actually set the underlying property.

# Resources #
  * This valve has been proposed to the Tomcat project as [Bug 47330 - proposal : port of mod\_remoteip in Tomcat as RemoteIpValve](https://issues.apache.org/bugzilla/show_bug.cgi?id=47330).
  * [XForwardedFilter](XForwardedFilter.md) : the equivalent of RemoteIpValve to handle X-Forwarded-For and X(Forwarded-Proto directly in web applications with a Servlet Filter.
  * Wikipedia [X-Forwarded-For](http://en.wikipedia.org/wiki/X-Forwarded-For) page.
  * How to enable the X-Forwarded-For http header on F5 BIGIP/Viprion : [Dev Central - Using "X-Forwarded-For" in Apache or PHP](http://devcentral.f5.com/weblogs/macvittie/archive/2008/06/02/3323.aspx)].
  * Apache mod\_proxy X-Forwarded http headers (X-Forwarded-For, X-Forwarded-Host and X-Forwarded-Server) : [Reverse Proxy Request Headers](http://httpd.apache.org/docs/2.2/mod/mod_proxy.html#x-headers).
  * Apache [mod\_remoteip](http://httpd.apache.org/docs/trunk/mod/mod_remoteip.html) module to handle X-Forwarded-For header in Apache.
  * Squid Proxy [X-Forwarded-For configuration](http://www.squid-cache.org/Doc/config/forwarded_for/).
  * Firefox [Modify Headers](https://addons.mozilla.org/en-US/firefox/addon/967) [X-Forwarded-For Spoofer](https://addons.mozilla.org/en-US/firefox/addon/5948) add-ons. Modify Headers allows you to test both X-Forwarded-For and X-Forwarded-Proto.
  * Jetty [connector configuration](http://docs.codehaus.org/display/JETTY/Configuring+Connectors) to handle X-Forwarded-For.
  * [Tomcat : Adresse IP de l’internaute, load balancer, reverse proxy et header Http X-Forwarded-For](http://blog.xebia.fr/2009/05/05/tomcat-adresse-ip-de-linternaute-load-balancer-reverse-proxy-et-header-http-x-forwarded-for/) : a french tutorial on X-Forwarded-For handling in Java.
  * [Tomcat, SSL, communications sécurisées et X-Forwarded-Proto](http://blog.xebia.fr/2009/11/13/tomcat-ssl-communications-securisees-et-x-forwarded-proto/) : a french tutorial on Tomcat, SSL, X-Forwarded-Proto and secured communications.
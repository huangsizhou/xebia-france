# Description #

Some http requests are secured even if they don't use SSL. This is usually the case for http request emitted by applications that are located in the same data center / VLAN as the requested server. A typical scenario would be a web service consumed both by consumers located on the Internet and others located in the same data center. The first ones will use SSL when the second ones will not.

SecuredRemoteAddressValve is a Tomcat valve to set `ServletRequest.isSecure() == true` for predefined remote addresses even if `ServletRequest.getScheme() == "http"`.

Thanks to this common java web security frameworks like Spring Security can still be used to enforce SSL for clients coming from non secured / non trusted networks like the Internet.

This valve is often preceded by the RemoteIpValve to get the remote address of the client even if the request goes through load balancers (e.g. F5 Big IP, Nortel Alteon) or proxies (e.g. Apache mod\_proxy\_http).


# Valve Configuration #

| **SecuredRemoteAddressValve attribute** | **Description** | **Format** | **Default value** |
|:----------------------------------------|:----------------|:-----------|:------------------|
| securedRemoteAddresses                  | IP addresses for which `ServletRequest.isSecure()` must return `true` | Comma delimited list of regular expressions (in the syntax supported by the [java.util.regex.Pattern](http://java.sun.com/javase/6/docs/api/java/util/regex/Pattern.html) library) | Class A, B and C [private network IP address blocks](http://en.wikipedia.org/wiki/Private_network) : 10\.\d{1,3}\.\d{1,3}\.\d{1,3}, 192\.168\.\d{1,3}\.\d{1,3}, 172\\.(?:1[6-9]|2\\d|3[0-1]).\\d{1,3}.\\d{1,3}, 169\.254\.\d{1,3}\.\d{1,3}, 127\.\d{1,3}\.\d{1,3}\.\d{1,3} |

Note : the default configuration can usually be used as internal servers are most of the time trusted.

# Sample of default configuration : trust request coming from private network address blocks #

SecuredRemoteAddressValve is preceded by RemoteIpValve to get the actual remote address of the calling client if a load balancer or a proxy is used between clients and the Tomcat server.

```
<Server ...>
   ...
   <Service name="Catalina">
      <Connector ... />

      <Engine ...>
         <!-- Process x-Forwarded-For to get remote address and X-Forwarded-Proto to identify SSL requests -->
         <Valve className="org.apache.catalina.connector.RemoteIpValve" protocolHeader="X-Forwarded-For" />

         <!-- Flag as secure all request coming from private network IP address blocks. Must be declared after RemoteIpValve -->
         <Valve className="org.apache.catalina.connector.SecuredRemoteAddressValve" />

         <!-- AccessLogValve must be declared after RemoteIpValve to get the remote address and the scheme https/http -->
         <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs" pattern="common" prefix="access_log."
            resolveHosts="false" suffix=".txt" />
         ...
         </Host>
      </Engine>
   </Service>
</Server>
```

# Sample with secured remote addresses limited to 192.168.0.10 and 192.168.0.11 #

```
<Server ...>
   ...
   <Service name="Catalina">
      <Connector ... />

      <Engine ...>
         <!-- Process x-Forwarded-For to get remote address and X-Forwarded-Proto to identify SSL requests -->
         <Valve className="org.apache.catalina.connector.RemoteIpValve" protocolHeader="X-Forwarded-For" />

         <!-- Flag as secure all request coming from 192.168.0.10 and 192.168.0.11. Must be declared after RemoteIpValve -->
         <Valve className="org.apache.catalina.connector.SecuredRemoteAddressValve" 
                securedRemoteAddresses="192\.168\.0\.10,192\.168\.0\.10" />

         <!-- AccessLogValve must be declared after RemoteIpValve to get the remote address and the scheme https/http -->
         <Valve className="org.apache.catalina.valves.AccessLogValve" directory="logs" pattern="common" prefix="access_log."
            resolveHosts="false" suffix=".txt" />
         ...
         </Host>
      </Engine>
   </Service>
</Server>
```

# Install / Download #
  * Jar : Copy the jar [xebia-tomcat-extras-1.0.0.jar](http://xebia-france.googlecode.com/files/xebia-tomcat-extras-1.0.0.jar) ([sources](http://xebia-france.googlecode.com/files/xebia-tomcat-extras-1.0.0-sources.jar)) in Tomcat's classpath (e.g. under `$TOMCAT_HOME/lib`)
  * Java class : [SecuredRemoteAddressValve.java](http://xebia-france.googlecode.com/svn/tomcat/xebia-tomcat-extras/tags/xebia-tomcat-extras-1.0.0/src/main/java/org/apache/catalina/connector/SecuredRemoteAddressValve.java)
  * Java project : `svn checkout http://xebia-france.googlecode.com/svn/tomcat/xebia-tomcat-extras/tags/xebia-tomcat-extras-1.0.0/`

# Resources #

  * RemoteIpValve : Tomcat valve usually used before SecuredRemoteAddressValve to handle `X-Forwarded-For` and `X-Forwarded-Proto` header to get the actual remote address of the calling client and the protocol used (http or https).
  * SecuredRemoteAddressFilter : Java Servlet Filter port of the SecuredRemoteAddressValve.
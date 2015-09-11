# Description #
The **IpBanner Filter** is a kind of java implementation of [fail2ban](http://www.fail2ban.org).
Its aim is to provide a protection over brute force authentication requests using a ban mechanism based on client IP address.

This implementation can be deployed as a Filter over any servlet container or Java EE Application Server (actually tested on Tomcat and Jetty).

# How it works #
In case of authentication failure, the IP address of the client that submit the authentication request is stored in **IpBanner**. After a number of retry, the filter will ban the IP address and reject the connection attempt with a `403/Forbidden` error.

The maximum number of retry and the ban duration can be configured at filter level.

The **IpBanner** filter detects authentication failures using a configurable list of HTTP response status (`401/Unauthorized` and `403/Forbidden` by default).

It will also consider that the authentication is a failure if the request contains a `failureRequest` attribute (defined in filter configuration using the param-name `failureRequestAttributeName`). This is mostly useful for tests.

Each time an authentication request is banned, its IP address is inserted in an **IpBanner** bucket. If this IP already exists, we increment the associated failed attempts counter. If the maximum number of retry is reached on a new failed attempt, the IP address is banned and the filter will reject with a `403/Forbidden` error this and the following request until the ban duration will be reached.
IP address are banned for a configurable amount of time. When the time is reached, the IP will be unbanned but the IP address will stay in the bucket so on the next authentication failure, the IP will be directly banned.

IP address are never removed from buckets but buckets are removed after a configurable amount of time. The life time for a bucket is defined by filter parameter `bucketTimeToLive`.

# How to use it #
## Filter installation ##
### Manual installation ###
TODO.
### Maven dependency ###
TODO
## Filter configuration ##
The filter is defined in your application `web.xml` :
```
<filter>
  <display-name>IP Banner</display-name>
  <filter-class>fr.xebia.ipbanner.IpBannerFilter</filter-class>
  <init-param>
    <param-name>failureResponseStatusCodes</param-name>
    <param-value>401,403</param-value>
  </init-param
  <init-param>
    <param-name>failureRequestAttributeName</param-name>
    <param-value>IpBannerFilter.failure</param-value>
  </init-param>
  <init-param>
    <param-name>maxRetry</param-name>
    <param-value>10</param-value>
  </init-param>
  <init-param>
    <param-name>banTimeInSecond</param-name>
    <param-value>60</param-value>
  </init-param>
  <init-param>
    <param-name>bucketTimeToLive</param-name>
    <param-value>600</param-value>
  </init-param>
</filter>
```
The following parameters are used to configure the **IpBanner** Filter :
  * **failureResponseStatusCodes** : defines the list of status code corresponding to a failed connection attempt (default value is `401`,`403`).
  * **failureRequestAttributeName** : defines a request attribute that implies a failed authentication request. This is mostly used for test purpose.
  * **maxRetry** : defines the number of retry before considering that the IP address must be banned (default to 10 times).
  * **banTimeInSecond** : defines how many time an IP address must be banned (default 60 seconds)
  * **bucketTimeToLive** : defines the life time of a bucket (default to 600 seconds)

## Configuring application authentication ##
[Spring Security](http://static.springsource.org/spring-security/site/) can be used to secure your web application. We will use it to illustrate IpBanner Filter usage.

Note that **IpBanner** is agnostic of underlying security mechanism if this
mechanism is able to send a response with an appropriate error status
(403/Forbidden for example) that can be handled by a Servlet Filter.

To demonstrate **IpBanner** Filter, we will use a simple security mechanism
definition. You can download the complete demo [here](http://..).

Here is a code snippet of `springContext-security.xml` :
```
<http auto-config='true'>
    <intercept-url pattern="/login.jsp" filters="none"/>
    <intercept-url pattern="/**" access="ROLE_USER"/>
    <form-login login-page="/login.jsp" default-target-url="/Home.jsp"
                authentication-failure-url="/login.jsp?authfailed=true" always-use-default-target="true"/>
    <logout/>
</http>

<authentication-manager>
    <authentication-provider>
        <user-service>
            <user name="test" password="test" authorities="ROLE_USER" />
        </user-service>
    </authentication-provider>
</authentication-manager>
```

This configuration defines the user `test` with password `test` that has been
granted `ROLE_USER` authority.

When first accessing the application, the user will be automatically redirected
to the login page `login.jsp` :
```
...
<form id="loginForm" name="loginForm" action="j_spring_security_check" method="post">
<c:if test="${param.authfailed == 'true'}">
    <span id="infomessage" class="errormessage" >
        Login failed due to: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
    </span>
</c:if>
        <table>
          <tr><td>Username</td><td><input id="usernameField" type="text" name="j_username" value="<c:out value="${SPRING_SECURITY_LAST_USERNAME}"/>"/></td></tr>
          <tr><td>Password</td><td><input id="passwordField" type="password" name="j_password" /></td></tr>

          <tr><td colspan="2" align="right"><input type="submit" value="Login" /></td></tr>
        </table>
</form>
...
```

Spring Security will handle `j_spring_security_check` action to verify `j_username`
and `j_password` and will redirect to `Home.jsp` in case or success or send back to
`login.jsp` (with request parameter `authfaile=true`) in case or authentication
failure.

Finally, we need to configure web application `web.xml` to initiate Spring
Security :
```
...
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:springContext-*.xml</param-value>
    </context-param>

    <!-- Load spring configuration -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <filter>
        <filter-name>springSecurityFilterChain</filter-name>
        <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    </filter>

...
```


## Put it all together ##
You can set the response status to notify the failure of the authentication
request :

```
...
<c:if test="${param.authfailed == 'true'}">
    <%response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);%>
    ...
```

We also need to define filters mappings in the `web.xml` file :
```
    <filter-mapping>
        <filter-name>IpBanner</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <filter-mapping>
        <filter-name>springSecurityFilterChain</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```

When passing through the **IpBanner** Filter, the IP address is checked to know if
the it has been banned. If not, the request is passed to the
`springSecurityFilterChain` that will try authenticate the user. In case of
authentication failure, we are redirect to `login.jsp` page with a
`401/Unauthorized` error status.
The response will then be checked by **IpBanner** Filter and in case of status error
defined in `failureResponseStatusCodes`, the IP address will be added to the
current bucket (or the number of retry will be incremented for this IP address).

# Troubleshooting #
## Servlet security constraint ##
The **IpBanner** filter can't be used associated with a servlet security constraint
because the failed authentication request will be rejected before entering
the application server filter mechanism.

Coming soon...
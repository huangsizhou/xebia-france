# Description #

Tomcat Listener to configure at the middleware level the deactivation of all SSL checks : self-signed, untrusted Certificate Authority, expired, not yet valid or with hostname mismatch certificate.

This listener will deactivate SSL verifications for connections established via :
  * `java.net.URL`
  * [Apache Commons Http Client 3](http://hc.apache.org/httpclient-3.x/)
  * CXF client (>=2.2.7) with configuration
```
<http-conf:conduit>
   <http-conf:tlsClientParameters useHttpsURLConnectionDefaultSslSocketFactory="true" useHttpsURLConnectionDefaultHostnameVerifier="true" />
</http-conf:conduit>
```

# Configuration #

## Standard Tomcat Configuration ##

**server.xml**
```
<Server port="8005" shutdown="SHUTDOWN">
   ...
   <!-- 
     Disable SSL/X509 certificates verifications (self-signed, untrusted Certificate Authority, expired, not yet valid or with hostname mismatch certificate) 
   -->
  <Listener className="fr.xebia.catalina.listener.AcceptAllSslCertificatesListener"/>

</Server>
```


## Parameterized Tomcat Configuration ##

**server.xml**
```
<Server port="8005" shutdown="SHUTDOWN">
   ...
   <!-- 
     Disable SSL/X509 certificates verifications (self-signed, untrusted Certificate Authority, expired, not yet valid or with hostname mismatch certificate) 
   -->
  <Listener 
      enable="${acceptAllSslCertificates}" 
      className="fr.xebia.catalina.listener.AcceptAllSslCertificatesListener"/>

</Server>
```

**catalina.properties**
```
acceptAllSslCertificates=true
```


# Install / Download #

  * Jar : Copy the jar [xebia-tomcat-extras-1.0.1.2.jar](http://xebia-france.googlecode.com/files/xebia-tomcat-extras-1.0.1.2.jar) ([sources](http://xebia-france.googlecode.com/files/xebia-tomcat-extras-1.0.1.2-sources.jar)) in Tomcat's classpath (e.g. under `$TOMCAT_HOME/lib`)
  * Java class : [AcceptAllSslCertificatesListener.java](http://xebia-france.googlecode.com/svn/tomcat/xebia-tomcat-extras/tags/xebia-tomcat-extras-1.0.1.2/src/main/java/fr/xebia/catalina/listener/AcceptAllSslCertificatesListener.java)
  * Java project : `svn checkout http://xebia-france.googlecode.com/svn/tomcat/xebia-tomcat-extras/tags/xebia-tomcat-extras-1.0.1.2/`

# Log Messages #

A warning message is emitted in Tomcat logs because disabling SSL verifications is a security hole.

Then, each time an SSL connection is established with untrusted certificates (self-signed, untrusted Certificate Authority, not yet valid, expired certificates or a mismatch between the URL hostname and the certificate), a warning message is emitted to both reminds user of the security hole and to ease debugging.

**Initialization log message for the enabled listener**

```
...
Mar 9, 2010 11:08:04 AM fr.xebia.catalina.listener.AcceptAllSslCertificatesListener lifecycleEvent
SEVERE: SSL VERIFICATIONS DISABLED ! SECURITY IS JEOPARDIZED ! SHOULD BE USED CAREFULLY IN PRODUCTION !
...
Mar 9, 2010 11:08:12 AM org.apache.catalina.startup.Catalina start
INFO: Server startup in 8360 ms
```

**Log message when an SSL connection is established with an untrusted certificate**

Message for a self-signed expired certificate :

```
Mar 9, 2010 11:11:22 AM fr.xebia.catalina.listener.AcceptAllSslCertificatesListener$AcceptAllX509TrustManager checkServerTrusted
SEVERE: SSL SECURITY IS JEOPARDIZED ! Untrusted self-signed expired certificate: 
   'EMAILADDRESS=cleclerc@xebia.fr, CN=localhost, OU=Xebia, O=Xebia, L=Paris, C=FR' (valid from Sun Sep 13 14:47:07 CEST 2009 until Tue Oct 13 14:47:07 CEST 2009) : 
   sun.security.validator.ValidatorException: PKIX path building failed: 
   sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
```

Message for a self-signed expired certificate with debug logging level enabled :
```
Mar 9, 2010 11:11:22 AM fr.xebia.catalina.listener.AcceptAllSslCertificatesListener$AcceptAllX509TrustManager checkServerTrusted
SEVERE: SSL SECURITY IS JEOPARDIZED ! Untrusted self-signed expired certificate: 
   'EMAILADDRESS=cleclerc@xebia.fr, CN=localhost, OU=Xebia, O=Xebia, L=Paris, C=FR' (valid from Sun Sep 13 14:47:07 CEST 2009 until Tue Oct 13 14:47:07 CEST 2009)
sun.security.validator.ValidatorException: PKIX path building failed: 
   sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
	at sun.security.validator.PKIXValidator.doBuild(PKIXValidator.java:285)
	at sun.security.validator.PKIXValidator.engineValidate(PKIXValidator.java:191)
	at sun.security.validator.Validator.validate(Validator.java:218)
	at com.sun.net.ssl.internal.ssl.X509TrustManagerImpl.validate(X509TrustManagerImpl.java:126)
	at com.sun.net.ssl.internal.ssl.X509TrustManagerImpl.checkServerTrusted(X509TrustManagerImpl.java:209)
	at fr.xebia.catalina.listener.AcceptAllSslCertificatesListener$AcceptAllX509TrustManager.checkServerTrusted(AcceptAllSslCertificatesListener.java:159)
	...
Caused by: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
	...
```

To enable debug level logging, declare in `$CATALINA_BASE/conf/logging.properties` :
```
fr.xebia.catalina.listener.AcceptAllSslCertificatesListener.level=FINE
```

**Log message when an SSL connection is established with a mismatch between the URL's hostname and the certificate**

Message for a `https://127.0.0.1/...` url with `localhost` declared for the certificate `CN` :

```
Mar 9, 2010 11:11:25 AM fr.xebia.catalina.listener.AcceptAllSslCertificatesListener$AcceptAllHostnameVerifier verify
WARNING: SSL SECURITY IS JEOPARDIZED ! SSL hostname mismatch : given hostname=127.0.0.1, 
   sslSession[peer=127.0.0.1:443, peerPrincipal=[EMAILADDRESS=cleclerc@xebia.fr, CN=localhost, OU=Xebia, O=Xebia, L=Paris, C=FR]]
```

**Initialization log message for the disabled listener**

SSL verifications are activated.
```
...
Mar 9, 2010 4:56:28 PM fr.xebia.catalina.listener.AcceptAllSslCertificatesListener lifecycleEvent
INFO: AcceptAllSslCertificatesListener is disabled. SSL verifications are activated.
...
Mar 9, 2010 4:56:41 PM org.apache.catalina.startup.Catalina start
INFO: Server startup in 13005 ms
```
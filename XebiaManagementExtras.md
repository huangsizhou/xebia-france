# Introduction #

Extras on top of Java SE (e.g. Thread Pool) and Spring Framework (e.g. JMS Listener) to ease monitoring. This library provides simple Spring XML namespace based configuration to expose JMX MBeans as well as jsp pages and [Hyperic HQ](http://www.hyperic.com/) plugin to monitor these JMX MBeans.

# Table of Content #



# Monitoring JMX MBeans #

## @Profiled Annotation JMX monitoring ##

See [ProfiledAnnotation](ProfiledAnnotation.md) page. Monitor methods invocations via JMX MBeans declaring:
```
public class MyService {

   @Profiled
   public void myOperation(...) {
      ...
   }
}
```

## Jakarta Commons DBCP `DataSource` JMX monitoring ##

See [ManagedBasicDataSource](ManagedBasicDataSource.md). Offer to your [Jakarta Commons DBCP Data Source](http://commons.apache.org/dbcp/) JMX MBean based monitoring and Spring XML namespace based configuration declaring :
```
<beans ...>
   <management:dbcp-datasource id="myDataSource">
      <management:url value="jdbc:h2:mem:dbcp-test" />
      <management:driver-class-name value="org.h2.Driver" />
      <management:username value="sa" />
      <management:password value="" />
      <management:max-active value="10" />
      <!-- ... any other useful configuration param -->
   </management:dbcp-datasource>
   ...
</beans>
```

## `EhCache` JMX monitoring ##

See [EhCacheSpringConfiguration](EhCacheSpringConfiguration.md). [EhCache](http://ehcache.org/) already has all the JMX MBeans needed to monitor caches, simply expose them declaring:
```
<beans ...>
   ...
   <management:eh-cache-management-service 
      mbean-server="mbeanServer" 
      cache-manager="cacheManager" />

</beans>
```

## Java Util Concurrent Executor Service / Thread Pool Executor JMX Monitoring ##

See [ManagedExecutorService](ManagedExecutorService.md). Declare a fully managed `ExecutorService`/ `ThreadPoolExecutor` with JMX MBean based monitoring declaring:
```
<beans ...>
   <management:executor-service 
       id="my-executor" 
       pool-size="1-10" 
       queue-capacity="5" />
   ...
</beans>
```

## JMS `ConnectionFactory` JMX monitoring ##

See [ManagedJmsConnectionFactory](ManagedJmsConnectionFactory.md). Expose metrics of a JMS `ConnectionFactory` via a JMX MBean with:
```
<beans ...>
   <!-- wrap 'myConnectionFactory' in a JMX enabled connection factory -->
   <management:jms-connection-factory-wrapper 
      id="connectionFactory" 
      connection-factory="myConnectionFactory" />
   ...
</beans>
```

Also offer leaks detection (connection, session, message producer and message consumer).

## Spring JMS Caching Connection Factory JMX monitoring ##

See [ManagedCachingConnectionFactory](ManagedCachingConnectionFactory.md). Expose your Spring JMS `CachingConnectionFactory` via a JMX MBean (reset connections via JMX, etc) declaring:

```
<beans ...>
   <!-- wrap 'rawConnectionFactory' in a JMX enabled caching connection factory -->
   <management:jms-caching-connection-factory 
      id="connectionFactory" 
      connection-factory="rawConnectionFactory" />
   ...

</beans>
```

## Spring JMS `DefaultMessageListenerContainer` JMX monitoring ##

See [ManagedSpringJmsDefaultMessageListenerContainer](ManagedSpringJmsDefaultMessageListenerContainer.md). `ManagedDefaultMessageListenerContainer` adds JMX based monitoring (active consumers count, etc) and control (start and stop) to Spring's JMS Listener Container (DefaultMessageListenerContainer). Simply define the `container-class` like:
```
<beans ...>
   <jms:listener-container 
      connection-factory="connectionFactory" 
      concurrency="5-10"
      container-class="fr.xebia.springframework.jms.ManagedDefaultMessageListenerContainer">
      <jms:listener destination="my-queue" ref="myMessageListener" />
      
   </jms:listener-container>
   ...
</beans>
```

## CXF JMX monitoring ##

See [CxfMonitoring](CxfMonitoring.md). [CXF](http://cxf.apache.org/) already has all the JMX MBeans needed to [monitor web services](http://cxf.apache.org/docs/jmx-management.html). Here is a Hyperic HQ plugin to integrate these web service in your monitoring system.

# JMX helpers #

## Servlet Context Aware MBean Server ##

See [ServletContextAwareMbeanServer](ServletContextAwareMbeanServer.md). Prevent collision of JMX MBeans of collocated web application suffixing ObjectNames with attributes uniquely identifying your web application ( `servletContext.contextPath` , `tomcatServletContext.hostName` , etc) replacing the `<context:mbean-server />` by:

```
<beans ...>

   <management:servlet-context-aware-mbean-server id="mbeanServer"/>
   ...
</beans>
```

## Application Version JMX MBean ##

See [MavenApplicationVersionMBean](MavenApplicationVersionMBean.md). Expose the version of your Maven application in a JMX MBean declaring:
```
<beans ...>

   <management:application-version-mbean />
   ...
</beans>
```

# How to Integrate the `xebia-management-extras` library in your project / Download #

Integration / download details are described on [XebiaManagementExtrasInstallation](XebiaManagementExtrasInstallation.md). The simplest way is to declare the following Maven dependency:
```
<project ...>
   <dependencies>
      <dependency>
         <groupId>fr.xebia.management</groupId>
         <artifactId>xebia-management-extras</artifactId>
         <version>1.2.3</version>
      </dependency>
      ...
   </dependencies>
   ...
</project>
```
> The artifact is available on [Maven Central Repository](http://repo1.maven.org/maven2/), no special `<repository />` declaration is needed.

Manual download is available at [xebia-management-extras-1.2.3.jar](http://repo1.maven.org/maven2/fr/xebia/management/xebia-management-extras/1.2.3/xebia-management-extras-1.2.3.jar) / [xebia-management-extras-1.2.3-sources.jar](http://repo1.maven.org/maven2/fr/xebia/management/xebia-management-extras/1.2.3/xebia-management-extras-1.2.3-sources.jar).

# Developers #

  * Source : https://github.com/xebia-france/xebia-management-extras/

> # Release Notes #

> See [release notes](XebiaManagementExtrasReleaseNotes.md)
# Managed Spring JMS Caching Connection Factory #

`ManagedCachingConnectionFactory` allows to manage a Spring JMS Caching Connection Factory via JMX.

It notably allows you to reset the jms connections, sessions, consumers and producers at runtime via a JMX operation without having to restart your server.

## Spring Configuration ##

  * Spring namespace based configuration:
```
<beans ...
   xmlns:management="http://www.xebia.fr/schema/xebia-management-extras" 
   xsi:schemaLocation="
		http://www.xebia.fr/schema/xebia-management-extras http://www.xebia.fr/schema/management/xebia-management-extras.xsd">

   <!-- wrap 'rawConnectionFactory' in a JMX enabled caching connection factory -->
   <management:jms-caching-connection-factory id="connectionFactory" connection-factory="rawConnectionFactory" />
   ...
   <!-- mbean exporter will register the spring jms caching connection factory mbean -->
   <context:mbean-export server="..." />

</beans>
```
  * Spring raw configuration
```
<beans ...>
   ...
   <!-- wrap 'rawConnectionFactory' in a JMX enabled caching connection factory -->
   <bean id="cachingConnectionFactory" class="fr.xebia.springframework.jms.ManagedCachingConnectionFactory">
      <property name="targetConnectionFactory" ref="rawConnectionFactory" />
   </bean>
   ...
   <!-- mbean exporter will register the jms connection factory mbean -->
   <context:mbean-export server="..." />
</beans>
```

## Configuration attributes ##

| **Name** | **Req'd** | **Default Value** |  **Description** |
|:---------|:----------|:------------------|:-----------------|
| `id`     | Yes       | N/A               | Id of the bean, used to build the object name as `"javax.jms:type=ConnectionFactory,name=${id}"` |
| `connection-factory` | Yes       | N/A               | name of the wrapped JMS ConnectionFactory |
| `cache-consumers` | No        | 'true'            | Indicates whether MessageConsumers should be cached |
| `cache-producers` | No        | 'true'            | Indicates whether MessageProducers should be cached |
| `reconnect-on-exception` | No        | 'true'            | Indicates weither a connection should be closed and reopened when a JMSException occurs |
| `session-cache-size` | No        | '1'               | Number of active JMS sessions. Default value is 1. Consider increasing this number in highly concurrent environments. |

## JMX MBean Object Name ##

  * JMX Object Name: `"javax.jms:type=CachingConnectionFactory,name=${spring-bean-name}"` where `"${spring-bean-name}"` is the name of the spring object.

## Managed Spring Jms Caching Connection Factory Collected Metrics / JMX MBeans attributes ##

Here are the configuration parameters exposed by the ManagedCachingConnectionFactory:

| **Name** | **Type** | **Description** |
|:---------|:---------|:----------------|
| `ReconnectOnException` | Configuration Parameter |  Return whether the single Connection should be renewed when a JMSException is reported by the underlying Connection (read/write attribute) |
| `CacheProducers` | Configuration Parameter | Return whether to cache JMS MessageProducers per JMS Session instance (read/write attribute) |
| `CacheConsumers` | Configuration Parameter | Return whether to cache JMS MessageConsumers per JMS Session instance  (read/write attribute) |
| `SessionCacheSize` | Configuration Parameter |  Return the desired size for the JMS Session cache (per JMS Session type) (read/write attribute) |
| `resetConnection` | Operation |  Resets the Connection, the open sessions and the producers and consumers if they are cached |

## Implementation details ##

  * `ManagedCachingConnectionFactory` is a subclass of `org.springframework.jms.connection.CachingConnectionFactory`. It has been tested with Active MQ 5.3.2 and WebSphere MQ 7.0.1.2.

## JSP / VisualVM / Hyperic HQ rendering ##

  * Visual VM / JConsole view:
> <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-jms-caching-connection-factory.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-jms-caching-connection-factory.png' height='200' /></a>
> <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-jms-caching-connection-factory-operations.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-jms-caching-connection-factory-operations.png' height='200' /></a>
  * Hyperic plugin: [tomcat-extras-jms-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-jms-plugin.xml), see [XebiaManagementExtrasHypericPlugins](XebiaManagementExtrasHypericPlugins.md)

## How to integrate the xebia-management-extras` library in your project ? ##

See [XebiaManagementExtrasInstallation](XebiaManagementExtrasInstallation.md).

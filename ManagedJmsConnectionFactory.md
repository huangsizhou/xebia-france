# JMS Connection Factory Monitoring #

`ManagedConnectionFactory` allows to monitor JMS resources. Exceptions ratios will help you to diagnose failures of the JMS Broker, increase of the send duration will help you to see saturation of the JMS Broker and changes of the creation rates will indicate changes in the usage of your application.

## Spring Configuration ##

  * Spring namespace based configuration:
```
<beans ...
   xmlns:management="http://www.xebia.fr/schema/xebia-management-extras" 
   xsi:schemaLocation="
		http://www.xebia.fr/schema/xebia-management-extras http://www.xebia.fr/schema/management/xebia-management-extras.xsd">

     <!-- Wrap the 'rawConnectionFactory' to expose its metrics via a JMX MBean --> 
     <management:jms-connection-factory-wrapper 
             id="connectionFactory" 
             connection-factory="rawConnectionFactory" />


   <!-- mbean exporter will register the spring jms caching connection factory mbean -->
   <context:mbean-export />

</beans>
```
  * Spring raw configuration
```
<beans ...>
   ...
   <!-- Wrap the 'rawConnectionFactory to expose its metrics via a JMX MBean -->
   <bean id="connectionFactory" class="fr.xebia.management.jms.SpringManagedConnectionFactory">
      <property name="connectionFactory" ref="rawConnectionFactory" />
   </bean>
   ...
   <!-- mbean exporter will register the jms connection factory mbean -->
   <context:mbean-export server="..." />
</beans>
```

## Configuration attributes ##

| **Name** | **Req'd** | Type | **Default Value** |  **Description** |
|:---------|:----------|:-----|:------------------|:-----------------|
| `id`     | Yes       | string / bean-id | N/A               | Id of the bean, used to build the object name as `"javax.jms:type=ConnectionFactory,name=${id}"` |
| `connection-factory` | string / bean-id | Yes  | N/A               | name of the wrapped JMS ConnectionFactory |
| `track-leaks` | boolean or property placeholder  | No   | `false`           | Enable leaks detector (**WARNING**: avoid on production du to performance and memory impact) |

## JMX MBean Object Name ##

  * JMX Object Name: `"javax.jms:type=ConnectionFactory,name=${spring-bean-name}"` where `"${spring-bean-name}"` is the name of the spring object.

## Managed Jms Connection Factory Collected Metrics / JMX MBeans attributes ##

Here are the metrics monitored by the ManagedJmsConnectionFactory:

| **Name** | **Indicator Type** | **Description** |
|:---------|:-------------------|:----------------|
| `SendMessageCount` | Trends Up          |  Number of sent JMS messages |
| `SendMessageExceptionCount` | Trends Up          | Number of exceptions sending messages  |
| `SendMessageDurationInMillis` | Trends Up          | Duration spent sending messages |
| `ReceiveMessageCount` | Trends Up          |  Number of received JMS messages |
| `ReceiveMessageExceptionCount` | Trends Up          |  Number of exceptions receiving JMS messages |
| `ReceiveMessageDurationInMillis` | Trends Up          |  Duration spent receiving JMS messages,  |
| `ActiveConnectionCount` | Dynamic            |  Number of active JMS connections |
| `ActiveMessageProducerCount` | Dynamic            | Number of active JMS message producers  |
| `ActiveMessageConsumerCount` | Dynamic            | Number of active JMS message consumers |
| `ActiveSessionCount` | Dynamic            |  Number of active JMS sessions |
| `CreateConnectionCount` | Trends Up          |  Number of created JMS connections |
| `CreateConnectionExceptionCount` | Trends Up          | Number of exceptions creating connections  |
| `CreateMessageProducerCount` | Trends Up          | Number of created JMS message producers  |
| `CreateMessageProducerExceptionCount` | Trends Up          | Number of exceptions creating message producers  |
| `CreateMessageConsumerCount` | Trends Up          | Number of created JMS message consumers |
| `CreateMessageConsumerExceptionCount` | Trends Up          | Number of exceptions creating message consumers  |
| `CreateSessionCount` | Trends Up          |  Number of created JMS sessions |
| `CreateSessionExceptionCount` | Trends Up          |  Number of exceptions creating sessions |
| `CloseConnectionCount` | Trends Up          |  Number of closed JMS connections |
| `CloseMessageProducerCount` | Trends Up          | Number of closed JMS message producers  |
| `CloseMessageConsumerCount` | Trends Up          | Number of closed JMS message consumers |
| `CloseSessionCount` | Trends Up          |  Number of closed JMS sessions |
| `dumpAllOpenedResources()` | Operation          |  List of created resource as string (name + creation date, thread-name, thread dump) |

Key metrics are usually:
  * `ActiveConnectionCount`, `ActiveSessionCount`, `ActiveMessageConsumerCount` and `ActiveMessageProducerCount` to monitor the flow of messages,
  * `SendMessageCount` and `ReceiveMessageCount` to monitor the flow of messages,
  * `SendMessageExceptionCount` and `ReceiveMessageExceptionCount` to detect problems in the flow of messages,
  * `SendMessageDurationInMillis` to detect slowness in the JMS layer.

Trends Up indicators are always increasing counters. The monitoring system is expected to convert these metrics into "per minute" metrics (e.g. `TaskCount` should be exposed as `TaskCountPerMinute`).

## Leaks Detector ##


Detect `Connection`, `Session`, `MessageProducer`, `MessageConsumer` and `Topicsubscriber` leaks.

**WARNING: enabling the leak detector should be avoided on production because it has a performance and memory impact generating an exception at each resource creation to collect a thread dump.**

List of open resources is available via JMX with the creation context (time, thread name and stack trace).


Sample of "`dumpAllOpenedResources()`" call on an application using a Spring `DefaultMessageListenerContainer`:

<a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-jms-operations.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-jms-operations.png' height='150' /></a>

<a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-jms-dump-open-resources-result.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-jms-dump-open-resources-result.png' height='150' /></a>

Full logs [dump-all-opened-resources.log](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-jms-dump-open-resources-result.log)

## Implementation details ##

  * `ManagedConnectionFactory` is a wrapper of a JMS 1.1 `javax.jms.ConnectionFactory`. It has been tested with Active MQ 5.3.2 and WebSphere MQ 7.0.1.2.
  * A non Spring enabled version is available with `fr.xebia.management.jms.ManagedConnectionFactory`,
  * This `ManagedConnectionFactory` is not intended to help you to diagnose JMS resources leaks and thus does not monitor resource's `close()` calls.

## JSP / VisualVM / Hyperic HQ rendering ##

  * JSP monitoring page : [jms.jsp (source)](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/webapp/jmx/jms.jsp),
> > <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-jsp-jms.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-jsp-jms.png' height='200' /></a>
  * Visual VM / JConsole view:
> > <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-jms.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-jms.png' height='200' /></a>
  * Hyperic plugin: [tomcat-extras-jms-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-jms-plugin.xml), see XebiaManagementExtrasHypericPlugins.

## How to integrate the xebia-management-extras` library in your project ? ##

See [XebiaManagementExtrasInstallation](XebiaManagementExtrasInstallation.md).
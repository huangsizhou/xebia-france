# Managed Spring JMS `DefaultMessageListenerContainer` #

`ManagedDefaultMessageListenerContainer` adds JMX based monitoring and control (start and stop) to Spring's JMS Listener Container (`DefaultMessageListenerContainer`).

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

The `ManagedDefaultMessageListenerContainer` is declared as the class implementation of the `<jms:listener-container />`.

## JMS Listener Container collected metrics / JMX MBeans attributes ##

Here are the metrics, configuration parameters and operations exposed by the `ManagedDefaultMessageListenerContainer`:

| **Name** | **Indicator Type** | **Description** |
|:---------|:-------------------|:----------------|
| `ActiveConsumerCount` | Dynamic            |  Number of currently active message consumers  |
| `PausedTaskCount` | Dynamic            |  Number of currently paused tasks  |
| `ScheduledConsumerCount` | Dynamic            |  Number of currently scheduled consumers  |
| `Running` | Dynamic            |  Indicates whether the listener container is started or not  |
| `ConcurrentConsumers` | Configuration parameter |  Number of concurrent message consumers |
| `MaxConcurrentConsumers` | Configuration parameter | TODO            |
| `DestinationName` | Configuration parameter | Name of the listened destination |
| `IdleConsumerLimit` | Configuration parameter |                 |
| `start`  | Management operation | Start listening messages on the destination |
| `start`  | Management operation | Stop listening messages on the destination |


## JMX MBeans ##

One JMX MBean is registered for each `<jms:listener-container />`:
The MBean Object pattern is `"javax.jms:type=MessageListenerContainer,name=${spring-mbean-name},destination=${destination-name}"`.

## JSP / VisualVM / Hyperic HQ rendering ##

  * JSP monitoring page : [jms.jsp (source)](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/webapp/jmx/jms.jsp),
> > <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-jsp-jms-listener.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-jsp-jms-listener.png' height='200' /></a>
  * Visual VM / JConsole view:
> > <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-jms-listener.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-jms-listener.png' height='200' /></a>
  * Hyperic plugin: [tomcat-extras-jms-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-jms-plugin.xml), see XebiaManagementExtrasHypericPlugins.

## How to integrate the xebia-management-extras` library in your project ? ##

See [XebiaManagementExtrasInstallation](XebiaManagementExtrasInstallation.md).
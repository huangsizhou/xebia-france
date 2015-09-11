# Using VisualVM #

# Lab #

## Get server prod 1 counter via Visualvm ##

  1. Add travel-prod-1.xebia-tech-event.info as Remote Host
  1. Add JMX Connection on port 1099
  1. Locate `ObjectName` and attribute name :
    1. `CreditCardService` and all exposed metrics
    1. Java Memory Pool (Eden, Tenured and Survivor Space) usage
    1. `GarbageCollector`  (Copy and `MarkSweepCompact`) collection count and time
    1. CPU usage on `OperatingSystem` MBean
  1. Graph `TimeoutExceptionCount` for `CreditCardService`


---


## Tips ##

### Navigate on MBean ###

Download [VisualVM](http://visualvm.java.net/download.html) and install it.

Install VisualVM-MBeans plugins. Open the plugin manager in Tools menu bar.

### JMX Remote connection ###

JMX uses 2 ports for remote connection. One for JNDI and one for JMX.
JNDI is fixed (1099 by default), JMX is dynamic.

  * Simple URL by JNDI and RMI (Sun standard)

`<hostname>:<jndiPort>`

Ex: travel-prod-1.xebia-tech-event.info:1099

  * URL with protocol

`service:jmx:<protocol>://[host[:port]][url-path]`

Simple example :

`service:jmx:rmi:///jndi/rmi://travel-prod-1.xebia-tech-event.info:1099/jmxrmi`

Example with static JMX port :

`service:jmx:rmi://travel-prod-1.xebia-tech-event.info/jndi/rmi://travel-prod-1.xebia-tech-event.info:1099/travel-prod-1.xebia-tech-event.info/7676/jmxrmi`

### Get `ObjectName` from VisualVM ###

You can get `ObjectName` and attribute in metadata tab.

![http://xebia-france.googlecode.com/svn/wiki/oss-monitoring/getMetainfInVisualVm.png](http://xebia-france.googlecode.com/svn/wiki/oss-monitoring/getMetainfInVisualVm.png)
# Introduction #

Hyperic plugins for xebia-management-extras mbeans.

# Hyperic plugins list #

  * [tomcat-extras-service-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-service-plugin.xml): see [ProfiledAnnotation](ProfiledAnnotation.md)
  * [tomcat-extras-ehcache-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-ehcache-plugin.xml)
  * [tomcat-extras-jms-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-jms-plugin.xml): see [ManagedJmsConnectionFactory](ManagedJmsConnectionFactory.md)
  * [tomcat-extras-executorservice-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-executorservice-plugin.xml):see [ManagedExecutorService](ManagedExecutorService.md)
  * [tomcat-extras-dbcp-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-dbcp-plugin.xml): see TODO
  * [tomcat-extras-ehcache-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-ehcache-plugin.xml): see TODO
  * [tomcat-extras-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-plugin.xml): extras for two Tomcat 6 components that have been forgotten by Hyperic tomcat-plugin : global data sources and executors,
  * [tomcat-extras-cxf-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-cxf-plugin.xml): monitoring of CXF endpoints and clients,
  * [tomcat-extras-hibernate-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-hibernate-plugin.xml): Hibernate monitoring.

# Installation notes #

Copy the hyperic plugin xml file under ` ${HYPERIC_HOME}/hq-plugins ` (e.g. next to ` ${HYPERIC_HOME}/agent-4.5.0 `.

These Hyperic Hq plugin assume that the JMX MBeans are declared through the [&lt;management:servlet-context-aware-mbean-server /&gt;](ServletContextAwareMbeanServer.md) and thus require the presence of the `path` and `path` attributes in the ObjectNames of the mbeans.

# Why are these plugins extensions of tomcat-plugin ? #

The xebia-management-extras Hyperic plugins extend the Hyperic tomcat-plugin to benefit of the autodiscovery of the Tomcat servers.

The benefit is that the application using xebia-management-extras ontop of a Tomcat server already managed by Hyperic don't have any Hyperic configuration taks. The MBeans are auto discovered.

The disadvantage of this approach of extending tomcat-plugin is that these plugin will not work on another application server (WebSphere, Weblogic, JBoss, Glassfish, etc).

Another approach could have been to make the xebia-management-extras plugins extend the jvm-plugin and to configure them in Hyperic independently of the already managed Java application server. The benefit would have been to have plugins that could be used on any application server. The disadvantage would have been to loose Hyperic auto discovery.

The idea to deal with other servers than Tomcat is to migrate them and create versions for the other servers (e.g. websphere-extras-ehcache next to tomcat-extras-ehcache) according to users requests.

# Troubleshooting #

**How to ensure Hyperic Server has discovered the added plugins ?**

In Hyperic GUI, "Administration / Monitoring Defaults", verify that your plugins appear under the "Apache Tomcat 6.0" server type.

> <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/hyperic-administration.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/hyperic-administration.png' width='200' /></a>

> <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/hyperic-administration-xebia-plugins.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/hyperic-administration-xebia-plugins.png' width='200' /></a>

Search in `${HYPERIC_HOME}/server-4.5/logs/server.log` for a message like:
```

2010-11-12 00:54:28,395 INFO  [main] [org.hyperic.hq.product.server.session.ProductManagerImpl@284] tomcat-extras-cxf unknown -- registering
2010-11-12 00:54:28,456 INFO  [main] [org.hyperic.hq.product.server.session.ProductManagerImpl@284] tomcat-extras-dbcp unknown -- registering
2010-11-12 00:54:28,493 INFO  [main] [org.hyperic.hq.product.server.session.ProductManagerImpl@284] tomcat-extras-ehcache unknown -- registering
2010-11-12 00:54:28,530 INFO  [main] [org.hyperic.hq.product.server.session.ProductManagerImpl@284] tomcat-extras-executorservice unknown -- registering
2010-11-12 00:54:28,566 INFO  [main] [org.hyperic.hq.product.server.session.ProductManagerImpl@284] tomcat-extras-hibernate unknown -- registering
2010-11-12 00:54:28,597 INFO  [main] [org.hyperic.hq.product.server.session.ProductManagerImpl@284] tomcat-extras-jms unknown -- registering
2010-11-12 00:54:28,655 INFO  [main] [org.hyperic.hq.product.server.session.ProductManagerImpl@284] tomcat-extras unknown -- registering
2010-11-12 00:54:28,701 INFO  [main] [org.hyperic.hq.product.server.session.ProductManagerImpl@284] tomcat-extras-service unknown -- registering
```

**How to ensure Hyperic Server has discovered the added plugins ?**

Search in `${HYPERIC_HOME}/agent-4.5/logs/agent.log` for a message like:

```

2010-11-12 00:54:03,096 INFO  [Thread-1] [ProductPluginManager] Loading plugin: tomcat-extras-cxf-plugin.xml
2010-11-12 00:54:03,155 INFO  [Thread-1] [ProductPluginManager] Loading plugin: tomcat-extras-dbcp-plugin.xml
2010-11-12 00:54:03,168 INFO  [Thread-1] [ProductPluginManager] Loading plugin: tomcat-extras-ehcache-plugin.xml
2010-11-12 00:54:03,210 INFO  [Thread-1] [ProductPluginManager] Loading plugin: tomcat-extras-executorservice-plugin.xml
2010-11-12 00:54:03,238 INFO  [Thread-1] [ProductPluginManager] Loading plugin: tomcat-extras-hibernate-plugin.xml
2010-11-12 00:54:03,301 INFO  [Thread-1] [ProductPluginManager] Loading plugin: tomcat-extras-jms-plugin.xml
2010-11-12 00:54:03,341 INFO  [Thread-1] [ProductPluginManager] Loading plugin: tomcat-extras-plugin.xml
2010-11-12 00:54:03,370 INFO  [Thread-1] [ProductPluginManager] Loading plugin: tomcat-extras-service-plugin.xml
```
[EhCache](http://ehcache.org/) already has all the JMX MBeans needed to monitor caches, we just proposed a simplified Spring configuration, a jsp page to watch the statistics and an Hyperic plugin.

## Spring Configuration ##

Spring configuration to expose `EhCache` JMX MBeans:

  * Spring namespace based configuration :
```
<beans ...
   xmlns:management="http://www.xebia.fr/schema/xebia-management-extras"
   xsi:schemaLocation="...
      http://www.xebia.fr/schema/xebia-management-extras http://www.xebia.fr/schema/management/xebia-management-extras.xsd">
   ...
   <management:eh-cache-management-service mbean-server="mbeanServer" cache-manager="cacheManager" />

</beans>
```
  * Spring raw configuration:
```
<beans ...>
   ...
   <bean id="managementService" class="net.sf.ehcache.management.ManagementService" init-method="init" destroy-method="dispose">
      <constructor-arg ref="cacheManager" />
      <constructor-arg ref="mbeanServer" />
      <constructor-arg index="2" value="true" />
      <constructor-arg index="3" value="true" />
      <constructor-arg index="4" value="true" />
      <constructor-arg index="5" value="true" />
   </bean>
</beans>
```

## JSP / VisualVM / Hyperic HQ rendering ##

  * JSP monitoring page : [ehcache.jsp (source)](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/webapp/jmx/ehcache.jsp),
> > <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-jsp-ehcache.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-jsp-ehcache.png' height='200' /></a>

  * Hyperic plugin: [tomcat-extras-ehcache-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-ehcache-plugin.xml), see XebiaManagementExtrasHypericPlugins.

## How to integrate the xebia-management-extras` library in your project ? ##

See [XebiaManagementExtrasInstallation](XebiaManagementExtrasInstallation.md).
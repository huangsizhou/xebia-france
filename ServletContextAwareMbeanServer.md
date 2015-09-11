# Servlet Context Aware MBean Server #



&lt;management:servlet-context-aware-mbean-server /&gt;

 helps to prevent collisions between JMX MBeans of collocated web applications.

A problem with MBean Servers is that they work at the JVM level and that several web applications may want to register mbeans with the same name. This is typically the case with mbeans exposed by common libraries/frameworks like  EH Cache or CXF.

To ensure that colocating several web applications on the same servlet engine (Tomcat, Websphere, Weblogic, JBoss, etc), we developed a thin MBeanServer wrapper which appends to mbeans names the following attributes:
  * **`"path"`**: the web application path (`servletContext.contextPath`),
  * if the servlet container is Tomcat, **`"host"`**: the web application hostname,
  * any extra attribute defined by configuration.

## Spring Configuration ##

  * Spring namespace based configuration:
```
<beans ...
   xmlns:management="http://www.xebia.fr/schema/xebia-management-extras"
   xsi:schemaLocation="...
      http://www.xebia.fr/schema/xebia-management-extras http://www.xebia.fr/schema/management/xebia-management-extras.xsd">

   <!-- namespace based servlet-context-aware-mbean-server automatically locate the mbean server -->
   <management:servlet-context-aware-mbean-server id="mbeanServer"/>

</beans>
```

> The 

&lt;management:servlet-context-aware-mbean-server/&gt;

  auto-detects `WebLogic` 9+, `WebSphere` 5.1+ and the JDK 1.5+ platform MBeanServer and wrap it to add the `path` and `host` attributes.

  * Spring raw configuration:
```
 <beans ... >
    <context:mbean-server id="rawMbeanServer" />
    <bean id="mbeanServer" class="fr.xebia.management.ServletContextAwareMBeanServerFactory">
       <property name="server" ref="rawMbeanServer" />
    </bean>
    ...
 </beans>
```

The `ServletContextAwareMBeanServerFactory` is slightly less smarter than the 

&lt;management:servlet-context-aware-mbean-server/&gt;

: you need to define the mbeanServer to wrap ; it cannot auto detect it (by consistency with Spring MBeanServerFactoryBean and 

&lt;context:mbean-server /&gt;

 behavior).

## Advanced Configuration ##

The `"objectNameExtraAttributes"` property allows to manually add extra attributes in addition to the `"path"` (and `"host"`) attribute that is automatically added.

```
 <beans ... >
    <context:mbean-server id="rawMbeanServer" />
    <bean id="mbeanServer" class="fr.xebia.management.ServletContextAwareMBeanServerFactory">
       <property name="mbeanServer" ref="rawMbeanServer" />
       <property name="objectNameExtraAttributes" >
          <map>
             <entry key="ze-app-id-asked-by-ze-monitoring-team" value="my-application-id" />
          </map>
       </property>
    </bean>
    <context:mbean-export server="mbeanServer" />
    ...
 </beans>
```

An object name `"net.sf.ehcache:CacheManager=my-cache-manager,name=my-cache,type=Cache"` will be registered as `"net.sf.ehcache:CacheManager=my-cache-manager,ze-app-id-asked-by-ze-monitoring-team=my-application-id,name=my-cache,type=Cache,host=localhost,path=/my-application"` for an application "my-application" declared in the "localhost" host of a Tomcat server: attributes `"path=/my-application"`,  `"host=localhost"` and `"ze-app-id-asked-by-ze-monitoring-team=my-application-id"` are added to the object name.
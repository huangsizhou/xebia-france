# CXF Monitoring #

[CXF](http://cxf.apache.org/) already has all the JMX MBeans needed to [monitor web services](http://cxf.apache.org/docs/jmx-management.html). Here is a Hyperic HQ plugin to integrate these web service in your monitoring system.

## Spring configuration ##

Use the standard CXF configuration for Spring:
```
<beans ...>

   <bean id="cxf" class="org.apache.cxf.bus.CXFBusImpl">
      <property name="id" value="my-cxf-bus" />
   </bean>
   <bean id="org.apache.cxf.management.InstrumentationManager" class="org.apache.cxf.management.jmx.InstrumentationManagerImpl">
      <property name="server" ref="mbeanServer" />
      <property name="enabled" value="true" />
      <property name="createMBServerConnectorFactory" value="false" />
   </bean>
   <bean id="CounterRepository" class="org.apache.cxf.management.counters.CounterRepository">
      <property name="bus" ref="cxf" />
   </bean>

   <jaxws:endpoint id="myService" implementor="#myServiceImpl" address="/myService" >
      <jaxws:features>
         <bean class="org.apache.cxf.management.interceptor.ResponseTimeFeature" />
      </jaxws:features>
      <jaxws:properties>
         <entry key="faultStackTraceEnabled" value="true" />
      </jaxws:properties>
   </jaxws:endpoint>
   ...
</beans>
```

Don't forget to specify the "`id`" property of the bus (e.g. "my-cxf-bus") ; otherwise, CXF will generate a random value that will change at each restart and confuse your monitoring system.

## JSP / VisualVM / Hyperic HQ rendering ##

  * JSP monitoring page : [cxf.jsp (source)](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/webapp/jmx/cxf.jsp),
> > <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-jsp-cxf.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-jsp-cxf.png' height='200' /></a>

  * Hyperic plugin: [tomcat-extras-cxf-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-cxf-plugin.xml), see XebiaManagementExtrasHypericPlugins.
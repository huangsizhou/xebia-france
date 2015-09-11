# @Profiled Annotation #

`@Profiled` is a java annotation which allows to monitor methods execution declaratively and expose all the indicators via a JMX MBean simply like this:
```
public class MyService {

   @Profiled
   public void myOperation(...) {
      ...
   }
}
```

## @Profiled Annotation Attributes ##

| **Name** | **Req'd** | **Default Value** | **Description** |
|:---------|:----------|:------------------|:----------------|
| `name`   | N         | `${class.name}.${method.name} ` | Identifier of the profiled method. This identifier can be plain text or use Spring Expression Language with parameters `args` and `invokedObject`.<br />Sample : `@Profiled(name = "my-invocation(#{args[0]}-#{args[1]})") ` |
| `slowInvocationThresholdInMillis` | N         | 500               | Lower bound of the interval of slow invocations reported under the `SlowInvocationCount` counter (the upper bound of this interval is the `verySlowInvocationThresholdInMillis` attribute) |
| `verySlowInvocationThresholdInMillis` | N         | 1000              | Upper bound of the slow invocations counter. Invocations that are longer than this threshold are reported in the `VerySlowInvocationCount` counter |
| `communicationExceptionsTypes` | N         | ` {IOException.class} ` | List of types of exceptions reported by the `CommunicationExceptionCount` counter. An exception is seen as a communication exception if it is an instance or one of its nested cause is an instance of one of the `communicationExceptionsTypes`. |
| `businessExceptionsTypes` | N         | ` {} `            | List of types of exceptions reported by the `BusinessExceptionCount`. An exception is seen as a communication exception if it is an instance or one of its nested cause is an instance of one of the `businessExceptionsTypes`. |

Note : if an exception is at the same time a communication exception and a business exception, only the `BusinessExceptionCount` counter is incremented.

## @Profiled Annotation Collected Metrics / JMX MBeans attributes ##

Here are the metrics monitored by the @Profiled annotation:

| **Name** | **Indicator Type** | **Description** |
|:---------|:-------------------|:----------------|
| `ActiveCount` | Dynamic            |  Number of currently active invocations  |
| `InvocationCount` | Trends Up          | Total number of invocations |
| `SlowInvocationCount` | Trends Up          | Total number of slow invocation (`@Profiled.slowInvocationThreshold < invocationDuration < @Profiled.verySlowInvocationThreshold`) |
| `VerySlowInvocationCount` | Trends Up          | Total number of very slow invocations (`@Profiled.verySlowInvocationThreshold < invocationDuration`) |
| `BusinessExceptionCount` | Trends Up          | Total number of business exceptions (invocations throwing exceptions which are instances or which have a nested cause that is an instance of one of the @Profiled.businessExceptionTypes) |
| `CommunicationExceptionCount` | Trends Up          | Total number of communication exceptions (invocations throwing exceptions which are instances or which have a nested cause that is an instance of one of the @Profiled.communicationExceptionTypes) |
| `OtherExceptionCount` | Trends Up          | Total number of other exceptions (invocations throwing exceptions which are not communication nor business exceptions) |
| `TotalDurationInNanos` | Trends Up          |  Total duration of the invocations in nano seconds |
| `TotalDurationInMillis` | Trends Up          |  Total duration of the invocations in milli seconds |

Notes :
  * Trends Up indicators are always increasing counters. The monitoring system is expected to convert these metrics into "per minute" metrics (e.g. `InvocationCount` should be exposed as `InvocationCountPerMinute`).
  * Duration in millis is provided to be more human friendly but duration in nanos is less expensive to compute and thus should be preferred by monitoring tools


## Spring Configuration ##

  * Spring namespace based configuration :
```
<beans ...
   xmlns:management="http://www.xebia.fr/schema/xebia-management-extras"
   xsi:schemaLocation="...
      http://www.xebia.fr/schema/xebia-management-extras http://www.xebia.fr/schema/management/xebia-management-extras.xsd">

   <!-- enable AOP to intercept @Profiled annotation -->
   <aop:aspectj-autoproxy />
   <!-- mbean server wil hold the ServiceStatistics mbeans used by @Profiled -->
   <context:mbean-server />
   
   <management:profile-aspect server="mbeanServer" />
   ...
</beans>
```
  * Spring raw configuration:
```
<beans ...>

   <!-- enable AOP to intercept @Profiled annotation -->
   <aop:aspectj-autoproxy />
   <!-- mbean server wil hold the ServiceStatistics mbeans used by @Profiled -->
   <context:mbean-server />

   <bean id="profileAspect" class="fr.xebia.management.statistics.ProfileAspect" />
   ...
</beans>
```

Note : If you face a `IllegalArgumentException: MetadataMBeanInfoAssembler does not support JDK dynamic proxies - export the target beans directly or use CGLIB proxies instead`, then you need configure CGLib proxies in AspectJ adding the attribute `proxy-target-class="true"` like `<aop:aspectj-autoproxy proxy-target-class="true" />`.

## JMX MBeans ##

One JMX MBean is registered for each different name of `@Profiled` annotation.
The MBean Object Name pattern is `"fr.xebia:type=ServiceStatistics,name=${name}"`.

The `"${name}"` is defined declaratively (`@Profiled(name="my-operation")`) or build from the class and method name with one of the following strategies :
  * FULLY\_QUALIFIED\_NAME (e.g. `"com.mycompany.MyService.myMethod"`)
  * COMPACT\_FULLY\_QUALIFIED\_NAME (e.g. `"c.m.MyService.myMethod"`)
  * SHORT\_NAME (e.g. `"MyService.myMethod"`)



## Samples ##

  * Basic java sample:
```
package com.mycompany;

import fr.xebia.management.statistics.Profiled;

public class MyService {

   @Profiled
   public void myOperation(...) {
      ...
   }
}

Will create a MBean with Object Name : "{{{fr.xebia:type=ServiceStatistics,name=com.mycompany.MyService.myOperation}}}".
 
```
  * Advanced java sample:
```
public class MyService {

   @Profiled(
      name = "myOperation(customerType-#{args[0]}-country-#{invokedObject.countryCode})", 
      businessExceptionsTypes = {MyBusinessException.class}, 
      communicationExceptionsTypes = {IOException.class, ...}, 
      slowInvocationThresholdInMillis = 100, 
      verySlowInvocationThresholdInMillis = 200)
   public void myOperation(String customerType, ...) throws MybusinessException {
      ...
   }
   
   public String getCountryCode() {...}
}
```

Will create a MBean with Object Name "`fr.xebia:type=ServiceStatistics,name=myOperation(customerType-VIP-country-FR)`" for invocations with parameter `customerType=FR` and for `countryCode=FR`.


## JSP / VisualVM / Hyperic HQ rendering ##

  * JSP monitoring page : [(source)](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/webapp/jmx/profiled-services.jsp),
> > <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-jsp-profiled-service.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-jsp-profiled-service.png' height='200' /></a>
  * Visual VM / JConsole view:
> > <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-profiled-service.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-profiled-service.png' height='200' /></a>

  * Hyperic plugin: [tomcat-extras-service-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-service-plugin.xml), see [XebiaManagementExtrasHypericPlugins](XebiaManagementExtrasHypericPlugins.md).

## How to integrate the xebia-management-extras library in your project ? ##

See [XebiaManagementExtrasInstallation](XebiaManagementExtrasInstallation.md).

## Raw usage of the `ServiceStatistics` bean ##

You can directly use the underlying `ServiceStatistics` bean without the `@Profiled` annotation. It can be particularly useful if the profiled code can not elegantly be isolated in a dedicated method.

`ServiceStatistics` usage demo:
```
long nanosBefore = System.nanoTime();
serviceStatistics.incrementCurrentActiveCount();
try {
    // ... do profiled job
} catch (Throwable t) {
    serviceStatistics.incrementExceptionCount(t);
    throw t;
} finally {
    serviceStatistics.decrementCurrentActiveCount();
    serviceStatistics.incrementInvocationCounterAndTotalDurationWithNanos(System.nanoTime() - nanosBefore);
}
```

Spring configuration based `ServiceStatistics` initialization demo:
```
<beans ...>
   <!-- mbean exporter is in charge of registering the ServiceStatistics MBean -->
   <context:mbean-export />

   <bean id="my-backend-statistics" class="fr.xebia.management.statistics.ServiceStatistics">
      <property name="name" value="my-service" />
      <property name="communicationExceptionsTypes">
         <array>
            <value>java.io.IOException</value>
            <value>com.mycompany.mybackend.CommunicationException</value>
         </array>
      </property>
      <property name="businessExceptionsTypes">
         <array>
            <value>com.mycompany.mybackend.BusinessException</value>
         </array>
      </property>
      <property name="slowInvocationThresholdInMillis" value="75" />
      <property name="verySlowInvocationThresholdInMillis" value="150" />
   </bean>
</beans>
```

## Implementation decisions and details ##

### Declarative approach and AOP ###

We decided to use an annotation based declarative approach that would be homogeneous with the Spring 2.5+ & Java EE 5+ programming styles with all their annotations (security - `@RolesAllowed`), transactions - `@Transactional`), etc ).

Developers would just have to decorate their methods with a `@Profiled` annotation.

This annotation based technique is the easiest to integrate but it comes with constraints:
  * profiling granularity is at the method scope when we sometime need to profile smaller blocks of code,
  * AOP interceptor and expression language can introduce of performance overhead that is not suited for the duration of the profiled block of code. These performance impacts are completely negligible to profile calls to backends (e.g. jdbc, http, rmi/iiop, ldap, etc).
If such constraint is a problem for your use case, you can use the raw `ServiceStatistics` or directly use a bunch of `AtomicInteger` and `AtomicLong` for profiling.

`@Profiled` annotated methods are intercepted at runtime thanks to [Spring AOP](http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/aop.html) and [AspectJ](http://www.eclipse.org/aspectj/)'s [@Around](http://www.eclipse.org/aspectj/doc/released/aspectj5rt-api/org/aspectj/lang/annotation/Around.html) annotation (see [ProfileAspect.java](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/java/fr/xebia/management/statistics/ProfileAspect.java)).

### Expression language based identifiers ###

The smoothest technique we found to allow developer to build a name of the profiler composed with method parameters and invoked object attributes was to exposed these in an expression language. This approach was consistent with the increasing role of expression languages in java frameworks (see Spring Expression Language, etc).
An other approach would have been to follow the [Inspektr](https://github.com/dima767/inspektr) way and ask developers to develop one "name builder class" per profiled method.

The performance impact of the expression language evaluation at each invocation proved to be negligible trying with both [Apache Commons JEXL](http://commons.apache.org/jexl/) and [Spring Expression Language](http://static.springsource.org/spring/docs/3.0.5.RELEASE/spring-framework-reference/html/ch07.html).

The `@Profiled` annotation uses [Spring Expression Language](http://static.springsource.org/spring/docs/3.0.0.M3/spring-framework-reference/html/ch07.html)'s [SpelExpressionParser](http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/expression/spel/standard/SpelExpressionParser.html) (see [ProfileAspect.java](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/java/fr/xebia/management/statistics/ProfileAspect.java)).

### XML Namespace based Spring configuration ###

Spring XML namespace based configuration is performed thanks to Spring's [BeanDefinitionParser](http://static.springsource.org/spring/docs/3.0.0.M3/spring-framework-reference/html/apbs04.html) (see [ProfileAspectDefinitionParser.java](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/java/fr/xebia/management/config/ProfileAspectDefinitionParser.java)).
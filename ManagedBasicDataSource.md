# Managed Jakarta Commons DBCP Basic `DataSource` #

Extension of the [Jakarta Commons DBCP](http://commons.apache.org/dbcp/) DataSource monitorable with a JMX MBean and configurable with a Spring Namespace.

It is not intended to monitor JDBC resource leaks and thus it does not monitor resource's `close()`.

## Spring Configuration ##

  * Spring namespace based configuration:
```
<beans ...
   xmlns:management="http://www.xebia.fr/schema/xebia-management-extras" 
   xsi:schemaLocation="
		http://www.xebia.fr/schema/xebia-management-extras http://www.xebia.fr/schema/management/xebia-management-extras.xsd">

   <!-- MBeanExporter is in charge of registering the DBCP DataSource MBean -->
   <context:mbean-export />

   <management:dbcp-datasource id="myDataSource">
      <management:url value="jdbc:h2:mem:dbcp-test" />
      <management:driver-class-name value="org.h2.Driver" />
      <management:username value="sa" />
      <management:password value="" />
      <management:max-active value="10" />
      <!-- ... any other useful configuration param -->
   </management:dbcp-datasource>

</beans>
```
  * Spring raw configuration
```
<beans ...>
   <bean id="myDataSource" class="fr.xebia.springframework.jdbc.ManagedBasicDataSource">
      <property name="url" value="jdbc:h2:mem:dbcp-test" />
      <property name="driverClassName" value="org.h2.Driver" />
      <property name="username" value="sa" />
      <property name="password" value="" />
      <property name="maxActive" value="10" />
      <!-- ... any other useful configuration param -->
   </bean>
   ...
   <!-- mbean exporter will register the datasource mbean -->
   <context:mbean-export server="..." />
</beans>
```

## Configuration attributes ##

| **`Attribute`** | **Type** | **Req'd** | **Default Value** |  **Description**  |
|:----------------|:---------|:----------|:------------------|:------------------|
| **`url`**       | string   | true      |                   | Jdbc 'url'.       |
| **`username`**  | string   | true      |                   | Connection 'username' to be passed to our JDBC driver to establish a connection. |
| **`password`**  | string   | true      |                   | Connection 'password' to be passed to our JDBC driver to establish a connection. |
| **`driver-class-name`** | string   | true      |                   | Jdbc driver class name. |
| **`default-auto-commit`** | boolean  | false     | 'true'            | Default auto-commit state of connections returned by this datasource. |
| **`default-read-only`** | boolean  | false     | 'false'           | 'defaultReadonly' property. |
| **`default-transaction-isolation`** | null     | false     | Nothing specified | Default transaction isolation state for returned connections. |
| **`default-catalog`** | string   | false     |                   | Default catalog.  |
| **`max-active`** | int      | false     | 8                 | Maximum number of active connections that can be allocated at the same time. Use a negative value for no limit. |
| **`max-idle`**  | int      | false     | 8                 | Maximum number of connections that can remain idle in the pool. |
| **`min-idle`**  | int      | false     | 0                 | Minimum number of idle connections in the pool. |
| **`initial-size`** | int      | false     | 0                 | Initial number of connections that are created when the pool is started. |
| **`max-wait`**  | long     | false     | -1                | Maximum number of milliseconds that the pool will wait (when there are no available connections) for a connection to be returned before throwing an exception, or <= 0 to wait indefinitely. |
| **`pool-prepared-statements`** | boolean  | false     | 'false'           | Prepared statement pooling for this pool. When this property is set to 'true' both PreparedStatements and CallableStatements are pooled. |
| **`max-open-prepared-statements`** | int      | false     | -1                | Maximum number of open statements that can be allocated from the statement pool at the same time, or non-positive for no limit.  Since  a connection usually only uses one or two statements at a time, this is mostly used to help detect resource leaks. |
| **`test-on-borrow`** | boolean  | false     | 'true'            | Indication of whether objects will be validated before being borrowed from the pool.  If the object fails to validate, it will be dropped from the pool, and we will attempt to borrow another. |
| **`test-on-return`** | boolean  | false     | 'false'           | Indication of whether objects will be validated before being returned to the pool. |
| **`time-between-eviction-runs-millis`** | long     | false     | -1                | Number of milliseconds to sleep between runs of the idle object evictor thread.  When non-positive, no idle object evictor thread will be run. |
| **`num-tests-per-eviction-run`** | int      | false     | 3                 | Number of objects to examine during each run of the idle object evictor thread (if any). |
| **`min-evictable-idle-time-millis`** | long     | false     | '1800000' (30 mins) | The minimum amount of time an object may sit idle in the pool before it is eligable for eviction by the idle object evictor (if any). |
| **`test-while-idle`** | boolean  | false     | 'false'           | The indication of whether objects will be validated by the idle object evictor (if any).  If an object fails to validate, it will be dropped from the pool. |
| **`validation-query`** | string   | false     | 'null'            | The SQL query that will be used to validate connections from this pool before returning them to the caller.  If specified, this query <strong>MUST</strong> be an SQL SELECT statement that returns at least one row. |
| **`validation-query-timeout`** | int      | false     | -1                | Timeout in seconds before connection validation queries fail. |
| **`access-to-underlying-connection-allowed`** | boolean  | false     | 'false'           | Controls access to the underlying connection. |
| **`remove-abandoned`** | boolean  | false     | 'false'           | Whether or not a connection is considered abandoned and eligible for removal if it has been idle longer than the removeAbandonedTimeout. |
| **`remove-abandoned-timeout`** | int      | false     | '300' seconds     | Timeout in seconds before an abandoned connection can be removed. |
| **`log-abandoned`** | boolean  | false     | 'false'           | Determines whether or not to log stack traces for application code which abandoned a Statement or Connection. |
| **`connection-properties`** | string   | false     |                   | The connection properties that will be sent to our JDBC driver when establishing new connections.  <strong>NOTE</strong> - The "user" and "password" properties will be passed explicitly, so they do not need to be included here. |

## JMX MBean Object Name ##

  * JMX Object Name: `"javax.sql:type=DataSource,name=${spring-bean-name}"` where `"${spring-bean-name}"` is the name of the spring object

## DBCP DataSource Collected Metrics / JMX MBeans attributes ##

Here are the metrics monitored by the Managed DBCP DataSource:

| **Name** | **Indicator Type** | **Description** |
|:---------|:-------------------|:----------------|
| `NumActive` | Dynamic            |  Number of currently active connections  |
| `NumIdle` | Dynamic            |  Number of currently idle connections  |
| `MaxActive` | Configuration parameter |  Maximum number of active connections that can be allocated at the same time. Use a negative value for no limit (read/write attribute) |
| `MaxIdle` | Configuration parameter |  Maximum number of connections that can remain idle in the pool (read/write attribute) |
| `MinIdle` | Configuration parameter |  Minimum number of idle connections in the pool (read/write attribute) |
| `MaxWait` | Configuration parameter |   Maximum number of milliseconds that the pool will wait (when there are no available connections) for a connection to be returned before throwing an exception, or <= 0 to wait indefinitely (read/write attribute) |
| `username` | Configuration parameter |  Connection 'username' to be passed to our JDBC driver to establish a connection (read only attribute) |
| `url`    | Configuration parameter | Jdbc 'url' (read only attribute) |
| `defaultAutoCommit` | Configuration parameter | Default auto-commit state of connections returned by this datasource (read only attribute) |

Trends Up indicators are always increasing counters. The monitoring system is expected to convert these metrics into "per minute" metrics (e.g. `TaskCount` should be exposed as `TaskCountPerMinute`).

## Implementation details ##

  * The `fr.xebia.springframework.jdbc.ManagedBasicDataSource` is a subclass of Jakarta Commons DBCP `org.apache.commons.dbcp.BasicDataSource`.


## JSP / VisualVM / Hyperic HQ rendering ##

  * JSP monitoring page : [(source)](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/webapp/jmx/dbcp.jsp.jsp),
> > <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-jsp-dbcp.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-jsp-dbcp.png' height='200' /></a>
  * Visual VM / JConsole view:
> > <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-dbcp.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-dbcp.png' height='200' /></a>

  * Hyperic plugin: [tomcat-extras-dbcp-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-dbcp-plugin.xml), see [XebiaManagementExtrasHypericPlugins](XebiaManagementExtrasHypericPlugins.md)

## How to integrate the xebia-management-extras library in your project ? ##

See [XebiaManagementExtrasInstallation](XebiaManagementExtrasInstallation.md).

## Implementation decisions and details ##

### MBean exposition technique ###

This JMX enabled version of DBCP data source is a subclass a the [BasicDataSource](http://commons.apache.org/dbcp/apidocs/org/apache/commons/dbcp/BasicDataSource.html) implementation instead of wrapping it. The benefits are:
  * it does not hide `BasicDataSource`,
  * the code is very compact and is not coupled to the version of JDBC API and of DBCP.

The [ManagedBasicDataSource](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/tags/xebia-management-extras-1.0.0/src/main/java/fr/xebia/springframework/jdbc/ManagedBasicDataSource.java) simply does the following:
**implement [ManagedBasicDataSourceMBean](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/tags/xebia-management-extras-1.0.0/src/main/java/fr/xebia/springframework/jdbc/ManagedBasicDataSourceMBean.java) interface which is the `xxxMBean` interface that declares all the exposed JMX attributes and operations,** implement Spring [SelfNaming](http://static.springsource.org/spring/docs/3.0.x/javadoc-api/org/springframework/jmx/export/naming/SelfNaming.html) to build a meaningful object name using the Spring bean name,

Please note that this JMX exposition of DBCP did not rely on any Spring JMX annotation (`@ManagedResource`, `@ManagedAttribute`, etc). The benefit of using Spring annotations would have been to expose the attributes descriptions in JMX tools like JConsole / VisualVM. However, the drawback would have been to add a lot of code and to use the dirty trick of overriding the exposed getters/setters/methods just to annotate them. This trick gets even more _dirty_ when methods are `final` and require to be slightly renamed (e.g. Spring JMS' `DefaultMessageListenerContainer.getActiveConsumerCount()`).

### XML Namespace based Spring configuration ###

Spring XML namespace based configuration is performed thanks to Spring's [BeanDefinitionParser](http://static.springsource.org/spring/docs/3.0.0.M3/spring-framework-reference/html/apbs04.html) (see [ManagedBasicDataSourceBeanDefinitionParser.java](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/tags/xebia-management-extras-1.0.0/src/main/java/fr/xebia/management/config/ManagedBasicDataSourceBeanDefinitionParser.java)).

As the number of configurable attributes was very important on the `BasicDataSource`, we decided to expose them as optional sub elements of `<management:dbcp-datasource id="...">` rather than as attributes.

To allow developers to configure numeric attributes with Spring property placeholder (e.g. `<management:max-active value="${jdbc.max-active}" />`) or Spring Expression Language (e.g. `<management:max-active value="#{ T(java.lang.Integer).parseInt(systemProperties['tomcat.thread-pool.size']) / 2 }" />`), we declared numeric attributes as parameterizables is the same way as CXF does. Integers are parameterized integers (`ParameterizedInt`) : integer or `${my-var}` or `#{ my expression}`. It is the same for longs and booleans. This feature is enabled in version 1.1.0.


## Todo list ##

Add a JMX operation to test the datasource.
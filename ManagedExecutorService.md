# Managed Executor Service / Thread Pool Executor #

The `<management:executor-service />` is a JMX MBean enabled implementation of a `java.util.concurrent`'s `ExecutorService` / `ThreadPoolExecutor` configurable via a Spring namespace.

It is very similar to Spring Framework's [&lt;task:executor /&gt;](http://static.springsource.org/spring/docs/3.0.x/spring-framework-reference/html/scheduling.html#scheduling-task-namespace-executor); the main difference is that the `<management:executor-service />` exposes its metrics via a JMX MBean.

## Spring Configuration ##

**Spring namespace based configuration:
```
<beans ...
   xmlns:management="http://www.xebia.fr/schema/xebia-management-extras" 
   xsi:schemaLocation="
		http://www.xebia.fr/schema/xebia-management-extras http://www.xebia.fr/schema/management/xebia-management-extras.xsd">

   <!-- MBeanExporter is in charge of registering the ExecutorService MBean -->
   <context:mbean-export />

   <management:executor-service 
       id="my-executor" 
       pool-size="1-10" 
       queue-capacity="5"
       keep-alive="5"
       rejection-policy="ABORT" />

</beans>
```
  * Spring raw configuration
```
<beans ...>
   <bean id="my-executor" class="fr.xebia.springframework.concurrent.ThreadPoolExecutorFactory">
      <property name="poolSize" value="1-10" />
      <property name="queueCapacity" value="5" />
      <property name="keepAliveTimeInSeconds" value="5" />
      <property name="rejectedExecutionHandlerClass" value="java.util.concurrent.ThreadPoolExecutor$AbortPolicy" />
   </bean>
   ...
   <!-- mbean exporter will register the thread pool executor mbean -->
   <context:mbean-export server="..." />
</beans>
```**

## Configuration attributes ##

| **Name** | **Req'd** | **Default Value** |  **Description** |
|:---------|:----------|:------------------|:-----------------|
| `id`     | Yes       | N/A               | Id of the bean, used to build the object name as `"java.util.concurrent:type=ThreadPoolExecutor,name=${id}"` |
| `pool-size` | No        | 1-∞ (Integer.MAX\_VALUE) | Size of the thread pool. Expressed as a single number for both min and max sizes or as a range like `1-5` expressing the min and max number of threads |
| `queue-capacity` | No        | ∞ (Integer.MAX\_VALUE) | Size of the blocking queue at the entrance of the thread pool |
| `keep-alive` | No        | 0                 | Timeout in seconds after which the created threads exceeding the core pool size are destroyed |
| `rejection-policy` |  No       | `ABORT`           | Strategy when all the threads are busy and the queue is full. Possible values are: <br /> - `ABORT`: a `RejectedExecutionException` is throwned, <br /> - `CALLER_RUNS`: the offered `Runnable` is executed in the calling thread,<br /> - `DISCARD`: the offered `Runnable` is discarded, <br /> - `DISCARD`: the oldest `Runnable` is discarded.|

## JMX MBean Object Name ##

  * JMX Object Name: `"java.util.concurrent:type=ThreadPoolExecutor,name=${spring-bean-name}"` where `"${spring-bean-name}"` is the name of the spring object

## ExecutorService Collected Metrics / JMX MBeans attributes ##

Here are the metrics monitored by the Managed ExecutorService / ThreadPool:

| **Name** | **Indicator Type** | **Description** |
|:---------|:-------------------|:----------------|
| `ActiveCount` | Dynamic            |  Number of currently active threads  |
| `QueueRemainingCapacity` | Dynamic            |  Number of slots available on the executor queue |
| `RejectedExecutionCount` | Trends Up          |  Number of slow rejected tasks |
| `TaskCount` | Trends Up          | Number of tasks that have been submitted to this executor, including the rejected ones |
| `CompletedTaskCount` | Trends Up          | Approximate total number of tasks that have completed execution (since 1.1.2) |
| `LargestPoolSize` | N/A                | Largest number of threads that have ever simultaneously been in the pool (since 1.1.2) |

Trends Up indicators are always increasing counters. The monitoring system is expected to convert these metrics into "per minute" metrics (e.g. `TaskCount` should be exposed as `TaskCountPerMinute`).

## ExecutorService runtime JMX based configuration attributes ##

| **Name** | **Access** | **Description** |
|:---------|:-----------|:----------------|
| `CorePoolSize` | read/write | Sets the core number of threads. If the new value is smaller than the current value, excess existing threads will be terminated when they next become idle. If larger, new threads will, if needed, be started to execute any queued tasks (since 1.1.2) |
| `MaximumPoolSize` | read/write | Sets the maximum allowed number of threads. If the new value is smaller than the current value, excess existing threads will be terminated when they next become idle (since 1.1.2) |

## Implementation details ##

  * The `ThreadPoolExecutorFactory` is a Spring factory of a subclass of `java.util.concurrent.ThreadPoolExecutor`,


## JSP / VisualVM / Hyperic HQ rendering ##

  * JSP monitoring page : [(source)](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/webapp/jmx/thread-pool-executors.jsp),
> > <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-jsp-thread-pool-executor.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-jsp-thread-pool-executor.png' height='200' /></a>
  * Visual VM / JConsole view:
> > <a href='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-thread-pool-executor.png'><img src='http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/site/monitoring-visualvm-thread-pool-executor.png' height='200' /></a>

  * Hyperic plugin: [tomcat-extras-executorservice-plugin.xml](http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/src/main/hyperic/tomcat-extras-executorservice-plugin.xml), see [XebiaManagementExtrasHypericPlugins](XebiaManagementExtrasHypericPlugins.md)

## How to integrate the xebia-management-extras` library in your project ? ##

See [XebiaManagementExtrasInstallation](XebiaManagementExtrasInstallation.md).
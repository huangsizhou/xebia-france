# Version 1.2.3 and more recent #

Recent release notes are available at https://github.com/xebia-france/xebia-management-extras/releases

# Version 1.2.2 (2012/12/04) #

  * fix [Issue 18](https://code.google.com/p/xebia-france/issues/detail?id=18) "ManagedDefaultMessageListenerContainer does not pass bean name to superclass."
  * fix a `NullPointerException` in `ProfileAspect`

# Version 1.2.1 (2011/07/27) #

  * [ManagedJmsConnectionFactory](ManagedJmsConnectionFactory.md): Enhance display of the JMS leaks detector.

# Version 1.2.0 (2011/07/27) #

  * [ManagedJmsConnectionFactory](ManagedJmsConnectionFactory.md): Add leaks detector.

# Version 1.1.3 (2011/06/08) #

  * [ManagedJmsConnectionFactory](ManagedJmsConnectionFactory.md): Add JMX metrics
    * `ActiveConnectionCount`, `ActiveSessionCount`, `ActiveMessageConsumerCount` and `ActiveMessageProducerCount` to monitor the flow of messages,
    * `CloseConnectionCount`, `CloseMessageProducerCount`, `CloseMessageConsumerCount`, `CloseSessionCount` to help diagnosing leaks (comparing to `CreateXxxCount` indicator) and internally used to process `ActiveXxxCount`,
  * [ProfiledAnnotation](ProfiledAnnotation.md): Add "`maxActive`" and "`maxActiveExpression`" attributes to `@Profiled` annotation.


# Version 1.1.2 (2011/04/20) #

  * `<management:executor-service />` : expose JMX counter `CompletedTaskCount` and configuration attributes `MaximumPoolSize`, `CorePoolSize` and indicator `LargestPoolSize`

# Version 1.1.1 (2011/04/19) #

  * Fix [Issue 10 - attribute jmxDomain of spring config <management:profile-aspect ... /> is ignored](http://code.google.com/p/xebia-france/issues/detail?id=10)

# Version 1.1.0 (2011/03/03) #

  * Fix [Issue 8 - NullPointerException in &lt;management:executor-service /&gt; with mono valued "pool-size"](http://code.google.com/p/xebia-france/issues/detail?id=8&can=1)
  * Allow to use SpEL expressions in `<management:dbcp-datasource .../>` configuration attributes

# Version 1.0.0 (2010/12/07) #

First release
b= Introduction =

Add your content here.

# Prerequisites #

  * Subversion
  * 1.6 JDK (with jvisualvm)
  * Maven
  * Eclipse with WTP and Tomcat server configured
  * Hyperic HQ

# Setup #

See [JmxTrainingSetup](JmxTrainingSetup.md)

# The Pounder #

  * Start the pounder to generate load on the Tomcat Server
    * In Eclipse, open `fr.xebia.demo.ws.employee.EmployeeServicePounder`
    * This multi threaded pounder will call both
      * http://localhost:8080/jmx-training/employee.jsp?id=1 with varying `id` : a REST style service to retrieve the employee
      * http://localhost:8080/jmx-training/services/employeeService the SOAP based Employee Service
    * Run this `EmployeeServicePounder`
    * Console output will look like
```
[19:12:20   27 req/s]	*--**-*--*##---###-#-*--------
[19:12:22   24 req/s]	-#-#-##-##---**--x-##---#-*-#-
[19:12:23   24 req/s]	--x**--#**--#---**####--#-*--*
[19:12:24   27 req/s]	-##-*#----#**##----*#----#-#-*
[19:12:25   24 req/s]	##--#*-##--**#--#---#------#--
[19:12:26   25 req/s]	---x--#x#--*-###*---#**--#--*#
```
    * Legend :
      * `-` success
      * `#` failure of the REST style `employee.jsp`
      * `x` EmployeeNotFoundException raised by the SOAP service
      * `*` failure of the SOAP service

# Lab 1 : Immediate Diagnosis, the quick win #

You are responsible of the Employee Web Service that is accused by the Quality Of Service QOS team to raise many errors.

Your first task is to follow the error rate of the REST style service.
The rules are :
  * The only tool you have is to query Tomcat Out Of The Box MBeans (ie the "Catalina" MBeans)
  * VisualVM can connect to the Tomcat Server

# Lab 2 : Reporting for the QOS team #

Well done, you can really quantify the error rate of the REST service in the QOS Crisis Meeting.

Your second task is to provide to the QOS team a way to look at these metrics. They want a report, even if it is basic.
The rules are :
  * The QOS team cannot use visualvm, they are not hardcore java guys like you are
  * The QOS team like to use web browsers
  * The Security team allows you to deploy troubleshooting readonly pages on the live application with the following recommandations :
    * it is better to have unpredictable URLs
    * it is better to have password protected pages

Few tips :
  * A nice Tomcat JMX troubleshooting page is available [here](http://xebia-france.googlecode.com/svn/jmx/jmx-training/trunk/src/main/webapp/tools/jmx/tomcat.jsp) and can be a nice template to start working
  * A nice web application called [Secure Password Generator](http://www.pctools.com/guides/password/) generates unpredictables word that could be cool in URLs
  * For Basic Authentication, the application is Already using Spring Security, a `ROLE_ADMIN` is already used and the account `admin` with password `admin` is already known by the QOS team. All this cool stuff is configured in [src/main/resources/applicationContext.xml](http://xebia-france.googlecode.com/svn/jmx/jmx-training/trunk/src/main/resources/applicationContext.xml)
```
   <sec:http auto-config='true'>
      <sec:intercept-url pattern="/services/**" access="ROLE_USER" />
      <sec:http-basic />
      <sec:anonymous />
   </sec:http>

   <sec:authentication-provider>
      <sec:user-service>
         <sec:user name="admin" password="admin" authorities="ROLE_USER, ROLE_ADMIN" />
         <sec:user name="user" password="user" authorities="ROLE_USER" />
      </sec:user-service>
   </sec:authentication-provider>
```

# Lab 3 : Don't shoot the messenger ! It's not me, it's the backend service #

Reporting is done, you can leave the crisis meetings and start working !

The Employee Service may be innocent. It the `BuggyService` that seems to be guilty. Everybody knows in the company that this service is terrible and that its connector is not reliable.

Your task is :
  1. To instrument this `BuggyService` to count errors
  1. To enhance the error rate report you gave to the QOS team with the new metrics.

# Lab 4 : Lower the pressure on the AsyncService #

Well done with the `BuggyService`. You are no longer accountable for their problems, the QOS team has the situation under control thanks to your metrics.

This time, it is the `AsyncService` that has problems. You are said to put too much pressure on it. They initially asked you to limit to 5 requests per seconds ; you transformed this into a maximum of 4 concurrent requests. However, you are not sure it will be enough to fulfill your needs. Time has come to be smart with `util.concurrent.ExecutorService`

Your task :
**Get metrics on the number of concurrent requests and the number of waiting requests** enhance the QOS team report to add this figures

Tips :
  * A smart guy in your team developed a Spring JMX enabled implementation of `ExecutorService`; it can be used like this:
```
   <bean id="executorService" class="fr.xebia.springframework.concurrent.ThreadPoolExecutorFactory">
      <property name="mbeanExporter" ref="mbeanExporter" />
      <property name="nbThreads" value="10" />
      <property name="queueCapacity" value="15" />
   </bean>
```

# Lab 5 : Database panic ! Too many connections, to many reads, not enough caching #

The DBA is not happy. There are too many open connections on the database and your application is querying too much ; the say you don't cache the data.

**Instrument the dataSource and the executorService. We new :
  * DataSource : numActive
  * EhCache : cache hit ratio ; the QOS can divide two numbers.** Enhance hte QOS team report

Tips :
**EHCache provides JMX intrumentation, you found on the internet this sample :
```
   <bean id="managementService" class="net.sf.ehcache.management.ManagementService" init-method="init">
      <constructor-arg ref="mbeanServer" />
      <constructor-arg ref="cacheManager" />
      <constructor-arg index="2" value="true" />
      <constructor-arg index="3" value="true" />
      <constructor-arg index="4" value="true" />
      <constructor-arg index="5" value="true" />
   </bean>
```**

# Lab 6 : Keep your service under control, monitor CXF #

Monitoring error rate on CXF endpoint would be a nice way to monitor the Employee Service.

Please expose these metrics t othe QOS team.

# Lab 7 : Limit load on BuggyService #

You are asked to limit the number of concurrent invocations on BuggyService to 5.

Tip :
  * create a wrapper implementation that rely on a semaphore

# Lab 8 : Let's put all this under Hyperic HQ #

**Checkout this clean tomcat setup http://xebia-france.googlecode.com/svn/tomcat/tomcat-setup/trunk and fix setenv.bat|sh under /bin**

**Deploy your jmx-demo WAR file  (built with `mvn package`)**

**Start the Tomcat Server**

**Create a folder `hq-plugins` under Hyperic root folder, next to the folder `agent-4.1.2` and `server-4.1.2` (by default on Windows under `C:\Program Files\Hyperic HQ 4.1.2`)** Copy Hyperic plugins located under [/src/main/hyperic/](http://xebia-france.googlecode.com/svn/jmx/jmx-training/trunk/src/main/hyperic/) to `hq-plugins`
**Restart Hyperic Agent and Server** Search in Hyperic console for autodiscovery

# Lab 9 : Create your own Hyperic Plugin #

Create an Hyperic Plugin for BuggyService
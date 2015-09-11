# Setup #

  * Checkout http://xebia-france.googlecode.com/svn/jmx/jmx-training/trunk/
  * Run "mvn eclipse:eclipse"
  * Start Eclipse IDE
    * Import "jmx-training" project
    * In Servers view, add jmx-traing project to the "Tomcat v6.0 Server at localhost"
    * Open "Debug Configuration...", select the "Tomcat v6.0 Server at localhost", on tab "Arguments", field "VM Arguments", add `-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=6969 -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false`
> > > ![http://xebia-france.googlecode.com/svn/jmx/jmx-training/trunk/src/site/img/eclipse-tomcat-vm-args-enable-jmx.png](http://xebia-france.googlecode.com/svn/jmx/jmx-training/trunk/src/site/img/eclipse-tomcat-vm-args-enable-jmx.png)
    * Start debugging "Tomcat v6.0 Server at localhost"
    * Check in the "console" view that no error occured
  * Open a web browser and test the following url :
    * http://localhost:8080/jmx-training/tools/jmx/mbeans.jsp : list all the mbeans of the Tomcat Server
    * http://localhost:8080/jmx-training/tools/parameters.jsp : a status page
    * http://localhost:8080/jmx-training/services with username=admin, password=admin : the CXF services page
  * Start `visualvm` (located under `JDK_HOME\bin` )
    * Open the "tools\plugins" window and install the VisualVM-MBeans and VisualGC plugins
    * Restart if necessary
    * In "Application\local", open the "Tomcat (pid xxx)" application
    * On "Tomcat (pid xxx)" application, open the MBeans tab
    * Check that the "Catalina" entry exists in the MBeans tree view
> > > ![http://xebia-france.googlecode.com/svn/jmx/jmx-training/trunk/src/site/img/visualVM-mbeans-tomcat-jmx-training.png](http://xebia-france.googlecode.com/svn/jmx/jmx-training/trunk/src/site/img/visualVM-mbeans-tomcat-jmx-training.png)
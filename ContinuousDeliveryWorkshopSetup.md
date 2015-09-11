# How to install the Workshop Installation #

  * Checkout https://xebia-france.googlecode.com/svn/cloudcomputing/xebia-cloudcomputing-extras/trunk
```
svn checkout https://xebia-france.googlecode.com/svn/cloudcomputing/xebia-cloudcomputing-extras/trunk workshop
```
  * Configure your credentials in src/main/resources/AwsCredentials.properties (see the [template file](http://xebia-france.googlecode.com/svn/cloudcomputing/xebia-cloudcomputing-extras/trunk/src/main/resources/AwsCredentials.properties.template))
```
cd workshop; cp /path/to/aws-credentials.txt src/main/resources/AwsCredentials.properties
```
  * Put the "continuous-delivery-workshop.pem" file in your classpath
```
cp /path/to/continuous-delivery-workshop.pem src/main/resources/continuous-delivery-workshop.pem
```
  * To create the infrastructure, run `fr.xebia.workshop.continuousdelivery.ContinuousDeliveryInfrastructureCreator`
```
mvn clean package exec:java -Dexec.mainClass="fr.xebia.workshop.continuousdelivery.ContinuousDeliveryInfrastructureCreator"
```
  * This scripts creates :
    * 1 Nexus server (common for all teams)
      * Amazon EC2 Name : "nexus"
      * URL: http://nexus.xebia-tech-event.info:8081/nexus/
    * 1 Jenkins/Rundeck/Deployit server per team
      * Amazon EC2 Names : `jenkins-${team-identifier`}
    * 1 Tomcat dev server per team
      * Amazon EC2 Name: `tomcat-${team-identifier}-dev-1`
    * 2 Tomcat valid servers per team
      * Amazon EC2 Names: `tomcat-${team-identifier}-valid-1` & `tomcat-${team-identifier}-valid-2`
    * 1 Petclinic project per team under Xebia Training's Github account (https://github.com/xebia-france-training/`xebia-petclinic-${team-identifier`})
    * A job under each team's Jenkins server to build the team's Petclinic project
  * Your infrastructure documentation (labs wiki pages) has been created in "/tmp/continuous-delivery/". If you want to regenerate it, run `fr.xebia.workshop.continuousdelivery.ContinuousDeliveryInfrastructureDocsGenerator`
```
mvn clean package exec:java -Dexec.mainClass="fr.xebia.workshop.continuousdelivery.ContinuousDeliveryInfrastructureDocsGenerator"
```


# Specifications #

## Nexus ##

  * Installed packages: JDK
  * Nexus 1.9.2.2 (with Rundeck plugin)
  * Logs: `/opt/nexus/nexus-oss-webapp-1.9.2.2/logs/`
  * [cloud-config-amzn-linux-nexus.txt](http://xebia-france.googlecode.com/svn/cloudcomputing/xebia-cloudcomputing-extras/trunk/src/main/resources/fr/xebia/workshop/continuousdelivery/cloud-config-amzn-linux-nexus.txt)

## Jenkins / Rundeck / Deployit servers ##

  * Installed package: JDK, Jenkins (with plugins Batch Task, Git, SSH and Rundeck), Rundeck, Maven, Git
    * [Batch Task Plugin for Jenkins](https://wiki.jenkins-ci.org/display/JENKINS/Batch+Task+Plugin)
    * [Git Plugin for Jenkins](https://wiki.jenkins-ci.org/display/JENKINS/Git+Plugin)
    * [Github Plugin for Jenkins](https://wiki.jenkins-ci.org/display/JENKINS/Github+Plugin)
    * [Rundeck Plugin for Jenkins](https://wiki.jenkins-ci.org/display/JENKINS/Rundeck+Plugin)
    * [SSH Plugin for Jenkins](https://wiki.jenkins-ci.org/display/JENKINS/SSH+Plugin)
  * Configuration:
    * "`continuous-delivery-workshop.pem`" in both `${jenkins-user-home}/.ssh` and `${rundeck-user-home}/.ssh`
    * maven settings.xml with Nexus repos url, login &password
  * [cloud-config-amzn-linux-jenkins-rundeck.txt](http://xebia-france.googlecode.com/svn/cloudcomputing/xebia-cloudcomputing-extras/trunk/src/main/resources/fr/xebia/workshop/continuousdelivery/cloud-config-amzn-linux-jenkins-rundeck.txt)
  * Petclinic job configuration:
    * [Jenkins remote access API](https://wiki.jenkins-ci.org/display/JENKINS/Remote+access+API)
    * [petclinic-jenkins-job-config.xml.fmt](http://xebia-france.googlecode.com/svn/cloudcomputing/xebia-cloudcomputing-extras/trunk/src/main/resources/fr/xebia/workshop/continuousdelivery/petclinic-jenkins-job-config.xml.fmt)


## Tomcat servers ##

  * Installed package: jdk
  * Additional anstalled software:
  * Tomcat 6, not installed by RPM because the Tomcat 6 RPM start/stop script (`/usr/sbin/tomcat6`) can not be invoked directly by tomcat user. `/usr/sbin/tomcat6` script must be invoked by linux tomcat6 service (`service tomcat6 start|stop`.
  * Configuration:
    * allow ssh connection as "`tomcat`" user with "`continuous-delivery-workshop.pem`"
    * grant access to tomcat-manager webapp to tomcat/tomcat, manager/manager and admin/admin
    * add "`ec2-user`" to group `tomcat`"
  * CloudInit: [cloud-config-amzn-linux-tomcat.txt](https://xebia-france.googlecode.com/svn/cloudcomputing/xebia-cloudcomputing-extras/trunk/src/main/resources/fr/xebia/workshop/continuousdelivery/cloud-config-amzn-linux-tomcat.txt)

# Documentation #

  * Keynote presentation : `workshop-continuous-delivery.key` in https://www.dropbox.com/home/java-infra-as-code-with-amazon-aws#/
  * Omnigraffle diagrams:  `automated-deployment-workshop.graffle` in https://www.dropbox.com/home/java-infra-as-code-with-amazon-aws#/

# Diagrams #


## Per Team Infrastructure ##

<img width='400' src='http://xebia-france.googlecode.com/svn/wiki/cont-delivery-img/per-team-infrastructure.png' />

## Global Lab Infrastructure ##

<img width='400' src='http://xebia-france.googlecode.com/svn/wiki/cont-delivery-img/global-infrastructure.png' />
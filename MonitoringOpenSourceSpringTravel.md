

# Introduction #

This article aims to deploy xebia-spring-travel application on Amazon environment.

**Observation:** images below may be not synchronized with the steps. In this case follow the step, not the image.

# Access the AWS Management Console #

Connect to Amazon AWS Management Console with your credentials (sent in the email "Xebia France Amazon EC2 Credentials").

The IAM User sign-in URL is : https://xebia-france.signin.aws.amazon.com/console

# Create Database instance #

> Create an MySQL database using AWS Relational Database Service (RDS)

  1. Select the RDS tab
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-1.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-1.png)
  1. Select the region
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-select-eu-west-region.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-select-eu-west-region.png)
  1. Click on "Launch DB Instance" and select MYSQL
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-2.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-2.png)
  1. Configure the database using the values below:
```
 Database Name: xebiaspringtravel
 DB Instance Identifier: admin
 Master User Password: admin
```
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-3.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-3.png)
  1. Configure the instance using the values below:
```
 Database Name: xebiaspringtravel
```
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-4.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-4.png)
  1. Configure the backup
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-5.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-5.png)
  1. Launch DB Instance
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-6.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-6.png)
  1. Note the DB instance hostname (endpoint)
> > <img src='http://www.clker.com/cliparts/d/b/d/3/1194998844557242824messagebox_warning.svg.med.png' width='20' /> The database will be available in a few minutes.
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-8.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-8.png)


# Create Tomcat instances #

## Deploy xebia-spring-travel-antifraud Instance ##

  1. Connect to AWS console
  1. On "EC2" tab, select your region (e.g. "EU West (Ireland)")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-select-eu-west-region.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-select-eu-west-region.png)
  1. On "EC2 Dashboard", click on "Launch new instance"
  1. Select your AMI (e.g. "Basic 32-bit Amazon Linux AMI 2011.02.1 Beta")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-1.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-1.png)
  1. Select the instance size (e.g. "Micro")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-2-instance-details.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-2-instance-details.png)
  1. Copy and paste the script YAML below into the User Date field:
```
#cloud-config
 
# Install Java 6 and Tomcat 6
packages:
- java-1.6.0-openjdk-devel
- tomcat6
- tomcat6-admin-webapps
- tomcat6-webapps
 
# Deploying spring-travel
runcmd:
- [ sh, -xc, "echo $(date) ': cloudinit runcmd begin'" ]
- [ wget, "http://xebia-france.googlecode.com/svn/repository/maven2/fr/xebia/demo/travel/xebia-spring-travel-antifraud/1.0.1/xebia-spring-travel-antifraud-1.0.1.war" ]
- [ cp, xebia-spring-travel-antifraud-1.0.1.war, /usr/share/tomcat6/webapps ]
- [ wget, "http://archive.apache.org/dist/tomcat/tomcat-6/v6.0.29/bin/extras/catalina-jmx-remote.jar" ]
- [ mv, catalina-jmx-remote.jar, /usr/share/tomcat6/lib ]
- [ wget, "http://xebia-france.googlecode.com/svn/repository/maven2/fr/xebia/demo/travel/xebia-spring-travel-ecommerce/1.0.1/tomcat6-conf.zip" ]
- [ unzip, tomcat6-conf.zip ]
- [ mv, server.xml, /usr/share/tomcat6/conf ]
- [ mv, tomcat6.conf, /usr/share/tomcat6/conf ]
- [ service, tomcat6, start ]
- [ sh, -xc, "echo $(date) ': cloudinit runcmd end'" ]
```
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-3-instance-details.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-3-instance-details.png)
  1. Type the name of your server (e.g. "antifraude-prod")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-4-instance-details.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-4-instance-details.png)
  1. Select a key-pair that will be used for SSH connection (e.g. "oss-monitoring.pem")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-5-key-pair.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-5-key-pair.png)
  1. Select the "accept-all" security group
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/ec2-security-group.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/ec2-security-group.png)
  1. Review the configuration and click on "Launch"
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-7-review.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-7-review.png)
  1. Repeat these steps to create a pre-prod instance (e.g. antifraud-preprod)


## Deploy xebia-spring-travel-ecommerce Instances ##

  1. Connect to AWS console
  1. On "EC2" tab, select your region (e.g. "EU West (Ireland)")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-select-eu-west-region.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-select-eu-west-region.png)
  1. On "EC2 Dashboard", click on "Launch new instance"
  1. Select your AMI (e.g. "Basic 32-bit Amazon Linux AMI 2011.02.1 Beta")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-1.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-1.png)
  1. Select the instance size (e.g. "Micro")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-2-instance-details.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-2-instance-details.png)
  1. Copy and paste the script YAML below into the User Date field:
```
#cloud-config

# Install Java 6 and Tomcat 6
packages:
- java-1.6.0-openjdk-devel
- tomcat6
- tomcat6-admin-webapps
- tomcat6-webapps

# Deploying spring-travel
runcmd:
- [ sh, -xc, "echo $(date) ': cloudinit runcmd begin'" ]
- [ wget, "http://xebia-france.googlecode.com/svn/repository/maven2/fr/xebia/demo/travel/xebia-spring-travel-ecommerce/1.0.1/xebia-spring-travel-ecommerce-1.0.1.war" ]
- [ mv, xebia-spring-travel-ecommerce-1.0.1.war, /usr/share/tomcat6/webapps ]
- [ wget, "http://archive.apache.org/dist/tomcat/tomcat-6/v6.0.29/bin/extras/catalina-jmx-remote.jar" ]
- [ mv, catalina-jmx-remote.jar, /usr/share/tomcat6/lib ]
- [ wget, "http://xebia-france.googlecode.com/svn/repository/maven2/fr/xebia/demo/travel/xebia-spring-travel-ecommerce/1.0.1/tomcat6-conf.zip" ]
- [ unzip, tomcat6-conf.zip ]
- [ mv, server.xml, /usr/share/tomcat6/conf ]
- [ mv, tomcat6.conf, /usr/share/tomcat6/conf ]
- [ service, tomcat6, start ]
- [ sh, -xc, "echo $(date) ': cloudinit runcmd end'" ]
```
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-3-instance-details.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-3-instance-details.png)
  1. Type the name of your server (e.g. "ecommerce-prod-1")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-4-instance-details.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-4-instance-details.png)
  1. Select a key-pair that will be used for SSH connection (e.g. "oss-monitoring.pem")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-5-key-pair.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-5-key-pair.png)
  1. Select the "accept-all" security group
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/ec2-security-group.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/ec2-security-group.png)
  1. Review the configuration and click on "Launch"
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-7-review.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-7-review.png)
  1. Repeat these steps to create a second instance called ecommerce-prod-2 and one pre-prod instance (e.g. ecommerce-preprod)


> ## Connect to the Amazon Linux Server ##

  1. Get the SSH private key oss-monitoring.pem to connect to the servers
```
mkdir ~/.aws/
curl https://s3-eu-west-1.amazonaws.com/oss-monitoring/oss-monitoring.pem --output ~/.aws/oss-monitoring.pem
chmod 400 ~/.aws/oss-monitoring.pem
```
  1. Go to AWS EC2 tab
  1. Click on your server
  1. Drop down the "Instance Action" list
  1. Select "Connect"
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-connect-instance.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-connect-instance.png)
  1. Get the oss-monitoring.pem
  1. Change rights on oss-monitoring.pem: chmod 400 oss-monitoring.pem
  1. Copy paste the command line
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-connect-instance-2.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-connect-instance-2.png)
  1. SSH connect to the server as "ec2-user"
> > <pre>ssh -i oss-monitoring.pem ec2-user@ec2-46-137-58-156.eu-west-1.compute.amazonaws.com</pre>


> ## Configure the Datasource Connection ##

This section needs to be executed for _ecommerce_ instances

  1. Stop Tomcat
> > <pre>sudo /etc/init.d/tomcat6 stop</pre>
  1. Modify catalina.properties to add MySQL support to xebiaspringtravel
> > <pre>sudo vi /usr/share/tomcat6/conf/catalina.properties</pre>
> > Add the following lines :
```
# spring-travel properties
antifraudservice.baseurl=http://46.137.168.248:8080/xebia-spring-travel-antifraud-1.0.1/
#antifraudservice.baseurl=http://46.137.99.111:8080/xebia-spring-travel-antifraud-1.0.1/

jdbc.url=jdbc:mysql://xebiaspringtravel.cccb4ickfoh9.eu-west-1.rds.amazonaws.com:3306/xebiaspringtravel
jdbc.username=admin
jdbc.password=admin
jdbc.driver=com.mysql.jdbc.Driver

jpa.database=MYSQL
# jpa.hbm2ddlAuto = create-drop | create | update | validate 
jpa.hbm2ddlAuto=update
```
> > Note:
    1. Replace `xebiaspringtravel.cccb4ickfoh9.eu-west-1.rds.amazonaws.com` with your RDS endpoint.
    1. For preprod instance change the antifraudservice.baseurl property value to (to use antifraud-preprod) :
```
antifraudservice.baseurl=http://46.137.99.111:8080/xebia-spring-travel-antifraud-1.0.1/
```
  1. Start Tomcat
> > <pre>sudo /etc/init.d/tomcat6 start</pre>
  1. Repeat these steps for the ecommerce-prod-2 and ecommerce-preprod instances

# Create a Load Balancer #

  1. In the EC2 tab, click on the "Load Balancers" menu under "NETWORK & SECURITY"
  1. Click on "Create Load Balancer"
  1. Remove the default rule and create a new one with 8080 as the "EC2 Instance Port"
```
Load Balancer Name: elb-ecommerce-prod
```
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-1.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-1.png)
  1. Let the default options
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-2.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-2.png)
  1. Choose your instances (e. g. ecommerce-prod-1 and ecommerce-prod-2)
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-3.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-3.png)
  1. Create the load balancer
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-4.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-4.png)
  1. Select your load balancer in the list and change the stickiness configuration on clicking on the edit button under "Port Configuration".
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-5.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-5.png)
  1. Choose "Enable Load Balancer Generated Cookie Stickiness" and let the duration field blank
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-6.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-6.png)

# Set up Elastic IPs #

  1. On the Navigation bar click on NETWORK & SECURITY > Elastic IPs
> > ![http://xebia-france.googlecode.com/svn/wiki/oss-monitoring/AWS%20Navigation.png](http://xebia-france.googlecode.com/svn/wiki/oss-monitoring/AWS%20Navigation.png)
  1. Using the button Associate Address associate existing IPs to the instances. Follow the table below
| **Travel prod HTTP** |   | |
|:---------------------|:--|:|
| **Travel pre-prod HTTP** |   | |
| **Travel prod frontal 1** | 46.137.97.78 | prod1.travel.xebia-tech-event.info |
| **Travel prod frontal 2** | 46.137.98.212 | prod2.travel.xebia-tech-event.info |
| **Travel prod antifraude** | 46.137.168.248 | |
| **Travel pre-prod frontal 1** |  46.137.98.234 | test.travel.xebia-tech-event.info |
| **Travel pre-prod frontal 2** | PAS DE 2eme... Pas assez d'IP fixe (5 max)  |
| **Travel pre-prod antifraude** | 46.137.99.111  | |
> > ![http://xebia-france.googlecode.com/svn/wiki/oss-monitoring/AWS%20Elastic%20IPs.png](http://xebia-france.googlecode.com/svn/wiki/oss-monitoring/AWS%20Elastic%20IPs.png)
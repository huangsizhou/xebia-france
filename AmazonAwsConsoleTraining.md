

# Goal of the lab #

The goal of this lab is to deploy the Petclinic webapp on two Amazon EC2 Linux instances with one Amazon RDS MySQL database and a load balancer.

# Access the AWS Management Console #

Connect to Amazon AWS Management Console with your credentials (sent in the email "Xebia France Amazon EC2 Credentials").

The IAM User sign-in URL is : https://xebia-france.signin.aws.amazon.com/console

# Create Database instance #

> Amazon AWS propose Relational Database Service (RDS) to create ready to use database

  1. Select RDS
> ![http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-1.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-1.png)
  1. Select the region
> <img src='http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-select-eu-west-region.png' />
  1. Click on "Launch DB Instance" and select MYSQL
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-2.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-2.png)
  1. Configure Database - database
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-3.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-3.png)
  1. Configure Database - instance
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-4.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-4.png)
  1. Configure Database - backup
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-5.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-5.png)
  1. Launch DB Instance
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-6.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-6.png)
  1. Note the DB instance hostname (endpoint)
> > <img src='http://www.clker.com/cliparts/d/b/d/3/1194998844557242824messagebox_warning.svg.med.png' width='20' /> The database will be available in a few minutes.
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-8.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/rds-8.png)


# Create Tomcat instances #
## Create EC2 Instances ##

  1. Connect to AWS console
  1. On "EC2" tab, select your region (e.g. "EU West (Ireland)")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-select-eu-west-region.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-select-eu-west-region.png)
  1. On "EC2 Dashboard", click on "Launch new instance"
  1. Select your AMI (e.g. "Basic 32-bit Amazon Linux AMI 2011.02.1 Beta")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-1.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-1.png)
  1. Select the instance size (e.g. "Micro")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-2-instance-details.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-2-instance-details.png)
  1. Nothing to do
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-3-instance-details.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-3-instance-details.png)
  1. Type the name of your server (e.g. "petclinic-(my-trigram)-1")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-4-instance-details.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-4-instance-details.png)
  1. Select a key-pair that will be used for SSH connection (e.g. "xebia-france.pem")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-5-key-pair.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-5-key-pair.png)
  1. Select the "tomcat" security group. This security group allow SSH (22) and Tomcat (8080) communication
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/ec2-security-group.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/ec2-security-group.png)
  1. Review the configuration and click on "Launch"
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-7-review.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-7-review.png)
  1. Repeat steps 3 to 9 and create a second EC2 instance


> ## Connect to an Amazon Linux Server ##

  1. Go to AWS EC2 tab
  1. Click on your server
  1. Drop down the "Instance Action" list
  1. Select "Connect"
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-connect-instance.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-connect-instance.png)
  1. Get the xebia-france.pem
  1. Change rights on xebia-france.pem : chmod 400 xebia-france.pem
  1. Copy paste the command line
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-connect-instance-2.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-connect-instance-2.png)
  1. SSH connect to the server as "ec2-user"
> > <pre>ssh -i xebia-france.pem ec2-user@ec2-46-137-58-156.eu-west-1.compute.amazonaws.com</pre>


> ## Install JVM and Tomcat ##

> The Amazon AMI are RPM based Linux distribution so we're using YUM to install JVM and Tomcat

  1. Connect to an EC2 instance
> > <pre>ssh -i xebia-france.pem ec2-user@<hostname>.compute.amazonaws.com</pre>
  1. Install Java 6
> > <pre>sudo yum install java-1.6.0-openjdk</pre>
  1. Install Tomcat 6. Tomcat will be installed in /usr/share/tomcat6
> > <pre>sudo yum install tomcat6 tomcat6-admin-webapps tomcat6-webapps</pre>
  1. Run tomcat
> > <pre>sudo /etc/init.d/tomcat6 start</pre>
  1. Repeat this steps on the second instance


> ## Install Petclinic web app ##
  1. Stop Tomcat
> > <pre>sudo /etc/init.d/tomcat6 stop</pre>
  1. Download WAR on Amazon S3
> > <pre>wget http://xebia-france.googlecode.com/svn/repository/maven2/fr/xebia/demo/xebia-petclinic/1.0.2/xebia-petclinic-1.0.2.war</pre>
  1. Install the webapp
> > <pre>sudo cp xebia-petclinic-1.0.2.war /usr/share/tomcat6/webapps/petclinic.war</pre>
  1. Modify catalina.properties to add MySQL support to petclinic
> > <pre>sudo vi /usr/share/tomcat6/conf/catalina.properties</pre>
> > Add the following lines :
```
# PETCLINIC ENVIRONMENT VARIABLES
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://petclinic.cccb4ickfoh9.eu-west-1.rds.amazonaws.com:3306/petclinic
jdbc.username=petclinic
jdbc.password=petclinic

# Properties that control the population of schema and data for a new data source
jdbc.initLocation=classpath:db/mysql/initDB.txt
jdbc.dataLocation=classpath:db/mysql/populateDB.txt

# Property that determines which Hibernate dialect to use
# (only applied with "applicationContext-hibernate.xml")
hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Property that determines which database to use with an AbstractJpaVendorAdapter
jpa.database=MYSQL
```


> Note: replace `petclinic.cccb4ickfoh9.eu-west-1.rds.amazonaws.com` by your RDS endpoint.

  1. Start Tomcat
> > <pre>sudo /etc/init.d/tomcat6 start</pre>
  1. Repeat on each EC2 Tomcat instance

# Create a Load Balancer #

  1. In the EC2 tab, click on the "Load Balancers" menu under "NETWORK & SECURITY"
  1. Click on "Create Load Balancer"
  1. Remove the default rule and create a new one with 8080 as the "EC2 Instance Port"
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-1.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-1.png)
  1. Let the default options
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-2.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-2.png)
  1. Choose your instances
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-3.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-3.png)
  1. Create the load balancer
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-4.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-4.png)
  1. Select your load balancer in the list and change the stickiness configuration on clicking on the edit button under "Port Configuration".
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-5.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-5.png)
  1. Choose "Enable Load Balancer Generated Cookie Stickiness" and let the duration field blank
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-6.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/elb-6.png)
  1. Open your browser on the load balancer "A Record"
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/result.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/result.png)
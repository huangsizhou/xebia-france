

# Introduction #

This article will guide you throw the creation and configuration of an AMI with JMeter embedded.

# Access the AWS Management Console #

Connect to Amazon AWS Management Console with your credentials (sent in the email "Xebia France Amazon EC2 Credentials").

The IAM User sign-in URL is : https://xebia-france.signin.aws.amazon.com/console

# Create the JMeter Instance #

## Create an EC2 Instance ##

_**Observation:** images below may not be synchronized with the steps. In this case follow the step, not the image._

  1. Connect to AWS console
  1. On "EC2" tab, select your region (e.g. "EU West (Ireland)")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-select-eu-west-region.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-select-eu-west-region.png)
  1. On "EC2 Dashboard", click on "Launch new instance"
  1. Select your AMI (e.g. "Basic 32-bit Amazon Linux AMI 2011.09")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-1.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-1.png)
  1. Select the instance size (e.g. "Micro")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-2-instance-details.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-2-instance-details.png)
  1. Copy and paste the YAML script below into the user data field
```
#cloud-config

# Install Java 6
packages:
- java-1.6.0-openjdk

# Install JMeter
runcmd:
- [ sh, -xc, "echo $(date) ': cloudinit runcmd begin'" ]
- [ cd, /tmp ]
- [ wget, "http://mir2.ovh.net/ftp.apache.org/dist//jakarta/jmeter/binaries/jakarta-jmeter-2.5.1.tgz" ]
- [ tar, -zxvf, jakarta-jmeter-2.5.1.tgz ]
- [ mv, jakarta-jmeter-2.5.1, /usr/local ]
- [ touch, /etc/profile.d/jmeter.sh ]
- [ sh, -xc, "echo '# Set path for JMEter' >> /etc/profile.d/jmeter.sh" ]
- [ sh, -xc, "echo 'export JMETER_HOME=/usr/local/jakarta-jmeter-2.5.1' >> /etc/profile.d/jmeter.sh" ]
- [ sh, -xc, "echo 'export PATH=$PATH:$JMETER_HOME/bin' >> /etc/profile.d/jmeter.sh" ]
- [ chmod, 755, /etc/profile.d/jmeter.sh ]
- [ wget, "http://xebia-france.googlecode.com/svn/repository/maven2/fr/xebia/demo/travel/xebia-spring-travel-ecommerce/1.0.1/jmeter.zip" ]
- [ unzip, jmeter.zip ]
- [ sh, -xc, "echo $(date) ': cloudinit runcmd end'" ]
```
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-3-instance-details.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-3-instance-details.png)
  1. Type the name of your server (e.g. "jmeter")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-4-instance-details.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-4-instance-details.png)
  1. Select a key-pair that will be used for SSH connection (e.g. "xebia-france.pem")
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-5-key-pair.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-5-key-pair.png)
  1. Select the "accept-all" security group
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/ec2-security-group.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/ec2-security-group.png)
  1. Review the configuration and click on "Launch"
> > ![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-7-review.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-launch-linux-instance-7-review.png)

Note: downloaded files are accessible in /tmp directory
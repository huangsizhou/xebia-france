

<br />

---

<br />

# Install #

Please refer to the emails "Xebia Amazon AWS Workshop Credentials" and "Xebia Amazon AWS Workshop Tools installation procedure" that were sent to you.

### Download ###

  * EC2 api tools: download [ec2-api-tools.zip](http://s3.amazonaws.com/ec2-downloads/ec2-api-tools.zip) and unzip it under "`~/aws-tools/ec2`" ("`c:\aws-tools\ec2`" on Windows),
  * RDS command line tools: download [RDSCli.zip](http://s3.amazonaws.com/rds-downloads/RDSCli.zip) and unzip it under "`~/aws-tools/rds`" ("`c:\aws-tools\rds`" on Windows),
  * ElasticLoadBalancing tools: download [ElasticLoadBalancing.zip](http://ec2-downloads.s3.amazonaws.com/ElasticLoadBalancing.zip) and unzip it under "`~/aws-tools/rds`" ("`c:\aws-tools\elb`" on Windows),

### Configure on Linux / MacOS X ###

  * Sample extract from `.profile`
```
export EC2_HOME=~/aws-tools/ec2
export AWS_RDS_HOME=~/aws-tools/rds
export AWS_ELB_HOME=~/aws-tools/elb

export PATH=$PATH:$EC2_HOME/bin:$AWS_RDS_HOME/bin:$AWS_ELB_HOME/bin

# EC2_REGION works for rds tools and elb tools but not for ec2 tools
export EC2_REGION=eu-west-1
# for ec2 tools
export EC2_URL=https://ec2.eu-west-1.amazonaws.com

export AWS_CREDENTIAL_FILE=~/.aws/aws-credentials.txt
export EC2_CERT=~/.aws/cert-AWVCG4HIN5C6I6ZY5JFHU7XQW3BZF7OK.pem
export EC2_PRIVATE_KEY=~/.aws/pk-AWVCG4HIN5C6I6ZY5JFHU7XQW3BZF7OK.pem

export JAVA_HOME=/PATH/VERS/JAVA/HOME
# MacOS X
# export JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Home/
```

  * check that your clock is well synchronized : you might get timestamp error otherwise when using the command lines.

### Configure on Windows ###

  * Create the followings environment variables:
```
EC2_HOME=c:\aws-tools\ec2
AWS_RDS_HOME=c:\aws-tools\rds
AWS_ELB_HOME=c:\aws-tools\elb

PATH=%PATH%;%EC2_HOME%\bin;%AWS_RDS_HOME%\bin;%AWS_ELB_HOME%\bin

# EC2_REGION works for rds tools and elb tools but not for ec2 tools
EC2_REGION=eu-west-1
# for ec2 tools
EC2_URL=https://ec2.eu-west-1.amazonaws.com

AWS_CREDENTIAL_FILE=c:\aws\aws-credentials.txt
EC2_CERT=c:\aws\cert-AWVCG4HIN5C6I6ZY5JFHU7XQW3BZF7OK.pem
EC2_PRIVATE_KEY=c:\aws\pk-AWVCG4HIN5C6I6ZY5JFHU7XQW3BZF7OK.pem

JAVA_HOME=\PATH\VERS\JAVA\HOME
```

<br />

---

<br />

# Amazon Relational Data Service (RDS) #

[Amazon Relational Database Service Quick Reference Card (pdf)](http://awsdocs.s3.amazonaws.com/RDS/latest/rds-qrc.pdf)

## Create a MySQL Database ##

<img src='http://www.clker.com/cliparts/9/1/4/0/11954322131712176739question_mark_naught101_02.svg.med.png' width='20' />
**Create a MySQL database with:**
  * `DBInstanceIdentifier`: petclinic-my-tri-gram
  * `db-name`: petclinic
  * `db-instance-class`: db.m1.small
  * `engine`: MySQL
  * `master-user-password`: petclinic
  * `master-username`: petclinic
  * `db-security-groups`: default
  * `allocated-storage`: 5 Go
  * `backup-retention-period`: 0

<img src='http://www.clker.com/cliparts/w/x/4/3/m/V/blue-information-glossy-button-md.png' width='20' /> [Amazon RDS > Command Line Reference > API Command Line Tools Reference > rds-create-db-instance](http://docs.amazonwebservices.com/AmazonRDS/latest/CommandLineReference/index.html?CLIReference-cmd-CreateDBInstance.html)

<font size='1'>
<pre>
rds-create-db-instance<br>
DBInstanceIdentifier<br>
-s (--allocated-storage) value<br>
-c (--db-instance-class) value<br>
-e (--engine) value<br>
-lm (--license model) value<br>
-p (--master-user-password) value<br>
-u (--master-username) value<br>
[-au (--auto-minor-version-upgrade) value ]<br>
[-v (--engine-version) value ]<br>
[-z (--availability-zone) value ]<br>
[--db-name value ]<br>
[-g (--db-parameter-group) value]<br>
[-m (--multi-az) value]<br>
[-a (--db-security-groups) value[,value...] ]<br>
[--port value ]<br>
[-w (--preferred-maintenance-window) value ]<br>
[-r (--backup-retention-period) value ]<br>
[-b (--preferred-backup-window) value ]<br>
[General Options]<br>
</pre>
</font>

<img src='http://www.clker.com/cliparts/3/d/e/4/12428083851546739178Symbol_OK.svg.med.png' width='20' />
Answer [AmazonAwsApiToolsTrainingAnswerRds#Create\_a\_MySQL\_Database](AmazonAwsApiToolsTrainingAnswerRds#Create_a_MySQL_Database.md).

<br />
## Describe MySQL Instance ##

<img src='http://www.clker.com/cliparts/9/1/4/0/11954322131712176739question_mark_naught101_02.svg.med.png' width='20' /> **Retrieve the Hostname (xyz.eu-west-1.rds.amazonaws.com) of your database**

<img src='http://www.clker.com/cliparts/w/x/4/3/m/V/blue-information-glossy-button-md.png' width='20' />
[Amazon RDS > Command Line Reference > API Command Line Tools Reference > rds-describe-instances](http://docs.amazonwebservices.com/AmazonRDS/latest/CommandLineReference/index.html?CLIReference-cmd-DescribeDBInstances.html)

<font size='1'>
<pre>
rds-describe-db-instances<br>
[DBInstanceIdentifier ]<br>
[--max-records ] value<br>
</pre>
</font>

<img src='http://www.clker.com/cliparts/3/d/e/4/12428083851546739178Symbol_OK.svg.med.png' width='20' />
Answer [AmazonAwsApiToolsTrainingAnswerRds#Describe\_Instances](AmazonAwsApiToolsTrainingAnswerRds#Describe_Instances.md).

<br />

---

<br />

# Amazon EC2 #

[Amazon EC2 Quick Reference Card (pdf)](http://awsdocs.s3.amazonaws.com/EC2/latest/ec2-qrc.pdf)

## Create Linux instances ##

<img src='http://www.clker.com/cliparts/9/1/4/0/11954322131712176739question_mark_naught101_02.svg.med.png' width='20' /> **Create two EC2 instances**
  * `ami_id`: ami-47cefa33
  * `instance-count`: 2
  * `group`: tomcat
  * `keypair`: xebia-france
  * `instance-type`: t1.micro
  * `user-data-file`: <see below>

<img src='http://www.clker.com/cliparts/w/x/4/3/m/V/blue-information-glossy-button-md.png' width='20' />Choose your CloudInit UserData file:
  * [cloud-config-amzn-linux.txt](http://xebia-france.googlecode.com/svn/training/java-infra-as-code-with-amzn-aws/trunk/src/main/resources/cloud-config-amzn-linux.txt) Simple cloud-config to install tomcat6 RPM (don't deploy petclinic.war)
  * [userdata-amzn-linux.txt](http://xebia-france.googlecode.com/svn/training/java-infra-as-code-with-amzn-aws/trunk/src/test/resources/userdata-amzn-linux.txt) Multipart cloud-config to install tomcat6 RPM (via cloud-config) and deploy petclinic.war (via a python shell script)



<img src='http://www.clker.com/cliparts/w/x/4/3/m/V/blue-information-glossy-button-md.png' width='20' />  [Amazon EC2 > Command Line Reference > API Tools Reference > ec2-run-instances](http://docs.amazonwebservices.com/AWSEC2/latest/CommandLineReference/index.html?ApiReference-cmd-RunInstances.html)

<img src='http://www.clker.com/cliparts/w/x/4/3/m/V/blue-information-glossy-button-md.png' width='20' />
Docs AMI http://aws.amazon.com/amazon-linux-ami/

<font size='1'>
<pre>
ec2-run-instances<br>
ami_id<br>
[-n instance_count]<br>
[-g group [-g group ...]]<br>
[-k keypair]<br>
[-d user_data |-f user_data_file]<br>
[--addressing addressing_type]<br>
[--instance-type instance_type]<br>
[--availability-zone zone]<br>
[--kernel kernel_id]<br>
[--ramdisk ramdisk_id]<br>
[--block-device-mapping block_device_mapping]<br>
[--monitor]<br>
[--disable-api-termination]<br>
[--instance-initiated-shutdown-behavior behavior]<br>
[--placement-group placement-group]<br>
[--tenancy tenancy]<br>
[--subnet subnet]<br>
[--private-ip-address ip_address]<br>
[--client-token token]<br>
</pre>
</font>

<img src='http://www.clker.com/cliparts/d/b/d/3/1194998844557242824messagebox_warning.svg.med.png' width='20' />
You will need to remember the names of the created instances (i-XXXXXXXX).

<img src='http://www.clker.com/cliparts/3/d/e/4/12428083851546739178Symbol_OK.svg.med.png' width='20' />
Answer [AmazonAwsApiToolsTrainingAnswerEc2#Create\_Linux\_instances](AmazonAwsApiToolsTrainingAnswerEc2#Create_Linux_instances.md).

<br />
## Name your instances with the "Name" tag ##

<img src='http://www.clker.com/cliparts/9/1/4/0/11954322131712176739question_mark_naught101_02.svg.med.png' width='20' /> **Give a name 'petclinic-my-tri-gram' to your Amazon instances with the "Name" tag**

<img src='http://www.clker.com/cliparts/w/x/4/3/m/V/blue-information-glossy-button-md.png' width='20' />
[Amazon EC2 > Command Line Reference > API Tools Reference > ec2-create-tags](http://docs.amazonwebservices.com/AWSEC2/latest/CommandLineReference/index.html?ApiReference-cmd-CreateTags.html)

<font size='1'>
<pre>
ec2-create-tags<br>
resource_id [resource_id ...]<br>
--tag key[=value] [--tag key[=value] ...]<br>
</pre>
</font>

<img src='http://www.clker.com/cliparts/d/b/d/3/1194998844557242824messagebox_warning.svg.med.png' width='20' /> The tag key should be exactly Name ie with a uppercase N.

<img src='http://www.clker.com/cliparts/3/d/e/4/12428083851546739178Symbol_OK.svg.med.png' width='20' />
Answer [AmazonAwsApiToolsTrainingAnswerEc2#Tag\_instances\_/\_give\_them\_a\_name](AmazonAwsApiToolsTrainingAnswerEc2#Tag_instances_/_give_them_a_name.md).

<br />

---

<br />

# Amazon Elastic Load Balancing API #

[Elastic Load Balancing Quick Reference Card (pdf)](http://awsdocs.s3.amazonaws.com/ElasticLoadBalancing/latest/elb-qrc.pdf)

## Create Load Balancer ##

<img src='http://www.clker.com/cliparts/9/1/4/0/11954322131712176739question_mark_naught101_02.svg.med.png' width='20' /> **Create a load balancer named "petclinic-my-tri-gram" handking HTTP from 80 to 8080. To ease the setup, associate it with all the availability zones "eu-west-1a,eu-west-1b,eu-west-1c"**

<img src='http://www.clker.com/cliparts/w/x/4/3/m/V/blue-information-glossy-button-md.png' width='20' />
[ELB > Elastic Load Balancing API Reference > Actions > CreateLoadBalancer](http://docs.amazonwebservices.com/ElasticLoadBalancing/latest/APIReference/index.html?API_CreateLoadBalancer.html)

<font size='1'>
<pre>
elb-create-lb<br>
!LoadBalancerName<br>
--availability-zones value [, value...]<br>
--listener "protocol=value,lb-port=value,instance-port=value [,certid=value]"<br>
[--listener "protocol=value,lb-port=value,instance-port=value [,certid=value]"...]<br>
</pre>
</font>

<img src='http://www.clker.com/cliparts/3/d/e/4/12428083851546739178Symbol_OK.svg.med.png' width='20' />
Answer [AmazonAwsApiToolsTrainingAnswerElb#Create\_Load\_Balancer](AmazonAwsApiToolsTrainingAnswerElb#Create_Load_Balancer.md)

<br />
## Configure health check ##

<img src='http://www.clker.com/cliparts/9/1/4/0/11954322131712176739question_mark_naught101_02.svg.med.png' width='20' /> **Configure a healthcheck targeting "HTTP:8080/" with threshold (healthy & unhealthy) "2" and timeout "2" and interval "30"**

<img src='http://www.clker.com/cliparts/w/x/4/3/m/V/blue-information-glossy-button-md.png' width='20' />
[ELB > Elastic Load Balancing API Reference > Actions > ConfigureHealthCheck](http://docs.amazonwebservices.com/ElasticLoadBalancing/latest/APIReference/index.html?API_ConfigureHealthCheck.html)

<font size='1'>
<pre>
elb-configure-healthcheck<br>
!LoadBalancerName<br>
--target value<br>
--healthy-threshold value<br>
--unhealthy-threshold value<br>
--interval value<br>
--timeout value<br>
</pre>
</font>

<img src='http://www.clker.com/cliparts/3/d/e/4/12428083851546739178Symbol_OK.svg.med.png' width='20' />
Answer [AmazonAwsApiToolsTrainingAnswerElb#Configure\_health\_check](AmazonAwsApiToolsTrainingAnswerElb#Configure_health_check.md)

<br />
## Register instances with load balancer ##

<img src='http://www.clker.com/cliparts/9/1/4/0/11954322131712176739question_mark_naught101_02.svg.med.png' width='20' /> **Register your linux instances**

<img src='http://www.clker.com/cliparts/w/x/4/3/m/V/blue-information-glossy-button-md.png' width='20' />
[ELB > Elastic Load Balancing API Reference > Actions > RegisterInstancesWithLoadBalancer](http://docs.amazonwebservices.com/ElasticLoadBalancing/latest/APIReference/index.html?API_RegisterInstancesWithLoadBalancer.html)

<font size='1'>
<pre>
elb-register-instances-with-lb<br>
!LoadBalancerName<br>
--instances value [, value...]<br>
</pre>
</font>

<img src='http://www.clker.com/cliparts/3/d/e/4/12428083851546739178Symbol_OK.svg.med.png' width='20' />
Answer [AmazonAwsApiToolsTrainingAnswerElb#Register\_instances](AmazonAwsApiToolsTrainingAnswerElb#Register_instances.md)

<br />
## Create load balancer stickiness policy ##

<img src='http://www.clker.com/cliparts/9/1/4/0/11954322131712176739question_mark_naught101_02.svg.med.png' width='20' /> **Configure cookie based stickiness giving policyname "petclinic-policy-my-tri-gram"**

<img src='http://www.clker.com/cliparts/w/x/4/3/m/V/blue-information-glossy-button-md.png' width='20' />
[ELB > Elastic Load Balancing API Reference > Actions > CreateLBCookieStickinessPolicy](http://docs.amazonwebservices.com/ElasticLoadBalancing/latest/APIReference/index.html?API_CreateLBCookieStickinessPolicy.html)

<font size='1'>
<pre>
elb-create-lb-cookie-stickiness-policy<br>
!LoadBalancerName<br>
--policy-name value<br>
[--expiration-period value]<br>
</pre>
</font>

<img src='http://www.clker.com/cliparts/3/d/e/4/12428083851546739178Symbol_OK.svg.med.png' width='20' />
Answer [AmazonAwsApiToolsTrainingAnswerElb#Create\_load\_balancer\_stickiness\_policy](AmazonAwsApiToolsTrainingAnswerElb#Create_load_balancer_stickiness_policy.md)

<br />
## Set Created Policy To Load Balancer ##

<img src='http://www.clker.com/cliparts/9/1/4/0/11954322131712176739question_mark_naught101_02.svg.med.png' width='20' /> **Set Created Policy To Load Balancer**

<img src='http://www.clker.com/cliparts/w/x/4/3/m/V/blue-information-glossy-button-md.png' width='20' />
[ELB > Elastic Load Balancing API Reference > Actions > SetLoadBalancerPoliciesOfListener](http://docs.amazonwebservices.com/ElasticLoadBalancing/latest/APIReference/index.html?API_SetLoadBalancerPoliciesOfListener.html)

<font size='1'>
<pre>
elb-set-lb-policies-of-listener<br>
!LoadBalancerName<br>
--lb-port  value<br>
--policy-names  value[,value...]<br>
[General Options]<br>
</pre>
</font>

<img src='http://www.clker.com/cliparts/3/d/e/4/12428083851546739178Symbol_OK.svg.med.png' width='20' />
Answer [AmazonAwsApiToolsTrainingAnswerElb#Set\_up\_policy](AmazonAwsApiToolsTrainingAnswerElb#Set_up_policy.md)


<br />

---

<br />

# Goal of the lab #

The goal of this lab is to code with Amazon AWS SDK for Java the deployment of the Petclinic application.

**A skeleton is provided :  [YourMakerChallenge](http://xebia-france.googlecode.com/svn/training/java-infra-as-code-with-amzn-aws/trunk/src/main/java/fr/xebia/demo/amazon/aws/petclinic/challenge/YourMakerChallenge.java)**
  * we coded all the defensive code to prevent creation of too many instances,
  * you will code the "create" code


<br />

---

<br />

# Project Installation #


  * Checkout http://xebia-france.googlecode.com/svn/training/java-infra-as-code-with-amzn-aws/trunk/

  * the package [fr.xebia.demo.amazon.aws.petclinic](http://xebia-france.googlecode.com/svn/training/java-infra-as-code-with-amzn-aws/trunk/src/main/java/fr/xebia/demo/amazon/aws/petclinic/) contains the code used during this training.

  * The goal is to create methods in the [YourMakerChallenge](http://xebia-france.googlecode.com/svn/training/java-infra-as-code-with-amzn-aws/trunk/src/main/java/fr/xebia/demo/amazon/aws/petclinic/challenge/YourMakerChallenge.java) class
    * <font size='2'><pre>fr.xebia.demo.amazon.aws.petclinic.challenge.!YourMakerChallenge</pre></font>

  * You can use the [PetclinicInfrastructureMakerTest](http://xebia-france.googlecode.com/svn/training/java-infra-as-code-with-amzn-aws/trunk/src/test/java/fr/xebia/demo/amazon/aws/petclinic/PetclinicInfrastructureMakerTest.java) class to run methods
    * <font size='2'><pre>fr.xebia.demo.amazon.aws.petclinic.!PetclinicInfrastructureMakerTest</pre></font>

  * Answers are in the [MakerChallengeAnswer](http://xebia-france.googlecode.com/svn/training/java-infra-as-code-with-amzn-aws/trunk/src/main/java/fr/xebia/demo/amazon/aws/petclinic/challenge/MakerChallengeAnswer.java) class
    * <font size='2'><pre>fr.xebia.demo.amazon.aws.petclinic.challenge.!MakerChallengeAnswer</pre></font>

<br />

## Get AwsCredentials from properties file ##

```
    InputStream credentialsAsStream = Thread.currentThread().getContextClassLoader()
                     .getResourceAsStream("AwsCredentials.properties");
    AWSCredentials credentials = new PropertiesCredentials(credentialsAsStream);
```

> AwsCredentials.properties file looks like this :

```
#Insert your AWS Credentials from http://aws.amazon.com/security-credentials
accessKey=change me
secretKey=change me
```

<br />

---

<br />

# Set up your personal information #

You will need to fill in your personal information :

  * getTrigram
Your trigram will help us to identify your instances.

  * getSshKeyPairName
The name of your amazon keyPair which has been sent to you by mail.
You will need it to instantiate your EC2 instances.

<br />

---

<br />

# Create Database instance #
> Amazon AWS propose Relational Database Service (RDS) to create ready to use database

> ## Use RDS Client ##
Use the EU-WEST-1 endpoint.

```
    private AmazonRDS rds;
    rds = new AmazonRDSClient(credentials);
    rds.setEndpoint("rds.eu-west-1.amazonaws.com");
```

> ## Create DB Instance ##
> Params :
<font size='1'>
<pre>
DBInstanceIdentifier: use method parameter "dbInstanceIdentifier"<br>
DBName: petclinic<br>
DBInstanceClass : db.m1.small<br>
Engine : MySQL<br>
!MasterUsername : petclinic<br>
!MasterUserPassword : petclinic<br>
DBSecurityGroups : default<br>
!AllocatedStorage : 5 (Go)<br>
!BackupRetentionPeriod : 0<br>
</pre>
</font>

```
    CreateDBInstanceRequest dbInstanceRequest = new CreateDBInstanceRequest() //
                                                    .with...
    DBInstance createDBInstance = rds.createDBInstance(dbInstanceRequest);
```

<br />

---

<br />

# Create Tomcat instances #

> ## Use `AmazonEC2Client` ##

> Use the EU-WEST-1 endpoint.

```
    AmazonEC2 ec2 = new AmazonEC2Client(credentials);
    ec2.setEndpoint("ec2.eu-west-1.amazonaws.com");
```

> ## Create EC2 Instances ##
> Params :
> <font size='1'>
<pre>
!ImageId : ami-47cefa33<br>
!MinCount : 2<br>
!MaxCount : 2<br>
!SecurityGroups : tomcat<br>
!KeyName : <YOUR_SSH_KEY_PAIR_NAME> (e.g. "cyrille_leclerc_at_gmail_dot_com")<br>
!InstanceType : com.amazonaws.services.ec2.model.InstanceType.T1Micro.toString()<br>
!UserData : use cloudInit.createUserDataBuilder(DBInstance dbInstance, String warUrl).buildBase64UserData() Method<br>
</pre>
</font>

```
    RunInstancesRequest runInstanceRequest = new RunInstancesRequest() //
                                               .with...();
    RunInstancesResult runInstances = ec2.runInstances(runInstanceRequest);
```
<pre>
You need to get Reservation object on !RunInstancesResult before returning instances.<br>
</pre>

<br />

---

<br />

# Create a Load Balancer #

> ## Use `AmazonElasticLoadBalancingClient` ##

Use the EU-WEST-1 endpoint.

```
    AmazonElasticLoadBalancing elb = new AmazonElasticLoadBalancingClient(credentials);
    elb.setEndpoint("elasticloadbalancing.eu-west-1.amazonaws.com");
```

> ## Create an Elastic Load Balancer ##

> Params :
<font size='1'>
<pre>
!LoadBalancerName : use method parameter "loadBalancerName"<br>
Listener : "HTTP", 80 et 8080<br>
!AvailabilityZones : "eu-west-1a", "eu-west-1b", "eu-west-1c"<br>
</pre>
</font>

```
    CreateLoadBalancerRequest createLoadBalancerRequest = new CreateLoadBalancerRequest() //
                .with...();
    elb.createLoadBalancer(createLoadBalancerRequest);
```

> ## Configure Ec2Instances ##
> Params :
<font size='1'>
<pre>
!LoadBalancerName : use method parameter "loadBalancerName"<br>
Instances : Add two ec2 Instances<br>
(Be careful, add EC2 instances of com.amazonaws.services.elasticloadbalancing.model.Instance class<br>
and not com.amazonaws.services.ec2.model.Instance class)<br>
</pre>
</font>

```
    List<com.amazonaws.services.elasticloadbalancing.model.Instance> instances = new ArrayList<com.amazonaws.services.elasticloadbalancing.model.Instance>();
    instances.add(...);
    instances.add(...);
        
    RegisterInstancesWithLoadBalancerRequest registerInstancesWithLoadBalancerRequest = new RegisterInstancesWithLoadBalancerRequest( //
                loadBalancerName, //
                instances);
        elb.registerInstancesWithLoadBalancer(registerInstancesWithLoadBalancerRequest);
```


# Infra as Code Samples #

  * Cloud Init samples
    * [cloud-config-amzn-linux-nexus.txt](http://xebia-france.googlecode.com/svn/cloudcomputing/xebia-cloudcomputing-extras/trunk/src/main/resources/fr/xebia/workshop/continuousdelivery/cloud-config-amzn-linux-nexus.txt)
    * [cloud-config-amzn-linux-jenkins-rundeck.txt](http://xebia-france.googlecode.com/svn/cloudcomputing/xebia-cloudcomputing-extras/trunk/src/main/resources/fr/xebia/workshop/continuousdelivery/cloud-config-amzn-linux-jenkins-rundeck.txt)
    * [provision\_tomcat.py.fmt](http://xebia-france.googlecode.com/svn/cloudcomputing/xebia-cloudcomputing-extras/trunk/src/main/resources/fr/xebia/training/troubleshooting/provision_tomcat.py.fmt)
  * Amazon AWS SDK for Java Sample:
    * [TroubleshootingTrainingInfrastructureCreator.java](http://xebia-france.googlecode.com/svn/cloudcomputing/xebia-cloudcomputing-extras/trunk/src/main/java/fr/xebia/training/troubleshooting/TroubleshootingTrainingInfrastructureCreator.java) ([syntax highlighted](http://code.google.com/p/xebia-france/source/browse/cloudcomputing/xebia-cloudcomputing-extras/trunk/src/main/java/fr/xebia/training/troubleshooting/TroubleshootingTrainingInfrastructureCreator.java))
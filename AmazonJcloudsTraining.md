

<br />

---

<br />

# Goal of the lab #

The goal of this lab is to code with the JClouds API the deployment of the Petclinic application on Amazon AWS/EC2.
JClouds is a library created to be used with a lot of cloud providers like Amazon, Azure and Rackspace.
Jclouds does not yet support features like RDS or ELB so we'll be using Amazon SDK for the creation of the database and the loadbalancer.

<br />

We will code the petclinic infrastructure twice :
  * First with **JCloud's specific API for Amazon AWS** [EC2TemplateOptions](http://www.jclouds.org/jclouds-maven-site/1.1.0/jclouds-multi/apidocs/org/jclouds/ec2/compute/options/EC2TemplateOptions.html). This technic is not fully portable.
  * Second with **JCloud's fully portable APIs**.

<br />

---

<br />

# Project Installation #

See AmazonAwsJavaSdkTraining for project installation.

To test your implementation, you can use :
  * `fr.xebia.demo.amazon.aws.petclinic.jclouds.PetclinicInfrastructureJcloudsAWSMakerTest`
  * `fr.xebia.demo.amazon.aws.petclinic.jclouds.PetclinicInfrastructureJcloudsMakerTest`

These two classes extend [MakerChallengeAnswer](http://xebia-france.googlecode.com/svn/training/java-infra-as-code-with-amzn-aws/trunk/src/main/java/fr/xebia/demo/amazon/aws/petclinic/challenge/MakerChallengeAnswer.java) to a create database and a load balancer. You dont have to implement these features

<br />
# Load Amazon AWS Credentials from properties file #

This class is specific to Amazon AWS, its goal is to load the access key and the secret key from a properties file.

```
    InputStream credentialsAsStream = Thread.currentThread().getContextClassLoader()
                     .getResourceAsStream("AwsCredentials.properties");
    AWSCredentials credentials = new PropertiesCredentials(credentialsAsStream);
```

`AwsCredentials.properties` file looks like this :

```
# Insert your AWS Credentials from http://aws.amazon.com/security-credentials
accessKey=change me
secretKey=change me
```

<br />

---

<br />

# JClouds with Amazon AWS specific features #

  * The goal is to implement methods in the [YourMakerJCloudsAWSChallenge](http://xebia-france.googlecode.com/svn/training/java-infra-as-code-with-amzn-aws/trunk/src/main/java/fr/xebia/demo/amazon/aws/petclinic/challenge/jclouds/YourMakerJCloudsAWSChallenge.java) class

  * Answers are in the [MakerJCloudsAWSChallengeAnswer](http://xebia-france.googlecode.com/svn/training/java-infra-as-code-with-amzn-aws/trunk/src/main/java/fr/xebia/demo/amazon/aws/petclinic/challenge/jclouds/MakerJCloudsAWSChallengeAnswer.java) class

The method
```
public List<Instance> createTwoEC2Instances(CloudInit cloudInit, DBInstance dbInstance, String warUrl) {
...
}
```
is provided, what you have to do is to implement the two private methods :

```
private ComputeServiceContext createComputeServiceContext()
```
```
private Template createDefaultTemplate(ComputeServiceContext context, CloudInit cloudInit, DBInstance dbInstance, String warUrl)
```

<br />
## Create Context ##

In this method, you must create a [ComputeServiceContext](http://jclouds.rimuhosting.com/apidocs/org/jclouds/compute/ComputeServiceContext.html) using [ComputeServiceContextFactory().createContext(...)](http://jclouds.rimuhosting.com/apidocs/org/jclouds/compute/ComputeServiceContextFactory.html#createContext%28java.lang.String,%20java.lang.String,%20java.lang.String,%20java.lang.Iterable,%20java.util.Properties%29).

Params :
<font size='1'>
<pre>
provider   : aws-ec2<br>
identity   : AWS Access ID from AWSCredential<br>
credential : AWS secret key from AWSCredential<br>
modules    : Only SLF4JLoggingModule is needed<br>
overrides  : You must override :<br>
AWSEC2Constants.PROPERTY_EC2_AMI_QUERY ( value : "virtualization-type=paravirtual;architecture=i386;owner-id=137112412989;state=available;image-type=machine;root-device-type=ebs")<br>
AWSEC2Constants.PROPERTY_EC2_CC_REGIONS (value :  "eu-west-1")<br>
</pre>
</font>

<br />
## Create template ##

In this method, you must create a [Template](http://jclouds.rimuhosting.com/apidocs/org/jclouds/compute/domain/Template.html) using [context.getComputeService().templateBuilder()...build()](http://jclouds.rimuhosting.com/apidocs/org/jclouds/compute/domain/TemplateBuilder.html#build%28%29)

Params :
<font size='1'>
<pre>
hardwareId  : !InstanceType.T1_MICRO<br>
os64Bit     : false<br>
osFamily    : !OsFamily.AMZN_LINUX<br>
imageId     : eu-west-1/ami-47cefa33<br>
locationId  : eu-west-1<br>
</pre>
</font>

Besides, you must add some options after the creation of the template using :
```
  template.getOptions()
```

<font size='1'>
<pre>
blockUntilRunning : true<br>
keyPair           : <your key pair name><br>
securityGroups    : tomcat<br>
userData          : cloudInit.createUserDataBuilder(...).buildUserData().getBytes()<br>
</pre>
</font>

For the 3 last options, you must use :
```
  template.getOptions().as(EC2TemplateOptions.class)
```
These options are Amazon specific.

<br />

---

<br />

# 100% portable pure JClouds (without Amazon AWS specific features) #

In the previous section, we used Amazon AWS specific code:
```
  template.getOptions().as(EC2TemplateOptions.class)...
```
This Amazon AWS specific feature wich is not available with other cloud providers.

We will now create the instances with portable equivalents of the following Amazon AWS specific concepts :
  * Key pair
  * Security groups
  * CloudInit

  * The goal is to create methods in the [YourMakerJCloudsChallenge](http://xebia-france.googlecode.com/svn/training/java-infra-as-code-with-amzn-aws/trunk/src/main/java/fr/xebia/demo/amazon/aws/petclinic/challenge/jclouds/YourMakerJCloudsChallenge.java) class

  * Answers are in the [MakerJCloudsAWSChallengeAnswer](http://xebia-france.googlecode.com/svn/training/java-infra-as-code-with-amzn-aws/trunk/src/main/java/fr/xebia/demo/amazon/aws/petclinic/challenge/jclouds/MakerJCloudsAWSChallengeAnswer.java) class

<img src='http://www.clker.com/cliparts/w/x/4/3/m/V/blue-information-glossy-button-md.png' width='20' />It can take a long time (> 10 minutes)

<br />
## Create JClouds context ##

In this method, you must create a `ComputeServiceContext` using `ComputeServiceContextFactory().createContext(...)`.

Params :
<font size='1'>
<pre>
provider   : aws-ec2<br>
identity   : AWS Access ID from AWSCredential<br>
credential : AWS secret key from AWSCredential<br>
modules    : SLF4JLoggingModule and !JschSshClientModule<br>
overrides  : You must override :<br>
AWSEC2Constants.PROPERTY_EC2_AMI_QUERY ( value : "virtualization-type=paravirtual;architecture=i386;owner-id=137112412989;state=available;image-type=machine;root-device-type=ebs")<br>
AWSEC2Constants.PROPERTY_EC2_CC_REGIONS (value :  "eu-west-1")<br>
</pre>
</font>

<img src='http://www.clker.com/cliparts/w/x/4/3/m/V/blue-information-glossy-button-md.png' width='20' /> We add JschSshClientModule to the list of modules. Without it, we would not able to run ssh scripts.

<br />
## Create JClouds template ##

In this method, you must create a `Template` using `context.getComputeService().templateBuilder()...build()`:

Params :
<font size='1'>
<pre>
hardwareId  : !InstanceType.T1_MICRO<br>
os64Bit     : false<br>
osFamily    : !OsFamily.AMZN_LINUX<br>
imageId     : eu-west-1/ami-47cefa33<br>
locationId  : eu-west-1<br>
options     : Open ports 22 et 8080 using Builder.inboundPorts(...)<br>
options     : Run script to replace !CloudInit using Builder.runScript(Payloads.newStringPayload(...).<br>
The script is available in method : !JCloudUtil.bootStrapScript(...)<br>
</pre>
</font>

Besides, you must add some options after the creation of the template using :
```
  template.getOptions()
```

<font size='1'>
<pre>
blockUntilRunning : true<br>
</pre>
</font>

<br />
## Deploy authorized SSH public keys ##

For SSH Keys we had many choices :
  * Use the Shell script to edit the ec2-user ~/.ssh/authorized\_keys and add our public key associated with our .pem file
  * Upload a RSA public key files using JClouds API (generated using ssh-keygen)
  * Let JClouds create SSH Keys and write it on our file system

We choose the last one so we have to write the SSH keys to our file system implementing :
```
  private void writeSSHKey(Set<? extends NodeMetadata> nodes) throws IOException{}
```

The private key is available with
```
  node.getCredentials().credential
```
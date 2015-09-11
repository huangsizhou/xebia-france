# AmazonEC2 #

<br />
## Create Linux instances ##

**Command**
```
ec2-run-instances \
	ami-47cefa33 \
 	-n 2 \
	-g tomcat \
	-k xebia-france \
	--instance-type t1.micro  \
	-f user_data_file
```



**Output**
```
RESERVATION	r-ac7f18da	010154155802	tomcat
INSTANCE	i-fc022b8a	ami-47cefa33			pending	xebia-france	0		t1.micro	2011-08-01T21:37:24+0000	eu-west-1b	aki-4deec439			monitoring-disabled					ebs					paravirtual	xen		sg-18838c6c	default
INSTANCE	i-fa022b8c	ami-47cefa33			pending	xebia-france	1		t1.micro	2011-08-01T21:37:24+0000	eu-west-1b	aki-4deec439			monitoring-disabled					ebs					paravirtual	xen		sg-18838c6c	default
```

<br />
## Describe Instances ##

**Command**
```
ec2-describe-instances i-fc022b8a i-fa022b8c
```

**Output**
```
RESERVATION	r-ac7f18da	010154155802	tomcat
INSTANCE	i-fc022b8a	ami-47cefa33	ec2-46-51-165-131.eu-west-1.compute.amazonaws.com	ip-10-227-129-115.eu-west-1.compute.internal	running	xebia-france	0		t1.micro	2011-08-01T21:37:24+0000	eu-west-1b	aki-4deec439			monitoring-disabled	46.51.165.131	10.227.129.115			ebs					paravirtual	xen		sg-18838c6c	default
BLOCKDEVICE	/dev/sda1	vol-df5918b6	2011-08-01T21:37:46.000Z	
INSTANCE	i-fa022b8c	ami-47cefa33	ec2-46-137-143-37.eu-west-1.compute.amazonaws.com	ip-10-48-7-56.eu-west-1.compute.internal	running	xebia-france	1		t1.micro	2011-08-01T21:37:24+0000	eu-west-1b	aki-4deec439			monitoring-disabled	46.137.143.37	10.48.7.56			ebs					paravirtual	xen		sg-18838c6c	default
BLOCKDEVICE	/dev/sda1	vol-dd5918b4	2011-08-01T21:37:45.000Z	
```

<br />
## Tag instances / give them a name ##

**Command**
```
ec2-create-tags i-fc022b8a i-fa022b8c --tag Name=petclinic-clc
```

**Output**
```
TAG	instance	i-fc022b8a	Name	petclinic-clc
TAG	instance	i-fa022b8c	Name	petclinic-clc
```
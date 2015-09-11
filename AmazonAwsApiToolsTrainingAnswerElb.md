# Amazon ELB #

<br />
## Create Load Balancer ##

Note: to simplify, we register all the availability zones.

**Command**
```
elb-create-lb \
	petclinic-clc \
	--availability-zones eu-west-1a,eu-west-1b,eu-west-1c \
	--listener "protocol=HTTP, lb-port=80, instance-port=8080"
```

**Output**
```
DNS_NAME  petclinic-clc-214905302.eu-west-1.elb.amazonaws.com
```

<br />
## Configure health check ##

**Command**
```
elb-configure-healthcheck \
	petclinic-clc \
	--target HTTP:8080/ \
	--healthy-threshold 2 \
	--unhealthy-threshold 2 \
	--interval 30 \
	--timeout 2
```

**Output**
```
HEALTH_CHECK  HTTP:8080/  30  2  2  2
```

<br />
## Register instances ##

**Command**
```
elb-register-instances-with-lb petclinic-clc --instances i-fc022b8a, i-fa022b8c
```


**Output**
```
INSTANCE_ID  i-fc022b8a
INSTANCE_ID  i-fa022b8c
```

<br />
## Create load balancer stickiness policy ##

**Command**
```
elb-create-lb-cookie-stickiness-policy petclinic-clc --policy-name petclinic-policy-clc
```

**Output**
```
OK-Creating LB Stickiness Policy
```

<br />
## Set up policy ##

**Command**
```
elb-set-lb-policies-of-listener petclinic-clc --lb-port 80 --policy-names petclinic-policy-clc
```

**Output**
```
OK-Setting Policies
```
# Amazon Relational Data Service (RDS) #

<br />
## Create a MySQL Database ##

**Command**
```
rds-create-db-instance  \
	petclinic-clc \
	-s 5 \
	-c db.m1.small \
	-e MySQL \
	-p petclinic \
	-u petclinic \
	--db-security-groups default \
	--db-name petclinic \
	--port 3306 \
	--backup-retention-period 0
```

**Output**
```
DBINSTANCE  petclinic-clc  db.m1.small  mysql  5  petclinic  creating  0  ****  n  5.1.57 general-public-license
      SECGROUP  default  active
      PARAMGRP  default.mysql5.1  in-sync
```
<img src='http://www.clker.com/cliparts/d/b/d/3/1194998844557242824messagebox_warning.svg.med.png' width='20' /> Please note that the server ip address is not yet known !

<br />
## Describe Instances ##

**Command**
```
rds-describe-db-instances petclinic-clc
```

<img src='http://www.clker.com/cliparts/d/b/d/3/1194998844557242824messagebox_warning.svg.med.png' width='20' /> Warning! You won't get the same output as far the DB is in "creating" process. The db create may take few minutes, be patient!

**Output**
```
DBINSTANCE  petclinic-clc  2011-08-01T21:09:42.403Z  db.m1.small  mysql  5  petclinic  available  petclinic-clc.cccb4ickfoh9.eu-west-1.rds.amazonaws.com  3306  eu-west-1b  0  n  5.1.57
      SECGROUP  default  active
      PARAMGRP  default.mysql5.1  in-sync
```

The hostname is `petclinic-clc.cccb4ickfoh9.eu-west-1.rds.amazonaws.com`
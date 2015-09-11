# CloudInit / cloud-config script to install Jenkins #

Jenkins can easily be installed in Amazon EC2 with [Cloud Init](https://help.ubuntu.com/community/CloudInit).
In the following example, Jenkins is set up in an [Amazon Linux instance](http://aws.amazon.com/amazon-linux-ami/) using a Cloud Config file, one of the possible ways to do something during the first boot of a launched instance.

# cloud-config script #

```
#cloud-config

repo_additions:
 - source: jenkins
   filename: jenkins.repo
   name: Jenkins
   baseurl: http://pkg.jenkins-ci.org/redhat/
   key: http://pkg.jenkins-ci.org/redhat/jenkins-ci.org.key
   enabled: 1

packages:
- jenkins

runcmd:
 - [ sh, -xc, "echo $(date) ': cloudinit runcmd begin'" ]
 - [service, jenkins, start ]
 - [usermod, -a, -G, jenkins, ec2-user]
 - [ sh, -xc, "echo $(date) ': cloudinit runcmd end'" ]
```

Details:
  * Jenkins repo is added to the list of yum repos of the server (`repo_additions` block),
  * `jenkins` package is installed (`packages` block),
  * `jenkins` is started (`runcmd` block),
  * user `ec2-user` is added to the `jenkins` group(`runcmd` block).

Note that the `runcmd` commands must be written in a valid yaml syntax.

# Launching the new instance #

You will find below two different ways of launching a node with the new configuration. The first one shows how to do it through the wizard of Amazon WS Management Console :



![http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-create-instance-jenkins-GUI.png](http://xebia-france.googlecode.com/svn/wiki/aws-img/aws-ec2-create-instance-jenkins-GUI.png)


You can also do it through the command line making use of the [aws-tool API](http://aws.amazon.com/developertools/351).



`$ ec2-run-instances ami-45cefa31 -n 1 -g accept-all -k xebia-france --instance-type t1.micro -f cloud-config-amzn-linux-jenkins.txt`


# Troubleshooting CloudInit #

Some tips that will help you debugging your cloud init script :

## Logs ##

Cloud init logs files :
  * `/var/log/messages`
  * `/var/log/cloud-init.log`

## How to relaunch CloudInit script ##

  * In order to launch cloud-init without having to create a new instance, you can just erase the semaphores existing in `/var/lib/cloud/sem/` and run cloud-init with the your new config :
```
[ec2-user@ip-10-234-133-197 sem]$ sudo rm /var/lib/cloud/sem/*
[ec2-user@ip-10-234-133-197 log]$ sudo /etc/init.d/cloud-init start
```

More details on [Forum: Amazon Elastic Compute Cloud >Thread: How to debug CloudInit issues?](https://forums.aws.amazon.com/thread.jspa?messageID=232183)

## How to debug "Failed loading of cloud config ..." ##

Sample logs in `/var/log/messages`
```
Sep 19 20:39:00 ip-10-226-234-218 [CLOUDINIT] 2011-09-19 20:39:00,754 - cloud-init-cfg[INFO]: cloud-init-cfg ['mounts']
Sep 19 20:39:00 ip-10-226-234-218 [CLOUDINIT] 2011-09-19 20:39:00,771 - __init__.py[CRITICAL]: Failed loading of cloud config '/var/lib/cloud/data/cloud-config.txt'. Continuing with empty config
...
```

To debug this kind of problem, first verify that the cloud-config.txt file is valid yaml:
```
$ python
Python 2.6 ...
>>> import yaml
>>> stream = open("/path/to/cloud-config.txt", "r")}}}
>>> yaml.load(stream)
```

Call to `yaml.load(stream)` validates your yaml file.
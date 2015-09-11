# aws-tool #

> ### Preconditions ###

You should install aws-tool.

> ### start _n_ instances ###

Start _n_ instances of the image which contains JMXTrans and Graphite (ami-e51d2091):

```

ec2-run-instances --instance-count <NUMBER OF INSTANCES> -t t1.micro --key garnaud --group accept-all --region eu-west-1 ami-e51d2091

```

> ### retrieve the connection commands ###

Retrieve the commands for a connection on each instances:

```

ec2-describe-instances --filter image-id="ami-e51d2091" --filter instance-state-name="running" | grep INSTANCE | sed "s/^.*\(ec2-.*amazonaws.com\).*/ssh -i <PEM FILE> root@\1/" 

```

**NOTE**: before launching this command, wait that each instance is running.
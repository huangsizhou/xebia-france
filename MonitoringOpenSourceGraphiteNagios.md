# Nagios #

## Lab ##

Graphite plugin is installed in your nagios installation

**See graphite command definition in /etc/nagios/objects/command.cfg** Add host for all server of xebia spring travel
**Define alert on metrics stored in graphite**

| Metric | Warning | Critical |
|:-------|:--------|:---------|
| On each server GC usage > N% | 10      | 25       |
| Exception on credit card service (req/min) | 10      | 50       |
| Average CreditCardService response time in last n minutes > (ms) | 1000    | 5000     |

## Tips ##

### What Is This? ###

Nagios is a system and network monitoring application.
It watches hosts and services that you specify, alerting you when things go bad and when they get better.

You can easily define new plugin. We add a new command for queries stored metrics in graphite and define thresold for alert.



### Start and stop ###

Manage nagios deamon
/etc/init.d/nagios [start|stop]

Manage nagios web application
/etc/init.d/httpd [start|stop]

### Configuration file ###



#### Adding a new command definition ####

In command.cfg add

define command{
> command\_name    my\_check\_command
> command\_line    my\_check\_command.py -u $ARG1$ -w $ARG2$ -c $ARG3$
}

You can use a lot of variable like ARG or HOSTNAME.

#### Adding a host ####

Add a new host definition in a config file. For this workshop add host in /etc/nagios/travel/
We use linux-server template defined in /etc/nagios/objects/template.cfg

```
define host{
        use                     linux-server            ; Name of host template to use
                                                        ; This host definition will inherit all variables that are defined
                                                        ; in (or inherited by) the linux-server host template definition.
        host_name               antifraud-prod.travel.xebia-tech-event.info
        alias                   antifraud-prod
        address                 46.137.98.234
}
```

#### Adding a new service monitoring on a host ####

Add a new service definition in config file.

Some remote service defined in /etc/nagios/objets/remotehost.cfg

```
define service{
        host_name                 prod1.travel.xebia-tech-event.info
        use                            check_remote_disk
}
```


#### Getting raw data from graphite ####

You can use rawData=true for query timeseries

```
http://server/render?target=monitored-indicator&from=-1minutes&rawData=true
```
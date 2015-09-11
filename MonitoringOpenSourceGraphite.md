# Using Graphite #

# Lab #

  1. Configure storage for xebia-travel-prod with a frequency 10s
  1. Configure storage for xebia-travel-preprod with a frequency 1 min
  1. Start Carbon
  1. Start httpd
  1. Verify graphite and Carbon is up
  1. Add json file for connecting JMXTrans to Graphite
  1. Start Graphite
  1. Connect to Graphite dashboard (http://your-monitoring-server/) and verify all metrics are presents


---


[Graphite Home page](http://graphite.wikidot.com/start)

Graphite is a simple tool with two essential features :

  * Store numeric time-series data
  * Render graphs of this data on demand

Graphite is comprised of three components:
  * a webapp frontend which can be deployed on Django or by http server with python module
  * Whisper: a file system storage
  * Carbon: an agent that listens to time-series data, maintains these data in a cache, persists them to Whisper and answers to the webapp queries.

Data collection agents like JMXTrans connect to carbon and send their data, and Carbon's job is to make that data available for real-time graphing immediately and try to get it stored on disk as fast as possible.

## Carbon ##

### Installation and Configuration ###

  1. /etc/carbon/carbon.conf: General Carbon configuration
  1. /etc/carbon/storage-schemas.conf: Storage configuration
  1. /etc/init.d/carbon [stop|start]: start the Carbon instance

### Consult Carbon log ###

  * Log directory: /var/log/carbon

#### Configure time-series frequency and retention ####

The frequency of time-series and their retention duration are setted in the file **storage-schemas.conf**.  The main parameters are:

```
[storage_name]
pattern = REGEXP PATTERN
retentions = Frequency:RetentionTime,...
```

The pattern will define the set of metrics whose the retentions are applied. The **retentions** parameter defines a list of frequency/retention time's.

For instance the list below defines a first sample frequency of 5 seconds during 1 day. After 1 day frequency becomes 30 seconds during the 6 next days and so on.

```
[xebia_spring_travel]
pattern = .*
retentions = 5s:1d,30s:6d,1m:21d,15m:1y
```
See [Full storage configuration](http://readthedocs.org/docs/graphite/en/latest/config-carbon.html#storage-schemas-conf)

## Graphite webapp ##

The only thing to do is to start the http daemon:
  * /etc/init.d/httpd start

Graphite offers to deploying the webapp into Django, a python webserver. In this workshop, the webapp will be deployed directly in **Apache httpd** thanks to additionnal python modules. The webapp will be accessible on **port 80** by default.

## Whisper ##

**Whisper** doesn't need to be configured because **Carbon** directly drives it.
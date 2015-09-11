# Using JMXTrans #

# Lab #

  1. Create a new JMXTrans config file for prod1 server
  1. Create a query for all attributes of CreditCardService
  1. Output result on a file (KeyOutWriter)
  1. Create a query for GC monitoring
  1. Create a query for Memory monitoring
  1. Create a query for CPU monitoring
  1. Verify result on /tmp/server1.txt
  1. Report configuration for all server (1 file by server)
  1. Verify result on /tmp/serverN.txt
  1. Change sampling frequency to 10 seconds


---


## JMXTrans ##

[JMXTrans Home page](http://code.google.com/p/jmxtrans/)

JMX Transformer is very powerful tool which uses easily generated JSON based configuration files and then outputs the data in whatever format you desire.
It does this with a very efficient engine design that will scale to communicating with thousands of machines from a single jmxtrans instance.

## Installation ##

Your JMXtrans instance use RPM packaging.
Configuration files are in  :

  1. /var/lib/jmxtrans/ :for queries configuration
  1. /etc/sysconfig/jmxtrans: for JMXtrans configuration instance
  1. /etc/init.d/jmxtrans [start|stop] :You can start or stop instance

The file /etc/sysconfig/jmxtrans:

```
# configuration file for package jmxtrans
export JAR_FILE="/usr/share/jmxtrans/jmxtrans-all.jar"
export LOG_DIR="/var/log/jmxtrans"
export SECONDS_BETWEEN_RUNS=60
export JSON_DIR="/var/lib/jmxtrans"
export HEAP_SIZE=512
export NEW_SIZE=64
export CPU_CORES=2
export NEW_RATIO=8
```

## Configuration ##

For adding JMX configurations (servers, objects and attributes) and output writer, JMXTrans uses json files in /var/lib/jmxtrans/. Examples of json configuration files. The first one writes JMX metrics to a fle, the second one to Graphite:

### Query a mBean and send to file ###
```
{
  "servers" : [ {
    "port" : "1099",
    "host" : "jmxhost",
    "queries" : [ {
      "obj" : "java.lang:type=Memory",
      "attr" : [ "HeapMemoryUsage", "NonHeapMemoryUsage" ],
      "outputWriters" : [ {
        "@class" : "com.googlecode.jmxtrans.model.output.KeyOutWriter",
        "settings" : {
          "outputFile" : "/tmp/server1.txt",
          "maxLogFileSize" : "10MB",
          "maxLogBackupFiles" : 200,
          "debug" : true,
          "typeNames" : ["name"]
        }
      } ]
    } ]
  } ]
}
```

### Query a mBean and send to Graphite ###
```
{
  "servers" : [ {
    "port" : "1099",
    "host" : "jmxhost",
    "queries" : [ {
      "obj" : "java.lang:type=Memory",
      "attr" : [ "HeapMemoryUsage", "NonHeapMemoryUsage" ],
      "outputWriters" : [ {
        "@class" : "com.googlecode.jmxtrans.model.output.GraphiteWriter",
        "settings" : {
          "port" : 2003,
          "host" : "192.168.192.133"
        }
      } ]
    } ]
  } ]
}
```

### Change sampling frequency ###

Edit SECONDS\_BETWEEN\_RUNS attribute on /etc/sysconfig/jmxtrans
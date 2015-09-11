# Using Graphite URL API #

# Lab #

  1. Create a page dashboard.html in your favorite editor
  1. Add these graph

For each server

| Memory usage |
|:-------------|
| Cpu usage    |
| GC Time in % |

Aggregated by plateform (prod & preprod)

| Credit card service current invocation (Aggregated by plateform) |
|:-----------------------------------------------------------------|
| Credit card service request in error / minutes. (Aggregated and by type) |
| Antifraud web service call performance (average request/time in last minute) |


---


# Tips #

## Graphite ##

[The reference documentation](http://readthedocs.org/docs/graphite/en/latest/url-api.html)

Graphite allows using an http API for configuring the graph display. This URL is :

```
http://GRAPHITE_HOST:GRAPHITE_PORT/render?target=...&name=value&...
```

**NOTE** In this lab, the GRAPHITE\_HOST will be the public address of your amazon image and GRAPHITE\_PORT will be 80.

The **target** attribute defines the url of your(s) JMX server(s). You can define several server by adding a wildcard (an asterisk):

```
&target=company.server*.applicationInstance*.requestsHandled
```

You can wrap each target by one or more functions, like _derivatives, averages..._ All functions are defined [here](http://readthedocs.org/docs/graphite/en/latest/functions.html).

A lot of other parameters exist for customizing the graph as the background color, the start date, the graphic size...


## Transform a counter to req/time ##

Ex:
summarize(derivative(requestInError),"1min")

## Transform a counter to percentile ##

Ex: With sample frequency at 5 seconds

&target=asPercent(derivative(gc.`CollectionTime`),5000)

## Configuring time windows ##

# Last 5 minutes
&from=-5minutes

# Today
&from=today

## Configure Graphic size ##

&width=550&height=310

## Background color ##

&bgcolor=FFFFFF

## Give an alias to a metric ##

&target=alias(non\_human\_readable\_metric\_name,My readable name)

## Fix color list ##

&colorList=green,yellow,orange,red,purple,
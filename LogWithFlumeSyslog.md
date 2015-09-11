# In this part you will see how log from classic java application to Syslog-ng

# Log to syslog-ng from your application #

## From Log4j ##

Log4j have a syslog appender. [Log4j syslog](http://wiki.apache.org/logging-log4j/syslog)

log4j.rootLogger=INFO, A1
log4j.appender.A1=org.apache.log4j.net.SyslogAppender
log4j.appender.A1.SyslogHost=127.0.0.1
log4j.appender.A1.facility=USER
log4j.appender.A1.layout=org.apache.log4j.PatternLayout
log4j.appender.A1.layout.ConversionPattern=%-4r %-5p %c{2} %M.%L %x - %m\n

## From logback ##

Logback have a standard Syslog appender. [Syslog doc](http://logback.qos.ch/manual/appenders.html#SyslogAppender)

`<configuration>`
> `<appender name="SYSLOG" class="ch.qos.logback.classic.net.SyslogAppender">`
> > `<syslogHost>`remote\_home`</syslogHost>`
> > `<facility>`AUTH`</facility>`
> > `<suffixPattern>`[%thread] %logger %msg`</suffixPattern>`

> `</appender>`

> `<root level="DEBUG">`
> > `<appender-ref ref="SYSLOG" />`

> `</root>`
`</configuration>`

Here are the properties you can pass to a SyslogAppender.

| Property Name | Type | Description |
|:--------------|:-----|:------------|
| syslogHost    | String | The host name of the syslog server. |
| port          | String | The port number on the syslog server to connect to. Normally, one would not want to change the default value of 514. |
| facility      | String |The facility is meant to identify the source of a message. The facility option must be set to one of the strings KERN, USER, MAIL, DAEMON, AUTH, SYSLOG, LPR, NEWS, UUCP, CRON, AUTHPRIV, FTP, NTP, AUDIT, ALERT, CLOCK, LOCAL0, LOCAL1, LOCAL2, LOCAL3, LOCAL4, LOCAL5, LOCAL6, LOCAL7. Case is not important. |
| suffixPattern | String | The suffixPattern option specifies the format of the non-standardized part of the message sent to the syslog server. By default, its value is [%thread] %logger %msg. Any value that a PatternLayout could use is a correct suffixPattern value. |
| stackTracePattern | String | The stackTracePattern property allows the customization of the string appearing just before each stack trace line. The default value for this property is "\t", i.e. the tab character. Any value accepted by PatternLayout is a valid value for stackTracePattern. |
| throwableExcluded |boolean | Setting throwableExcluded to true will cause stack trace data associated with a Throwable to be omitted. By default, throwableExcluded is set to false so that stack trace data is sent to the syslog server. The syslog severity of a logging event is converted from the level of the logging event. The DEBUG level is converted to 7, INFO is converted to 6, WARN is converted to 4 and ERROR is converted to 3. Since the format of a syslog request follows rather strict rules, there is no layout to be used with SyslogAppender. However, using the suffixPattern option lets the user display whatever information she wishes. |


## From Apache ##

Apache support syslog only for ErrorLog. But access log can be pipe on another process.
You can send access log to 'logger' command for send them to syslog.

CustomLog "|/usr/bin/logger -t apache -i -p local6.notice" combined

# Exo 1 #

Change application configuration for logging from local file to local syslog-ng
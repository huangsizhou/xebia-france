

<br />

---

<br />

# Goal of the lab #

  * The purpose of this workshop is to use the CloudBees platform. CloudBees is both a development platform and a production platform.

  * Our goal is to make some changes to the famous petclinic application then deploy it on a CloudBees production cluster.

<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/workshop_overview.png' height='450' />
</p>

# Workshop requirements #

  * Git client: GitHub has [setup tutorials](http://help.github.com/set-up-git-redirect) for all platforms (just perform the "Download and Install Git" step, we'll set up the SSH key at the beginning of the lab).

  * [Apache Maven](http://maven.apache.org) (version 3.0.3).

  * SSH client (should already be installed on any Unix platform, will come with Git for Windows).

  * [cURL](http://curl.haxx.se/) command line tool for transferring data with URL syntax.

<br />

---

<br />

# Workshop Steps #

## @DEV ##

### Configure GIT repository ###

  * Backup your existing `~/.ssh` folder and install the workshop's SSH private key ([id\_rsa](https://s3-eu-west-1.amazonaws.com/xebia-cloudbees-workshop/id_rsa)):
```
$ mv ~/.ssh ~/.ssh.bak
$ mkdir ~/.ssh
$ curl https://s3-eu-west-1.amazonaws.com/xebia-cloudbees-workshop/id_rsa --output ~/.ssh/id_rsa
$ chmod 700 ~/.ssh
$ chmod -R 600 ~/.ssh/*
```

  * Unzip the [petclinic.zip](https://s3-eu-west-1.amazonaws.com/xebia-cloudbees-workshop/petclinic.zip) application under `~/petclinic`:
```
$ curl https://s3-eu-west-1.amazonaws.com/xebia-cloudbees-workshop/petclinic.zip --output /tmp/petclinic.zip
$ unzip /tmp/petclinic.zip -d ~/
$ cd ~/petclinic
```

  * Modify the **groupId** in the Maven POM file (replace 'XX' by your team number, e.g. '01'):
```
<groupId>fr.xebia.techevent.labXX</groupId>
```

  * Initialize a new Git local repository ([Everyday Git](http://schacon.github.com/git/everyday.html) in 20 commands is good for a useful minimum set of commands):
```
$ git init
$ git remote add origin ssh://git@git.cloudbees.com/atelier-xebia/labXX.git
$ git add -A
$ git commit -am 'Initial checkin - all files'
$ git push origin master
```

  * Create an integration branch named "develop":
```
$ git branch develop
$ git checkout develop
$ git push origin develop
```

  * Verify your branch organization by using the command "git-branch", and check if everything is correct:
```
$ git branch -a
  * develop
  master
  remotes/origin/develop
  remotes/origin/master
```

  * We will use a simple Git branching model based on the popular [Gitflow](http://nvie.com/posts/a-successful-git-branching-model/) model

### Create a Jenkins job for Continuous Integration ###

  * Log in to [CloudBees](https://grandcentral.cloudbees.com/) (_your credentials will be provided at the beginning of the lab_).


  * Click on the **Jenkins build** icon.
> > <img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/jenkins-build-icon.png' />


  * Go to the view corresponding to your group name **"labXX"**.

  * Create a new job from the left menu with the job name **"1-labXX-DEV"** and tick "Build a Maven2/3 project".

<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/add-jenkins-job.png' height='400' />
</p>

  * On the **"Code source management"** part choose **"Git"** and fill these parameters:
    * **URL of repository: `ssh://git@git.cloudbees.com/atelier-xebia/labXX.git`
    *** Branch Specifier (blank for default): `develop`
<p align='center'>
<blockquote><img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/jenkins-job-configuration-git.png' />
</p></blockquote>

  * On the **"Build triggers"** part, tick only **"Build when a change is pushed to CloudBees Forge"**, which replaces the traditional Poll SCM option by a Push from the repository itself:
<p align='center'>
<blockquote><img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/jenkins-job-configuration-build-trigger.png' />
</p></blockquote>

  * On the **"Build"** part, choose Maven Version 3.0.3 and indicate the Maven goal:
```
clean package
```
<p align='center'>
<blockquote><img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/jenkins-job-configuration-build.png' height='150' />
</p>
</blockquote>  * On the **"Post build action"** part,
    * **tick**"Deploy artifacts to my Private CloudBees Repository"**option and verify that SNAPSHOT repository is correctly selected,
    *** tick **Sonar** option to active sonar analysis of your project and set Branch to "labXX" to differentiate petclinic projetcs in sonar.
<p align='center'>
<blockquote><img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/jenkins-job-configuration-post-build-actions.png' />
</p>
</blockquote>  * Save the configuration and run the job (it can take some minutes before the build begins).

  * The build will be unstable due to a test failure:

<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/ci-job-failed.png' height='350' />
</p>

### Fix the build and check the Continuous Integration ###

  * Open the **OwnerTests** class and fix the test failure.
```
fido.setName("Rex"); // should be "Fido"!
```

  * Push your modification on the remote repository:
```
$ git commit -am "Fix the test failure ;-)"
$ git push origin develop
```

  * The Continuous Integration job should be run automatically.

<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/ci-job-fixed.png' height='350' />
</p>

  * Once the build is fixed, go back to the job page. Click on the **Sonar** link to have a look at your quality metrics.
> > <img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/jenkins-job-sonar-icon.png' />


<br />

---

<br />

## @INTEG ##

### Create and configure a new MySQL database ###
  * Go to the [services/database screen](https://run.cloudbees.com/a/atelier-xebia#db-manage) from your CloudBees account home screen
  * Click on the "add new database" link. Fill with the following parameters :
```
Database name: labXX-integ
Username: labXX-integ
Password: labXX-integ
```

<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/create-database-mysql-integ.png' height='350' />
</p>

### Configure a new application ###

  * On the left-hand side menu, click on **Applications / Add New application**.

  * **Create** a new application on **RUN@cloud** called "labXX-integ".
<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/create-app-integ.png' height='300' />
</p>

### Create a Jenkins job for deploying application on Integration environment ###

  * Create a new Job from the left menu with the job name **"2-labXX-INTEG"** and tick "Build a Maven2/3 project"

  * On the "Code source management" part, choose "Git" and fill these parameters:
```
URL of repository=ssh://git@git.cloudbees.com/atelier-xebia/labXX.git
Branch Specifier (blank for default)=develop
```

  * On the **"Build triggers"** part, uncheck all selected options and tick **"Build after other projects are built"** and indicate your Continuous Integration job
```
Project names: 1-labXX-DEV
```

  * On the "Build" part, choose Maven Version 3.0.3 and indicate the Maven goal
```
clean package
```

  * On the "Post build action" part, tick **"Deploy to CloudBees"** option and fill parameters:
```
Application Id=atelier-xebia/labXX-integ
```

<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/configure-deploy-jenkins-job.png' height='300' />
</p>

### Configure your application for Integration environment ###

  * The sources provided for the lab are already configured for MySQL. The only thing you need to do is adapt your database info. Go to the **src/main/resources/environment/integ/** directory and edit the **cloudbees.properties** file according to your team number (this file is used to filter **src/main/webapp/WEB-INF/cloudbees-web.xml** during the build):
```
cloudbees.datasource.username=labXX-integ
cloudbees.datasource.password=labXX-integ
cloudbees.datasource.url=jdbc:cloudbees://labXX-integ
```

  * Commit and push your changes:
```
$ git commit -am "Configure integration database"
$ git push origin develop
```

  * The Continuous Integration job should be run automatically. Then, the new Integration job should be executed.

  * Once the job is finished, your application is available at this URL:
```
http://labXX-integ.atelier-xebia.cloudbees.net
```

  * Also, the CloudBees website provides a management page for the application (click on **Applications** from the home page, then locate your instance in the list and click on it).

<br />

---

<br />

## @TEST ##

### Run Selenium tests from the cloud ###

  * Your project's **pom.xml** contains a preconfigure _selenium_ profile. Customize it to run the tests against your instance, by adapting the _myselenium.target.host_ property:
```
<profile>
 <id>selenium</id>
 <properties>
  <myselenium.target.host>http://labXX-integ.atelier-xebia.cloudbees.net</myselenium.target.host>
 </properties>
 ...
```

  * The actual Selenium test classes are in the _src/test/java/org/.../selenium_ package.

  * Modify Selenium test src/test/java/org/springframework/samples/petclinic/selenium/SeleniumSauceLabSetUp.java
  * You can change the desired capability to the client you want and add the desired capabilities properties to choose which browser version and OS you want to emulate:
```
 capabillities.setCapability("version", "5");
 capabillities.setCapability("platform", Platform.XP);
```
  * Add a name to your test:
```
 capabillities.setCapability("name", "Simple selenium test for labXX [" + System.currentTimeMillis() + "].");
```

### Configure a new job for Selenium tests ###

  * Create a new Job from the left menu with the job name **"3-labXX-TEST"** and tick "Build a Maven2/3 project".

  * On the "Code source management" part, choose "Git" and fill these parameters:
```
URL of repository=ssh://git@git.cloudbees.com/atelier-xebia/labXX.git
Branch Specifier (blank for default)=develop
```

  * On the **"Build triggers"** part, uncheck all selected options and tick **"Build after other projects are built"** and indicate your Integration deployment job:
```
Project names: 2-labXX-INTEG
```

  * On the "Build" part, choose Maven Version 3.0.3 and indicate the Maven goal, then save :
```
clean verify -Pselenium
```

  * Commit and push your changes (SeleniumSauceLabSetUp.java) and wait for your selenium's test running :
```
$ git commit -am "Selenium test"
$ git push origin develop
```

  * Go to Sauce Labs by clicking [Service/Saucelabs](https://grandcentral.cloudbees.com/services/goto/sauce_labs)
<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/saucelab-accueil.png' height='350' />
</p>
  * You can see that your tests was logged by clicking on [OnDemand/My Jobs](https://saucelabs.com/jobs)
<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/saucelab-tests.png' height='200' />
</p>

  * You can do a lot more with Sauce Labs with Scout, OnDemand and Connect. More info at https://saucelabs.com/account

<br />

---

<br />

## @RUN ##

### Configure a new MySQL database ###

  * Go back to the Home page in CloudBees and click on **Applications**.

  * **Create** a new MySQL database by clicking on the databases icon in the home page of the Cloudbees account. Then click on the add new database link. Fill with the following parameters :
```
Database name: labXX
Username: labXX
Password: labXX
```

<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/create-database-mysql.png' height='350' />
</p>

### Configure a new application with Elasticity, monitoring with New Relic and logs with Papertrail ###

  * On the left-hand side menu, click on **Applications / Add New application**.

  * **Create** a new application on **RUN@cloud** called "labXX".
<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/create-app-prod.png' height='300' />
</p>

  * Click on the **"Configure"** link under your application, and then on the **"Configuration"** tab.

  * Choose **"Starter"** plan and under **"Redundancy and Scale"**, select **"2 Instances"**.

  * Tick **"Enable New Relic RPM application monitoring"** and **"Enable Papertrail application logging"**.

  * Click on the "Save Changes" button at the top of the page.
<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/app-configuration.png' height='800' />
</p>

### Configure your application for Production environment ###

  * The sources provided for the lab are already configured for MySQL. The only thing you need to do is adapt your database info. Edit **src/main/resources/environment/prod/cloudbees.properties** (this file is used to filter **src/main/webapp/WEB-INF/cloudbees-web.xml** during the build):
```
cloudbees.datasource.username=labXX
cloudbees.datasource.password=labXX
cloudbees.datasource.url=jdbc:cloudbees://labXX
```

  * Commit and push your changes:
```
$ git commit -am "Configure production database"
$ git push origin develop
```

### Create a Jenkins job for deploying application on Production environment ###

  * Create a new Job from the left menu with the job name **"4-labXX-RUN"** and tick "Build a Maven2/3 project"

  * On the "Code source management" part choose "Git" and fill these parameters:
```
URL of repository=ssh://git@git.cloudbees.com/atelier-xebia/labXX.git
Branch Specifier (blank for default)=master
```

  * On the **"Build triggers"** part, tick only **"Build when a change is pushed to CloudBees Forge"**, which replaces the traditional Poll SCM option by a Push from the repository itself.

  * On the "Build" part, choose Maven Version 3.0.3 and indicate the Maven goal
```
clean package -Denvironment=prod
```

  * On the "Post build action" part tick **"Deploy to CloudBees"** option and fill parameters. If the application Id doesn't exist, it will be created.
```
Application Id=atelier-xebia/labXX
```

  * Save the configuration.

### Branch promotion with Git ###

  * Merge your **"develop"** branch to your **"master"** branch (for further information, take a look at the [Gitflow](http://nvie.com/posts/a-successful-git-branching-model/) model):
```
$ git checkout master
$ git merge develop --no-ff
$ git push origin master
```
<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/gitflow-promote-branch.png' height='116' />
</p>

  * The Production deployment job should be run automatically.

  * Once the job is finished, your application is available at this URL:
```
http://labXX.atelier-xebia.cloudbees.net
```

<br />

---

<br />

## @PERF ##

### Stress test with JMeter ###

  * Open the **pom.xml** file and edit **performance** profile to run [jmeter-maven-plugin](https://github.com/Ronnie76er/jmeter-maven-plugin/wiki)
```
<profile>
  <id>performance</id>
  <properties>
    <hostAddress>labXX.atelier-xebia.cloudbees.net</hostAddress>
    <hostPort>80</hostPort>
    <nbVirtualUsers>5</nbVirtualUsers>
    <nbLoopsCount>2</nbLoopsCount>
    <nbRampUp>5</nbRampUp>
  </properties>
  ...
</profile>
```

  * Commit and push your changes:
```
$ git commit -am "Configure stress tests"
$ git push origin master
```

  * Create a new Job from the left menu with the job name **"5-labXX-PERF"** and tick **"Build a Maven2/3 project"**.

  * On the **"Code source management"** part, choose **"Git"** and fill these parameters:
```
URL of repository=ssh://git@git.cloudbees.com/atelier-xebia/labXX.git
Branch Specifier (blank for default)=master
```

  * On the **"Build triggers"** part, uncheck all selected options.

  * On the "Build" part, choose Maven Version 3.0.3 and indicate the Maven goal
```
clean integration-test -Pperformance
```
  * If you want to play with Virtual Users, Ramp up and Loops add :
```
-DnbVirtualUsers=500 -DnbLoopsCount=10 -DnbRampUp=50
```

  * In Post-build Actions, check **"Publish Performance test result report"** and add Jmeter reports :
```
target/jmeter-reports/*.xml
```

<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/configure-performance-test-job-jenkins.png' height='200' />
</p>

  * Run the job manually.

  * Once complete, the reports can be accessed through the **"Performance Trend"** link on the job page:

<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/performance-jenkins-jmeter.png' height='250' />
</p>

<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/performance-jenkins-jmeter-trend.png' height='300' />
</p>

### Monitoring with New Relic ###

  * Click on the services link in the top bar.

  * Click on the **"New Relic"** icon. You can read a brief [presentation](http://wiki.cloudbees.com/bin/view/RUN/NewRelic) in the wiki.

  * Select AppServer view (maybe you must click on right button to see menu bar)
<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/newrelic-appserver-access.png' height='300' />
</p>

  * Verify elasticity by checking how many servers are running

<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/newrelic-performance.png' height='600' />
</p>

  * The view map is also very interesting for analysing application topology:
<p align='center'>
<img src='http://xebia-france.googlecode.com/svn/wiki/cloudbees-img/newrelic-appserver-map.png' height='220' />
</p>

<br />

---

<br />

## @SDK ##

### Installing the SDK ###

  * The SDK provides command-line tools to manage applications and databases.

  * On your CloudBees home page, click on the **[CloudBees SDK](http://wiki.cloudbees.com/bin/view/RUN/BeesSDK)** link then **Downloads** SDK. Follow the installation instructions for your platform.

### Getting information ###

  * List the applications available to the workshop's account:
```
$ bees app:list
```

  * You should see your production application (as well as those of the other teams):
```
Application                Status    URL                                    Instance(s)
...
atelier-xebia/labXX        active    labXX.atelier-xebia.cloudbees.net      2
```

  * To get more information, try the following command:
```
$ bees app:info -a atelier-xebia/labXX
```
```
# CloudBees SDK version: 0.7.3
Application     : atelier-xebia/labXX
Title           : labXX
Created         : Wed Nov 09 02:08:03 CET 2011
Status          : active
URL             : labXX.atelier-xebia.cloudbees.net
clusterSize     : 2
container       : java_tiny
idleTimeout     : -1
maxMemory       : 128
securityMode    : PUBLIC
serverPool      : stax-global
serverType      : tomcat
```


### Tailing the logs ###

  * Should you get nostalgic of the good old Unix **tail** command, the SDK provides a way to follow your logs in real-time from the command line:
```
$ bees app:tail -a atelier-xebia/labXX
```

  * Go back to the application in your browser and navigate through a few pages; the console logs should get updated.

### Creating and deploying a new application ###

  * In this section, we'll bootstrap an application from a CloudBees-provided template, and deploy it to the cloud from the command-line.

  * Run the following command (the new application's root directory will be created in the current directory):
```
$ cd ~/my_projects
$ bees create -a atelier-xebia/helloworldXX -p fr.xebia helloworldXX
```

  * Navigate to the newly created directory and examine the application structure. Edit **webapp/index.jsp** and locate for this text:
```
<h2 class="color text-xl">This application under development</h2>
```

  * Replace it with something more personal:
```
<h2 class="color text-xl">Team XX says: hello, world!</h2>
```

  * Launch your application locally by running this command from your root directory (NB: if port 8080 is already used on your machine, specify another with the **-p** option):
```
$ bees run
```

  * Navigate to http://localhost:8080 to view the home page with your personal message.

  * Now let's deploy the application to the cloud! Kill your local server and run the following command (again from the application's root directory):
```
$ bees deploy -a atelier-xebia/helloworldXX
```

  * In the CloudBees web administration console, go to the applications management page. Your new app should be available.
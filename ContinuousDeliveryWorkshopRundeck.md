<font size='5'>Continuous Delivery with Jenkins and rundeck Lab for Team '20'</font>



# Your architecture #

<img width='400' src='http://xebia-france.googlecode.com/svn/wiki/cont-delivery-img/per-team-infrastructure.png' />

<table>
<tr><td> <b>SSH Private key</b> </td><td> <a href='https://s3-eu-west-1.amazonaws.com/continuous-delivery/continuous-delivery-workshop.pem'>continuous-delivery-workshop.pem</a></td></tr>
<tr><td> <b>GitHub project repository url</b> </td><td> <a href='https://github.com/xebia-guest/xebia-petclinic-lite-20'>https://github.com/xebia-guest/xebia-petclinic-lite-20</a> </td></tr>
<tr><td> <b>Jenkins URL</b> </td><td> <a href='http://ec2-46-137-131-50.eu-west-1.compute.amazonaws.com:8080/'>http://ec2-46-137-131-50.eu-west-1.compute.amazonaws.com:8080/</a> </td></tr>
<tr><td> <b>Rundeck URL</b> </td><td> <a href='http://ec2-46-137-131-50.eu-west-1.compute.amazonaws.com:4440/'>http://ec2-46-137-131-50.eu-west-1.compute.amazonaws.com:4440/</a> </td></tr>
<tr><td> <b>Deployit URL</b> </td><td> <a href='http://ec2-46-137-131-50.eu-west-1.compute.amazonaws.com:4516/'>http://ec2-46-137-131-50.eu-west-1.compute.amazonaws.com:4516/</a> </td></tr>
<tr><td> <b>Nexus URL</b> </td><td> <a href='http://nexus.xebia-tech-event.info:8081/nexus/'>http://nexus.xebia-tech-event.info:8081/nexus/</a> </td></tr>
<tr><td> <b>Tomcat Dev URL</b> </td><td> <a href='http://ec2-46-51-156-159.eu-west-1.compute.amazonaws.com:8080/'>http://ec2-46-51-156-159.eu-west-1.compute.amazonaws.com:8080/</a> </td></tr>
<tr><td> <b>Tomcat Dev SSH</b> </td><td>
<pre><code>ssh -i ~/.aws/continuous-delivery-workshop.pem tomcat@ec2-46-51-156-159.eu-west-1.compute.amazonaws.com<br>
</code></pre>
<blockquote></td></tr>
<tr><td> <b>Tomcat Valid 1 URL</b> </td><td> <a href='http://ec2-46-51-160-62.eu-west-1.compute.amazonaws.com:8080/'>http://ec2-46-51-160-62.eu-west-1.compute.amazonaws.com:8080/</a> </td></tr>
<tr><td> <b>Tomcat Valid 1 SSH</b> </td><td>
<pre><code>ssh -i ~/.aws/continuous-delivery-workshop.pem tomcat@ec2-46-51-160-62.eu-west-1.compute.amazonaws.com<br>
</code></pre>
</td></tr>
<tr><td> <b>Tomcat Valid 2 URL</b> </td><td> <a href='http://ec2-46-137-63-51.eu-west-1.compute.amazonaws.com:8080/'>http://ec2-46-137-63-51.eu-west-1.compute.amazonaws.com:8080/</a> </td></tr>
<tr><td> <b>Tomcat Valid 2 SSH</b> </td><td>
<pre><code>ssh -i ~/.aws/continuous-delivery-workshop.pem tomcat@ec2-46-137-63-51.eu-west-1.compute.amazonaws.com<br>
</code></pre>
</td></tr>
</table></blockquote>


---


# Environment setup #

  1. Get the SSH private key [continuous-delivery-workshop.pem](https://s3-eu-west-1.amazonaws.com/continuous-delivery/continuous-delivery-workshop.pem) to connect to the servers
```
mkdir ~/.aws/
curl https://s3-eu-west-1.amazonaws.com/continuous-delivery/continuous-delivery-workshop.pem --output ~/.aws/continuous-delivery-workshop.pem
chmod 400 ~/.aws/continuous-delivery-workshop.pem
```
  1. Clone Github repository xebia-petclinic-lite-20
```
mkdir ~/continuous-delivery-workshop
cd ~/continuous-delivery-workshop
git clone https://xebia-guest@github.com/xebia-guest/xebia-petclinic-lite-20.git
```
> > Note: password of the "xebia-guest" user will be sent before the workshop.
  1. build project
```
cd ~/continuous-delivery-workshop/xebia-petclinic-lite-20
mvn package
```
  1. Modify the welcome page and push the change
    1. Modify `~/continuous-delivery-workshop/xebia-petclinic-lite-20/src/main/webapp/welcome.jsp`
    1. Push the change
```
xebia-petclinic-lite-20 > git commit -m "test" src/main/webapp/welcome.jsp
xebia-petclinic-lite-20 > git push
```
  1. Verify that Jenkins detects the Git change and triggers a build : http://ec2-46-137-131-50.eu-west-1.compute.amazonaws.com:8080//job/xebia-petclinic-lite-20/ (it may take up to 1 minute).
  1. Do a release of the application
    1. Add the Nexus credentials to your Maven settings.xml file :
```
<settings>
  <servers>
    <!-- Xebia Workshop -->
    <server>
      <id>xebia-tech-event-nexus-releases</id>
      <username>deployment</username>
      <password>deployment123</password>
    </server>
    <server>
      <id>xebia-tech-event-nexus-snapshots</id>
      <username>deployment</username>
      <password>deployment123</password>
    </server>
  </servers>
</settings>
```
    1. Execute `mvn release:prepare -B release:perform`
  1. Verify that the new release is available in Nexus : http://nexus.xebia-tech-event.info:8081/nexus/content/groups/public/fr/xebia/demo/petclinic-20/xebia-petclinic-lite/

---


Links to the different labs :
  * [ContinuousDeliveryWorkshopLab\_20\_apache\_tomcat\_maven\_plugin](ContinuousDeliveryWorkshopLab_20_apache_tomcat_maven_plugin.md)
  * [ContinuousDeliveryWorkshopLab\_20\_jenkins\_remote\_ssh](ContinuousDeliveryWorkshopLab_20_jenkins_remote_ssh.md)
  * [ContinuousDeliveryWorkshopLab\_20\_rundeck](ContinuousDeliveryWorkshopLab_20_rundeck.md)
  * [ContinuousDeliveryWorkshopLab\_20\_deployit](ContinuousDeliveryWorkshopLab_20_deployit.md)

_This page has been generaterd by '`class fr.xebia.workshop.continuousdelivery.ContinuousDeliveryInfrastructureCreator`' with template '`/fr/xebia/workshop/continuousdelivery/lab/setup.fmt`' on the 2011-10-20T13:15:47.273+02:00_
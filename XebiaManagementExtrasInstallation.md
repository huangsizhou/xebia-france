# How to integrate the `xebia-management-extras` library in your project #

There are different ways to integrate these features in your project:
  * Maven integration :
```
<project ...>
   <dependencies>
      <dependency>
         <groupId>fr.xebia.management</groupId>
         <artifactId>xebia-management-extras</artifactId>
         <version>1.2.1</version>
      </dependency>
      ...
   </dependencies>
   ...
</project>
```
> > The artifact is available on [Maven Central Repository](http://repo1.maven.org/maven2/), no special `<repository />` declaration is needed in your `pom.xml` file.
  * Download the jar [xebia-management-extras-1.2.1.jar](http://xebia-france.googlecode.com/files/xebia-management-extras-1.2.1.jar) ([sources](http://xebia-france.googlecode.com/files/xebia-management-extras-1.2.1-sources.jar)),
  * Get the source from svn, modify it if needed and add it to your project. The source is available under the Open Source licence [Apache Software Licence 2](http://www.apache.org/licenses/LICENSE-2.0) at http://xebia-france.googlecode.com/svn/management/xebia-management-extras/trunk/ .
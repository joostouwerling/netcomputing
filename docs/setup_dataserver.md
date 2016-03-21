# Ubuntu 14.04 setup

# DataServer

```
apt-get install default-jre
apt-get install default-jdk
apt-get install git
apt-get install mysql-server
sudo apt-get install maven
```

Now, set the right environment variables for MySQL (MYSQL_SERVER, MYSQL_DATABASE, MYSQL_USERNAME, MYSQL_PASSWORD)
```
nano /etc/environment
```

Execute the required SQL statements to build the database
```
mysql -u<user> -p<password> < dataserver.sql
```

Clone the code from the repository & cd into the right directory
```
git clone https://github.com/joostouwerling/netcomputing.git
cd netcomputing/project/dataserver
```

Add the local jars to the maven project
```
mvn install:install-file -Dfile=/root/netcomputing/project/common/libs/com-sportperformancemanagement-common.jar -DgroupId=eu.sportperformancemanagement -DartifactId=common -Dversion=1 -Dpackaging=jar
mvn install:install-file -Dfile=/root/netcomputing/project/common/libs/json-20160212.jar -DgroupId=eu.sportperformancemanagement -DartifactId=json -Dversion=1 -Dpackaging=jar
mvn install:install-file -Dfile=/root/netcomputing/project/common/libs/mysql-connector-java-5.0.8-bin.jar -DgroupId=eu.sportperformancemanagement -DartifactId=mysql -Dversion=1 -Dpackaging=jar
```

Add the local repository items to pom.xml file:
```
<dependency>
  <groupId>eu.sportperformancemanagement</groupId>
  <artifactId>common</artifactId>
  <version>1</version>
</dependency>
<dependency>
  <groupId>eu.sportperformancemanagement</groupId>
  <artifactId>json</artifactId>
  <version>1</version>
</dependency>
<dependency>
  <groupId>eu.sportperformancemanagement</groupId>
  <artifactId>mysql</artifactId>
  <version>1</version>
</dependency>
```

Run maven compile and start the app:
```
mvn:compile
mvn exec:java -Dexec.mainClass=".sportperformancemanagement.dataserver.DataServer"
```

# Tomcat server

* https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-8-on-ubuntu-14-04
* COmpile with mvn compile and upload with through manager console

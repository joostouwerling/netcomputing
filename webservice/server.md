# Ubuntu 14.04 setup

# WebService
```
apt-get update
apt-get install default-jre
apt-get install default-jdk
apt-get install git
apt-get install mysql-server
apt-get install maven
```

Clone the code from the repository & cd into the right directory
```
git clone https://github.com/joostouwerling/netcomputing.git
```

First, we need to install the common code on which Data Server depends:
```
cd netcomputing/common/Common
mvn install
```

CD into the DataServer and install the MYSQL
```
cd ../../webservice
mysql -u<user> -p<password> < dataserver.sql
```

Install tomcat, with tutorial from https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-8-on-ubuntu-14-04.

```
cd ~
groupadd tomcat
useradd -s /bin/false -g tomcat -d /opt/tomcat tomcat
wget http://mirror.sdunix.com/apache/tomcat/tomcat-8/v8.0.32/bin/apache-tomcat-8.0.32.tar.gz
mkdir /opt/tomcat
tar xvf apache-tomcat-8*tar.gz -C /opt/tomcat --strip-components=1
cd /opt/tomcat
chgrp -R tomcat conf
chmod g+rwx conf
chmod g+r conf/*
chown -R tomcat work/ temp/ logs/ webapps/
```

Add the following to /opt/tomcat/bin/setenv.sh
```
export MYSQL_SERVER=localhost:3306
export MYSQL_DATABASE=spm
export MYSQL_USERNAME=<user>
export MYSQL_PASSWORD=<pass>
```

Now, create the start script:
```
nano /etc/init/tomcat.conf
```

and add the following contents

```
description "Tomcat Server"

  start on runlevel [2345]
  stop on runlevel [!2345]
  respawn
  respawn limit 10 5

  setuid tomcat
  setgid tomcat

  env JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64/jre
  env CATALINA_HOME=/opt/tomcat

  # Modify these options as needed
  env JAVA_OPTS="-Djava.awt.headless=true -Djava.security.egd=file:/dev/./urandom"
  env CATALINA_OPTS="-Xms512M -Xmx1024M -server -XX:+UseParallelGC"

  exec $CATALINA_HOME/bin/catalina.sh run

  # cleanup temp directory after stop
  post-stop script
    rm -rf $CATALINA_HOME/temp/*
  end script
```

Now, start Tomcat:
```
initctl reload-configuration
initctl start tomcat
```
Check on http://SERVER_IP:8080 if Tomcat is running fine.

Put the following content in /opt/tomcat/conf/tomcat-users.xml:
```
<?xml version='1.0' encoding='utf-8'?>
<tomcat-users xmlns="http://tomcat.apache.org/xml"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://tomcat.apache.org/xml tomcat-users.xsd"
              version="1.0">

<role rolename="manager-gui"/>
<user username="<username>" password="<password>" roles="manager-gui"/>
</tomcat-users>
```

Add the following lines in /opt/tomcat/conf/web.xml
```
<filter>
  <filter-name>CorsFilter</filter-name>
  <filter-class>org.apache.catalina.filters.CorsFilter</filter-class>
  <async-supported>true</async-supported>
</filter>
<filter-mapping>
  <filter-name>CorsFilter</filter-name>
  <url-pattern>/*</url-pattern>
</filter-mapping>
```

Restart Tomcat:
```
initctl restart tomcat
```

Now, you can generate a WAR file (can be done locally) using `mvn install` and load it up through the web manager. Or copy WAR file into /opt/tomcat/webapps/
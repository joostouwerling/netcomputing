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
```

First, we need to install the common code on which Data Server depends:
```
cd netcomputing/common/Common
mvn install
```

Now, we can run the data server as follows:
```
cd ../../dataserver/DataServer
mvn install
mvn exec:java -Dexec.mainClass=".sportperformancemanagement.dataserver.DataServer"
```

# Tomcat server

* https://www.digitalocean.com/community/tutorials/how-to-install-apache-tomcat-8-on-ubuntu-14-04
* COmpile with mvn compile and upload with through manager console

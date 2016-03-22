# Ubuntu 14.04 setup

# DataServer

```
apt-get update
apt-get install default-jre
apt-get install default-jdk
apt-get install git
apt-get install mysql-server
apt-get install maven
```

Now, set the right environment variables for MySQL (MYSQL_SERVER, MYSQL_DATABASE, MYSQL_USERNAME, MYSQL_PASSWORD)
```
nano /etc/environment
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
cd ../../dataserver
mysql -u<user> -p<password> < dataserver.sql
```


Now, we can run the data server as follows:
```
cd DataServer
mvn install
mvn exec:java -Dexec.mainClass="eu.sportperformancemanagement.dataserver.DataServer"
```

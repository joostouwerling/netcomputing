# Ubuntu 14.04 setup

# Rabbit MQ server
```
apt-get update
```

Add the Rabbit MQ repository and install
```
echo "deb http://www.rabbitmq.com/debian/ testing main" >> /etc/apt/sources.list
curl http://www.rabbitmq.com/rabbitmq-signing-key-public.asc | sudo apt-key add -
apt-get update
apt-get install rabbitmq-server
```

Enable the management console and make a sure for connecting to the queue
```
rabbitmq-plugins enable rabbitmq_management
rabbitmqctl add_user <user> <pwd>
rabbitmqctl set_user_tags <user> administrator
rabbitmqctl set_permissions -p / <user> ".*" ".*" ".*"
```
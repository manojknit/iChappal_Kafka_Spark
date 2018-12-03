# iChappal_Kafka_Spark

## Db Structure
* Steps-detail - user ID, date, time, step
* Total_steps - user id, date, totalsteps

## Kafka Commands 
Start
#cd /opt/Kafka/kafka_2.10-0.10.0.1/
sudo  /opt/Kafka/kafka_2.10-0.10.0.1/bin/kafka-server-start.sh /opt/Kafka/kafka_2.10-0.10.0.1/config/server.properties

Stop
sudo /opt/Kafka/kafka_2.10-0.10.0.1/bin/kafka-server-stop.sh

Create Topic
sudo /opt/Kafka/kafka_2.10-0.10.0.1/bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1  --partitions 1 --topic testing

Zookeeper to List Topic 
sudo /opt/Kafka/kafka_2.10-0.10.0.1/bin/kafka-topics.sh --list --zookeeper localhost:2181

Producer
sudo /opt/Kafka/kafka_2.10-0.10.0.1/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic testing

Consumer
sudo /opt/Kafka/kafka_2.10-0.10.0.1/bin/kafka-console-consumer.sh --zookeeper localhost:2181 --topic testing --from-beginning

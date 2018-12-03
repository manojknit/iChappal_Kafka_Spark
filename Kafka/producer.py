#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Dec  1 11:49:56 2018

@author: Manoj Kumar
"""
# https://kafka.apache.org/quickstart

# $ python sender.py --host localhost --port 8000 --project-id my-project --topic-id my-topic "This is a message for my-topic on my-project."
# https://blog.appscale.com/run-pub/sub-apps-anywhere-using-googles-open-source-tool
#$ python producer.py --host 35.238.95.100 --port 8000 --project-id	9038565789643270526 --topic-id testtopic "This is a message for my-topic on my-project."

# install google cloud as pre req
# pip install --upgrade google-cloud-pubsub
#pip install kafka-python


# sender.py

from time import sleep
from json import dumps
from kafka import KafkaProducer

if __name__ == '__main__':

    _producer = None
    try:
        _producer = KafkaProducer(bootstrap_servers=['X.X.X.X:9092'], api_version=(0, 10))
        
        key = "4"
        value = "my first test4"
        key_bytes = bytes(key, encoding='utf-8')
        value_bytes = bytes(value, encoding='utf-8')
        _producer.send('testing', key=key_bytes, value=value_bytes)
        _producer.flush()
        _producer.close()
    except Exception as ex:
        print('Exception while connecting Kafka')
        print(str(ex))
        
        

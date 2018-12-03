#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Dec  1 11:53:16 2018

@author:  Manoj Kumar
"""
# $ python receiver.py --host localhost --port 8000 --project-id my-project --subscription-id subscription-to-my-topic
# https://blog.appscale.com/run-pub/sub-apps-anywhere-using-googles-open-source-tool
# https://towardsdatascience.com/getting-started-with-apache-kafka-in-python-604b3250aa05
# https://www.programcreek.com/python/example/98440/kafka.KafkaConsumer


# receiver.py
import time
from kafka import KafkaConsumer
from pymongo import MongoClient
from json import loads

    
if __name__ == '__main__':

    topic_name = 'testing'
    consumer = KafkaConsumer(topic_name, auto_offset_reset='earliest',
                             bootstrap_servers=['X.X.X.X:9092'], api_version=(0, 10), consumer_timeout_ms=1000)

    for message in consumer:
        message = message.value
        print("= "+message.decode('utf8'))

    consumer.close()

    '''
    client = MongoClient('mongodb://quoteuser:xxx@xx.mlab.com:11374/kafkadb')
    collection = client.numtest.numtest
    
    for message in consumer:
        message = message.value
        print(" "+message)
        collection.insert_one(message)
        print('{} added to {}'.format(message, collection))
        
        #and
        consumer.subscribe(topic_name)

    counter = 0
    nbr_msg = 10
    for message in consumer:
        print("= "+ message.value.decode('utf8'))
        counter = counter + 1
        if counter == nbr_msg:
            break
    '''


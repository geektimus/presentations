package com.codingmaniacs.bigdata.kafka.eventproducer.controllers;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Properties;
import java.util.Random;

@RestController
@RequestMapping("/producer")
public class ProducerController {
	@RequestMapping(method = RequestMethod.GET)
	public String producer(@RequestParam(value = "events") long events, @RequestParam("topic") String topic){
		String brokers = "kafka:9092";
		Random rnd = new Random();

		Properties props = new Properties();
		props.put("bootstrap.servers", brokers);
		props.put("client.id", "ProducerExample");
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

		KafkaProducer<String, String> producer = new KafkaProducer<>(props);

		long t = System.currentTimeMillis();
		for (long nEvents = 0; nEvents < events; nEvents++) {
			long runtime = new Date().getTime();
			String ip = "192.168.2." + rnd.nextInt(255);
			String msg = runtime + "," + nEvents + ",www.example.com," + ip;
			try {
				//async
				producer.send(new ProducerRecord<>(topic, ip, msg), (recordMetadata, e) ->{});
				//sync

				producer.send(new ProducerRecord<>(topic, ip, msg)).get();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

		System.out.println("sent per second: " + events * 1000 / (System.currentTimeMillis() - t));
		producer.close();

		return "Success";
	}
}

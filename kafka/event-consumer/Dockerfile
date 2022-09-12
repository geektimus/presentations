FROM anapsix/alpine-java
COPY target/event-consumer-0.0.1-SNAPSHOT.jar /home/app.jar
CMD ["java","-cp","/home/app.jar", "com.codingmaniacs.bigdata.kafka.eventconsumer.Consumer", "kafka:9092", "group1", "metrics","10", "0"]
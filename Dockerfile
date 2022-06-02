FROM openjdk:11
ADD wait-for-it.sh /opt
ADD start.sh /opt
ADD target/stock-0.0.1-SNAPSHOT.jar /opt
WORKDIR /opt
RUN chmod +x wait-for-it.sh
RUN chmod +x start.sh
EXPOSE 8090
ENTRYPOINT ["sh", "-c", "./start.sh"]
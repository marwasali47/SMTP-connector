FROM dockerproxy-iva.si.francetelecom.fr/openjdk:8-slim


ARG JAR_FILE


ENV ACTIVE_PROFILE ""

EXPOSE 8083

ADD ${JAR_FILE} /opt/hubme/orange-connector-service.jar

WORKDIR /opt/hubme

RUN chown -R 1003540000:1003540000 /opt/hubme

USER 1003540000

ENTRYPOINT ["sh","-c","java -Dspring.profiles.active=${ACTIVE_PROFILE} \
                            -jar orange-connector-service.jar"]

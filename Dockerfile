FROM tomcat:10.1.11-jdk17-temurin-jammy AS builder

WORKDIR /app

COPY .mvn .mvn
COPY mvnw pom.xml ./
COPY src ./src

RUN ./mvnw package
RUN mkdir ./target/ROOT \
	&& cd ./target/ROOT \
	&& jar -xvf ../ROOT.war
RUN mv ./target/ROOT /usr/local/tomcat/webapps/ROOT
CMD ["catalina.sh", "run"]

FROM tomcat:10.1.11-jdk17-temurin-jammy AS production
COPY --from=builder /usr/local/tomcat/webapps/ROOT /usr/local/tomcat/webapps/ROOT

EXPOSE 80
CMD ["catalina.sh", "run"]
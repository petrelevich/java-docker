FROM bellsoft/liberica-openjdk-centos:11.0.3
ENV PROJECT_ROOT /usr/src/app
WORKDIR $PROJECT_ROOT
COPY ./testJavaAppl.jar testJavaAppl.jar
CMD java -XX:+UseContainerSupport -Xmx10g -Xms10g -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=1026 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -jar testJavaAppl.jar


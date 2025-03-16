# netty-otel-train

## OTelのAuto Instrumentationを用いる
```
wget https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.14.0/opentelemetry-javaagent.jar

export JAVA_TOOL_OPTIONS="-javaagent:./opentelemetry-javaagent.jar" \
  OTEL_TRACES_EXPORTER=logging \
  OTEL_METRICS_EXPORTER=logging \
  OTEL_LOGS_EXPORTER=logging \
  OTEL_METRIC_EXPORT_INTERVAL=15000

mvn clean package && java -jar target/netty-app-1.0-SNAPSHOT.jar
```

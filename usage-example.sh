#!/usr/bin/env bash


for kv in "v1.10.1" "v1.10.2" "v1.9.1" "v1.9.2" "v1.9.3" "v1.9.4" "v1.8.3" "master"; do
mkdir -p kyuubi/$kv
java -jar target/kyuubi-config.jar kyuubi -ver $kv --format json               > kyuubi/$kv/kyuubi-config.json
java -jar target/kyuubi-config.jar kyuubi -ver $kv --format json --no-section  > kyuubi/$kv/kyuubi-config-no-section.json
java -jar target/kyuubi-config.jar kyuubi -ver $kv --format conf               > kyuubi/$kv/kyuubi-config.conf
done

for kv in "3.5.4" "3.5.5" "3.5.6" "4.0.0" "latest"; do
echo "version=$kv"
mkdir -p spark/$kv
java -jar target/kyuubi-config.jar spark -ver $kv --format json               > spark/$kv/spark-config.json
java -jar target/kyuubi-config.jar spark -ver $kv --format conf               > spark/$kv/spark-config.conf
done

for kv in "1.13" "1.14" "1.15" "1.16" "1.17" "1.18" "1.19" "1.20" "2.0" "master"; do
echo "version=$kv"
mkdir -p flink/$kv
java -jar target/kyuubi-config.jar flink -ver $kv --format json               > flink/$kv/flink-config.json
java -jar target/kyuubi-config.jar flink -ver $kv --format conf               > flink/$kv/flink-config.conf
done

for kv in "36" "37" "38" "39" "master"; do
echo "version=$kv"
mkdir -p kafka/$kv
java -jar target/kyuubi-config.jar kafka -ver $kv --format json               > kafka/$kv/kafka-config.json
java -jar target/kyuubi-config.jar kafka -ver $kv --format json --no-section  > kafka/$kv/kafka-config-no-section.json
java -jar target/kyuubi-config.jar kafka -ver $kv --format conf               > kafka/$kv/kafka-config.conf
done

for kv in "0.8.0" "latest"; do
echo "version=$kv"
mkdir -p amoro/$kv
java -jar target/kyuubi-config.jar amoro -ver $kv --format json --no-section  > amoro/$kv/amoro-config-no-section.json
java -jar target/kyuubi-config.jar amoro -ver $kv --format conf               > amoro/$kv/amoro-config.conf
done


for kv in "4.16.7" "next"; do
echo "version=$kv"
mkdir -p bookkeeper/$kv
java -jar target/kyuubi-config.jar bookkeeper -ver $kv --format json --no-section  > bookkeeper/$kv/bookkeeper-config-no-section.json
java -jar target/kyuubi-config.jar bookkeeper -ver $kv --format conf               > bookkeeper/$kv/bookkeeper-config.conf
done

for kv in "2.0" "3.0" "dev"; do
  echo "version=$kv";
  mkdir -p doris/$kv;
  java -jar target/kyuubi-config.jar doris -ver $kv --format json --type fe --no-section  > doris/$kv/doris-fe-config-no-section.json;
  java -jar target/kyuubi-config.jar doris -ver $kv --format conf --type fe               > doris/$kv/doris-fe-config.conf;
  java -jar target/kyuubi-config.jar doris -ver $kv --format json --type be --no-section  > doris/$kv/doris-be-config-no-section.json;
  java -jar target/kyuubi-config.jar doris -ver $kv --format conf --type be               > doris/$kv/doris-be-config.conf;
done

for kv in "0.5" "0.6" "current" "next" ; do
  echo "version=$kv";
  mkdir -p fluss/$kv;
  java -jar target/kyuubi-config.jar fluss -ver $kv --format json --no-section  > fluss/$kv/fluss-config-no-section.json;
  java -jar target/kyuubi-config.jar fluss -ver $kv --format conf               > fluss/$kv/fluss-config.conf;
done


for kv in "0.9.0-incubating" "0.9.1" ; do
  echo "version=$kv";
  mkdir -p gravitino/$kv;
  java -jar target/kyuubi-config.jar gravitino -ver $kv --format json --no-section  > gravitino/$kv/gravitino-config-no-section.json;
  java -jar target/kyuubi-config.jar gravitino -ver $kv --format conf               > gravitino/$kv/gravitino-config.conf;

  java -jar target/kyuubi-config.jar gravitino -ver $kv --format json --no-section iceberg-rest > gravitino/$kv/gravitino-iceberg-rest-no-section.json;
  java -jar target/kyuubi-config.jar gravitino -ver $kv --format conf              iceberg-rest > gravitino/$kv/gravitino-iceberg-rest.conf;

done

for kv in "3.4" "current"; do
  echo "version=$kv";
  mkdir -p spring-boot/$kv;
  java -jar target/kyuubi-config.jar spring-boot -ver $kv --format json               > spring-boot/$kv/spring-boot-config.json;
  java -jar target/kyuubi-config.jar spring-boot -ver $kv --format json --no-section  > spring-boot/$kv/spring-boot-config-no-section.json;
  java -jar target/kyuubi-config.jar spring-boot -ver $kv --format conf               > spring-boot/$kv/spring-boot-config.conf;
done


for kv in "3.3" "stable"; do
  echo "version=$kv";
  mkdir -p debezium/$kv;
  java -jar target/kyuubi-config.jar debezium -ver $kv --format json --no-section  > debezium/$kv/debezium-mysql-no-section.json;
  java -jar target/kyuubi-config.jar debezium -ver $kv --format conf               > debezium/$kv/debezium-mysql.conf;
  java -jar target/kyuubi-config.jar debezium -ver $kv --format json --no-section -t jdbc  > debezium/$kv/debezium-jdbc-no-section.json;
  java -jar target/kyuubi-config.jar debezium -ver $kv --format conf              -t jdbc  > debezium/$kv/debezium-jdbc.conf;
  java -jar target/kyuubi-config.jar debezium -ver $kv --format json --no-section -t postgresql  > debezium/$kv/debezium-postgresql-no-section.json;
  java -jar target/kyuubi-config.jar debezium -ver $kv --format conf              -t postgresql  > debezium/$kv/debezium-postgresql.conf;
done

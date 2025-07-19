#!/usr/bin/env bash


for kv in "v1.10.1" "v1.10.2" "v1.9.1" "v1.9.2" "v1.9.3" "v1.9.4" "v1.8.3" "master"; do
mkdir -p kyuubi/$kv
java -jar target/kyuubi-config.jar config -ver $kv --format json               > kyuubi/$kv/kyuubi-config.json
java -jar target/kyuubi-config.jar config -ver $kv --format json --no-section  > kyuubi/$kv/kyuubi-config-no-section.json
java -jar target/kyuubi-config.jar config -ver $kv --format conf               > kyuubi/$kv/kyuubi-config.conf
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




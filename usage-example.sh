#!/usr/bin/env bash

# Optional proxy support:
#   ./usage-example.sh --proxy 127.0.0.1:8118
#   ./usage-example.sh --proxy 127.0.0.1:1080 --proxy-type SOCKS
# Or via environment variables:
#   PROXY=127.0.0.1:8118 ./usage-example.sh
#   PROXY=127.0.0.1:1080 PROXY_TYPE=SOCKS ./usage-example.sh
#
# Atomic writes: each command writes to a .tmp file first; the destination
# is only replaced via `mv` when the java command exits successfully.
# A failed run (network error, proxy down, etc.) leaves the existing
# file untouched.

PROXY="${PROXY:-}"
PROXY_TYPE="${PROXY_TYPE:-HTTP}"
PROXY_ARGS=""
PROXY_TYPE_ARGS=""

while [[ $# -gt 0 ]]; do
    case "$1" in
        --proxy|-P)
            [[ $# -ge 2 ]] || { echo "ERROR: $1 requires host:port argument" >&2; exit 1; }
            PROXY="$2"
            shift 2
            ;;
        --proxy=*)
            PROXY="${1#*=}"
            shift
            ;;
        -P=*)
            PROXY="${1#*=}"
            shift
            ;;
        --proxy-type|-pt)
            [[ $# -ge 2 ]] || { echo "ERROR: $1 requires HTTP or SOCKS" >&2; exit 1; }
            PROXY_TYPE="$2"
            shift 2
            ;;
        --proxy-type=*|-pt=*)
            PROXY_TYPE="${1#*=}"
            shift
            ;;
        -h|--help)
            echo "Usage: $0 [--proxy host:port] [--proxy-type HTTP|SOCKS]"
            echo ""
            echo "Environment variables (override defaults; CLI flags take precedence):"
            echo "  PROXY       proxy host:port, e.g. 127.0.0.1:8118"
            echo "  PROXY_TYPE  HTTP (default) or SOCKS"
            exit 0
            ;;
        *)
            echo "ERROR: unknown option: $1" >&2
            echo "Usage: $0 [--proxy host:port] [--proxy-type HTTP|SOCKS]" >&2
            exit 1
            ;;
    esac
done

if [[ -n "$PROXY" ]]; then
    PROXY_ARGS="-P $PROXY"
    PROXY_TYPE_ARGS="-pt $PROXY_TYPE"
    echo "Using proxy: $PROXY (type: $PROXY_TYPE)"
fi

# Atomic write: $1 = output file, $@ = command + args
# Writes to "$1.tmp"; only moves into place if the command exits 0
# AND produced non-empty output. Guards against "fake success" cases
# where the command exits 0 but stdout is empty (e.g. [] from a
# Command that found no data and silently produced an empty array).
# Stops the script on failure (set -e) so the next iteration doesn't
# silently succeed on missing data.
atomic_write() {
    local out="$1"; shift
    local tmp="${out}.tmp"
    if "$@" > "$tmp"; then
        local size
        size=$(wc -c < "$tmp" | tr -d ' ')
        # strip whitespace to detect "[]" / "{}" / "{}" style empties
        local stripped
        stripped=$(tr -d '[:space:]' < "$tmp")
        if [[ "$size" -eq 0 || "$stripped" == "[]" || "$stripped" == "{}" ]]; then
            rm -f "$tmp"
            echo "FAIL $out (empty output, kept existing file)" >&2
            return 1
        fi
        mv -f "$tmp" "$out"
        echo "OK   $out"
    else
        local rc=$?
        rm -f "$tmp"
        echo "FAIL $out (exit=$rc)" >&2
        return $rc
    fi
}

set -e

kv="v1.10.3"
mkdir -p kyuubi/$kv
atomic_write kyuubi/$kv/kyuubi-config.json              java -jar target/kyuubi-config.jar kyuubi -ver $kv --format json $PROXY_ARGS $PROXY_TYPE_ARGS
atomic_write kyuubi/$kv/kyuubi-config-no-section.json   java -jar target/kyuubi-config.jar kyuubi -ver $kv --format json --no-section $PROXY_ARGS $PROXY_TYPE_ARGS
atomic_write kyuubi/$kv/kyuubi-config.conf              java -jar target/kyuubi-config.jar kyuubi -ver $kv --format conf $PROXY_ARGS $PROXY_TYPE_ARGS


for kv in "v1.10.1" "v1.10.2" "v1.11.0" "v1.9.1" "v1.9.2" "v1.9.3" "v1.9.4" "v1.8.3" "master"; do
mkdir -p kyuubi/$kv
atomic_write kyuubi/$kv/kyuubi-config.json              java -jar target/kyuubi-config.jar kyuubi -ver $kv --format json $PROXY_ARGS $PROXY_TYPE_ARGS
atomic_write kyuubi/$kv/kyuubi-config-no-section.json   java -jar target/kyuubi-config.jar kyuubi -ver $kv --format json --no-section $PROXY_ARGS $PROXY_TYPE_ARGS
atomic_write kyuubi/$kv/kyuubi-config.conf              java -jar target/kyuubi-config.jar kyuubi -ver $kv --format conf $PROXY_ARGS $PROXY_TYPE_ARGS
done

for kv in "3.5.4" "3.5.5" "3.5.6" "4.0.0" "latest"; do
echo "version=$kv"
mkdir -p spark/$kv
atomic_write spark/$kv/spark-config.json   java -jar target/kyuubi-config.jar spark -ver $kv --format json $PROXY_ARGS $PROXY_TYPE_ARGS
atomic_write spark/$kv/spark-config.conf   java -jar target/kyuubi-config.jar spark -ver $kv --format conf $PROXY_ARGS $PROXY_TYPE_ARGS
done

for kv in "1.13" "1.14" "1.15" "1.16" "1.17" "1.18" "1.19" "1.20" "2.0" "2.1" "master"; do
echo "version=$kv"
mkdir -p flink/$kv
atomic_write flink/$kv/flink-config.json   java -jar target/kyuubi-config.jar flink -ver $kv --format json $PROXY_ARGS $PROXY_TYPE_ARGS
atomic_write flink/$kv/flink-config.conf   java -jar target/kyuubi-config.jar flink -ver $kv --format conf $PROXY_ARGS $PROXY_TYPE_ARGS
done

for kv in "36" "37" "38" "39" "master"; do
echo "version=$kv"
mkdir -p kafka/$kv
atomic_write kafka/$kv/kafka-config.json              java -jar target/kyuubi-config.jar kafka -ver $kv --format json $PROXY_ARGS $PROXY_TYPE_ARGS
atomic_write kafka/$kv/kafka-config-no-section.json   java -jar target/kyuubi-config.jar kafka -ver $kv --format json --no-section $PROXY_ARGS $PROXY_TYPE_ARGS
atomic_write kafka/$kv/kafka-config.conf              java -jar target/kyuubi-config.jar kafka -ver $kv --format conf $PROXY_ARGS $PROXY_TYPE_ARGS
done

for kv in "0.8.0" "latest"; do
echo "version=$kv"
mkdir -p amoro/$kv
atomic_write amoro/$kv/amoro-config-no-section.json   java -jar target/kyuubi-config.jar amoro -ver $kv --format json --no-section $PROXY_ARGS $PROXY_TYPE_ARGS
atomic_write amoro/$kv/amoro-config.conf              java -jar target/kyuubi-config.jar amoro -ver $kv --format conf $PROXY_ARGS $PROXY_TYPE_ARGS
done


for kv in "4.16.7" "next"; do
echo "version=$kv"
mkdir -p bookkeeper/$kv
atomic_write bookkeeper/$kv/bookkeeper-config-no-section.json   java -jar target/kyuubi-config.jar bookkeeper -ver $kv --format json --no-section $PROXY_ARGS $PROXY_TYPE_ARGS
atomic_write bookkeeper/$kv/bookkeeper-config.conf              java -jar target/kyuubi-config.jar bookkeeper -ver $kv --format conf $PROXY_ARGS $PROXY_TYPE_ARGS
done

# doris 2.0 / 3.0 docs URLs are permanently 404 on doris.apache.org;
# only 2.1 and dev (latest) are reachable. The doris/2.0/ snapshot is
# kept as a historical archive and is not regenerated by this script.
for kv in "2.1" "dev"; do
  echo "version=$kv";
  mkdir -p doris/$kv;
  atomic_write doris/$kv/doris-fe-config-no-section.json   java -jar target/kyuubi-config.jar doris -ver $kv --format json --type fe --no-section $PROXY_ARGS $PROXY_TYPE_ARGS
  atomic_write doris/$kv/doris-fe-config.conf              java -jar target/kyuubi-config.jar doris -ver $kv --format conf --type fe $PROXY_ARGS $PROXY_TYPE_ARGS
  atomic_write doris/$kv/doris-be-config-no-section.json   java -jar target/kyuubi-config.jar doris -ver $kv --format json --type be --no-section $PROXY_ARGS $PROXY_TYPE_ARGS
  atomic_write doris/$kv/doris-be-config.conf              java -jar target/kyuubi-config.jar doris -ver $kv --format conf --type be $PROXY_ARGS $PROXY_TYPE_ARGS
done

for kv in "0.5" "0.6" "current" "next" ; do
  echo "version=$kv";
  mkdir -p fluss/$kv;
  atomic_write fluss/$kv/fluss-config-no-section.json   java -jar target/kyuubi-config.jar fluss -ver $kv --format json --no-section $PROXY_ARGS $PROXY_TYPE_ARGS
  atomic_write fluss/$kv/fluss-config.conf              java -jar target/kyuubi-config.jar fluss -ver $kv --format conf $PROXY_ARGS $PROXY_TYPE_ARGS
done

# for kv in "0.9.0-incubating" "0.9.1" "1.0.0" "1.1.0" "1.2.0"; do
for kv in "1.1.0" "1.2.0"; do
  echo "version=$kv";
  mkdir -p gravitino/$kv;
  atomic_write gravitino/$kv/gravitino-config-no-section.json       java -jar target/kyuubi-config.jar gravitino -ver $kv --format json --no-section $PROXY_ARGS $PROXY_TYPE_ARGS
  atomic_write gravitino/$kv/gravitino-config.conf                  java -jar target/kyuubi-config.jar gravitino -ver $kv --format conf $PROXY_ARGS $PROXY_TYPE_ARGS

  atomic_write gravitino/$kv/gravitino-iceberg-rest-no-section.json java -jar target/kyuubi-config.jar gravitino -ver $kv --format json --no-section iceberg-rest $PROXY_ARGS $PROXY_TYPE_ARGS
  atomic_write gravitino/$kv/gravitino-iceberg-rest.conf            java -jar target/kyuubi-config.jar gravitino -ver $kv --format conf              iceberg-rest $PROXY_ARGS $PROXY_TYPE_ARGS

done

for kv in "3.4" "3.5" "4.0" "4.1" "current"; do
  echo "version=$kv";
  mkdir -p spring-boot/$kv;
  atomic_write spring-boot/$kv/spring-boot-config.json              java -jar target/kyuubi-config.jar spring-boot -ver $kv --format json $PROXY_ARGS $PROXY_TYPE_ARGS
  atomic_write spring-boot/$kv/spring-boot-config-no-section.json   java -jar target/kyuubi-config.jar spring-boot -ver $kv --format json --no-section $PROXY_ARGS $PROXY_TYPE_ARGS
  atomic_write spring-boot/$kv/spring-boot-config.conf              java -jar target/kyuubi-config.jar spring-boot -ver $kv --format conf $PROXY_ARGS $PROXY_TYPE_ARGS
done


for kv in "3.3" "stable"; do
  echo "version=$kv";
  mkdir -p debezium/$kv;
  atomic_write debezium/$kv/debezium-mysql-no-section.json          java -jar target/kyuubi-config.jar debezium -ver $kv --format json --no-section $PROXY_ARGS $PROXY_TYPE_ARGS
  atomic_write debezium/$kv/debezium-mysql.conf                     java -jar target/kyuubi-config.jar debezium -ver $kv --format conf $PROXY_ARGS $PROXY_TYPE_ARGS
  atomic_write debezium/$kv/debezium-jdbc-no-section.json           java -jar target/kyuubi-config.jar debezium -ver $kv --format json --no-section -t jdbc $PROXY_ARGS $PROXY_TYPE_ARGS
  atomic_write debezium/$kv/debezium-jdbc.conf                      java -jar target/kyuubi-config.jar debezium -ver $kv --format conf              -t jdbc $PROXY_ARGS $PROXY_TYPE_ARGS
  atomic_write debezium/$kv/debezium-postgresql-no-section.json     java -jar target/kyuubi-config.jar debezium -ver $kv --format json --no-section -t postgresql $PROXY_ARGS $PROXY_TYPE_ARGS
  atomic_write debezium/$kv/debezium-postgresql.conf                java -jar target/kyuubi-config.jar debezium -ver $kv --format conf              -t postgresql $PROXY_ARGS $PROXY_TYPE_ARGS
done
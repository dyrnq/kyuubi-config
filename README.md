# kyuubi-config

## build

```bash
mvn clean package -Dmaven.test.skip=true
```

## use

### kyuubi

```bash
Usage: <main class> kyuubi [-nv] [-f=<format>] [-kv=<kyuubiVersion>]
                           [-P=<proxy>] [-pt=<proxyType>]
kyuubi
  -f, --format=<format>   format
      -kv, -ver, --kyuubi-version=<kyuubiVersion>
                          kyuubi version e.g v1.10.2
  -n, --no-section
  -P, --proxy=<proxy>
      -pt, --proxy-type=<proxyType>

  -v, --verbose           explain what is being done

```

### spark

```bash
Usage: <main class> spark [-nv] [-f=<format>] [-P=<proxy>] [-pt=<proxyType>]
                          [-sv=<sparkVersion>]
spark
  -f, --format=<format>   format
  -n, --no-section
  -P, --proxy=<proxy>
      -pt, --proxy-type=<proxyType>

      -sv, -ver, --spark-version=<sparkVersion>
                          spark version e.g. 3.5.4
  -v, --verbose           explain what is being done


```

### flink

```bash
Usage: <main class> flink [-nv] [-f=<format>] [-fv=<flinkVersion>] [-P=<proxy>]
                          [-pt=<proxyType>]
flink
  -f, --format=<format>   format
      -fv, -ver, --flink-version=<flinkVersion>
                          flink version e.g. 1.20
  -n, --no-section
  -P, --proxy=<proxy>
      -pt, --proxy-type=<proxyType>

  -v, --verbose           explain what is being done


```

### trino

see <https://github.com/nineinchnick/trino-properties-index>.
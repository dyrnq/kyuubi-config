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

### hadoop and yarn

| ver     | hdfs-default.xml                                                                                            | core-default.xml                                                                                              | yarn-default.xml                                                                                           |
|---------|-------------------------------------------------------------------------------------------------------------|---------------------------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------------------------|
| r2.4.1  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.4.1/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.4.1/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.4.1/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.5.2  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.5.2/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.5.2/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.5.2/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.6.0  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.6.0/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.6.0/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.6.0/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.6.1  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.6.1/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.6.1/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.6.1/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.6.2  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.6.2/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.6.2/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.6.2/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.6.3  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.6.3/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.6.3/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.6.3/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.6.4  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.6.4/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.6.4/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.6.4/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.6.5  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.6.5/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.6.5/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.6.5/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.7.0  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.7.0/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.7.0/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.7.0/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.7.1  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.7.1/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.7.1/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.7.1/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.7.2  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.7.2/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.7.2/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.7.2/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.7.3  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.7.3/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.7.3/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.7.3/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.7.4  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.7.4/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.7.4/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.7.4/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.7.5  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.7.5/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.7.5/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.7.5/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.7.6  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.7.6/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.7.6/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.7.6/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.7.7  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.7.7/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.7.7/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.7.7/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.8.0  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.8.0/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.8.0/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.8.0/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.8.2  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.8.2/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.8.2/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.8.2/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.8.3  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.8.3/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.8.3/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.8.3/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.8.4  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.8.4/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.8.4/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.8.4/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.8.5  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.8.5/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.8.5/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.8.5/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.9.0  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.9.0/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.9.0/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.9.0/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.9.1  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.9.1/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.9.1/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.9.1/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.9.2  | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.9.2/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r2.9.2/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r2.9.2/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r2.10.0 | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.10.0/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml) | [core-default.xml](https://hadoop.apache.org/docs/r2.10.0/hadoop-project-dist/hadoop-common/core-default.xml) | [yarn-default.xml](https://hadoop.apache.org/docs/r2.10.0/hadoop-yarn/hadoop-yarn-common/yarn-default.xml) |
| r2.10.1 | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.10.1/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml) | [core-default.xml](https://hadoop.apache.org/docs/r2.10.1/hadoop-project-dist/hadoop-common/core-default.xml) | [yarn-default.xml](https://hadoop.apache.org/docs/r2.10.1/hadoop-yarn/hadoop-yarn-common/yarn-default.xml) |
| r2.10.2 | [hdfs-default.xml](https://hadoop.apache.org/docs/r2.10.2/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml) | [core-default.xml](https://hadoop.apache.org/docs/r2.10.2/hadoop-project-dist/hadoop-common/core-default.xml) | [yarn-default.xml](https://hadoop.apache.org/docs/r2.10.2/hadoop-yarn/hadoop-yarn-common/yarn-default.xml) |
| r3.0.0  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.0.0/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.0.0/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.0.0/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.0.1  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.0.1/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.0.1/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.0.1/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.0.2  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.0.2/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.0.2/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.0.2/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.0.3  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.0.3/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.0.3/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.0.3/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.1.0  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.1.0/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.1.0/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.1.0/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.1.1  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.1.1/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.1.1/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.1.1/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.1.2  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.1.2/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.1.2/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.1.2/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.1.3  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.1.3/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.1.3/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.1.3/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.1.4  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.1.4/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.1.4/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.1.4/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.2.0  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.2.0/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.2.0/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.2.0/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.2.1  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.2.1/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.2.1/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.2.1/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.2.2  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.2.2/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.2.2/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.2.2/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.2.3  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.2.3/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.2.3/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.2.3/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.2.4  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.2.4/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.2.4/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.2.4/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.3.0  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.3.0/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.3.0/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.3.0/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.3.1  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.3.1/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.3.1/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.3.1/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.3.2  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.3.2/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.3.2/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.3.2/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.3.3  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.3.3/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.3.3/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.3.3/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.3.4  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.3.4/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.3.4/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.3.4/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.3.5  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.3.5/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.3.5/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.3.5/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.3.6  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.3.6/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.3.6/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.3.6/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.4.0  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.4.0/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.4.0/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.4.0/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| r3.4.1  | [hdfs-default.xml](https://hadoop.apache.org/docs/r3.4.1/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/r3.4.1/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/r3.4.1/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |
| current | [hdfs-default.xml](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml) | [core-default.xml](https://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/core-default.xml) | [yarn-default.xml](https://hadoop.apache.org/docs/current/hadoop-yarn/hadoop-yarn-common/yarn-default.xml) |
| stable  | [hdfs-default.xml](https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-hdfs/hdfs-default.xml)  | [core-default.xml](https://hadoop.apache.org/docs/stable/hadoop-project-dist/hadoop-common/core-default.xml)  | [yarn-default.xml](https://hadoop.apache.org/docs/stable/hadoop-yarn/hadoop-yarn-common/yarn-default.xml)  |



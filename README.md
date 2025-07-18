#kyuubi-config

## build

```bash
mvn clean package -Dmaven.test.skip=true
```

## use

```bash
java -jar target/kyuubi-config.jar config


Usage: <main class> config [-nv] [-f=<format>] [-kv=<kyuubiVersion>]
config
  -f, --format=<format>   format
      -kv, --kyuubi-version=<kyuubiVersion>
                          kyuubi version e.g v1.10.2
  -n, --no-section
  -v, --verbose           explain what is being done
```
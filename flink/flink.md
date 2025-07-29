```bash
WARNING: Unknown module: jdk.compiler specified to --add-exports
WARNING: Unknown module: jdk.compiler specified to --add-exports
WARNING: Unknown module: jdk.compiler specified to --add-exports
WARNING: Unknown module: jdk.compiler specified to --add-exports
WARNING: Unknown module: jdk.compiler specified to --add-exports
./flink <ACTION> [OPTIONS] [ARGUMENTS]

The following actions are available:

Action "run" compiles and runs a program.

  Syntax: run [OPTIONS] <jar-file> <arguments>
  "run" action options:
     -c,--class <classname>                     Class with the program entry
                                                point ("main()" method). Only
                                                needed if the JAR file does not
                                                specify the class in its
                                                manifest.
     -C,--classpath <url>                       Adds a URL to each user code
                                                classloader  on all nodes in the
                                                cluster. The paths must specify
                                                a protocol (e.g. file://) and be
                                                accessible on all nodes (e.g. by
                                                means of a NFS share). You can
                                                use this option multiple times
                                                for specifying more than one
                                                URL. The protocol must be
                                                supported by the {@link
                                                java.net.URLClassLoader}.
     -cm,--claimMode <arg>                      Defines how should we restore
                                                from the given savepoint.
                                                Supported options: [claim -
                                                claim ownership of the savepoint
                                                and delete once it is subsumed,
                                                no_claim (default) - do not
                                                claim ownership, the first
                                                checkpoint will not reuse any
                                                files from the restored one,
                                                legacy (deprecated) - the old
                                                behaviour, do not assume
                                                ownership of the savepoint
                                                files, but can reuse some shared
                                                files.
     -d,--detached                              If present, runs the job in
                                                detached mode
     -n,--allowNonRestoredState                 Allow to skip savepoint state
                                                that cannot be restored. You
                                                need to allow this if you
                                                removed an operator from your
                                                program that was part of the
                                                program when the savepoint was
                                                triggered.
     -p,--parallelism <parallelism>             The parallelism with which to
                                                run the program. Optional flag
                                                to override the default value
                                                specified in the configuration.
     -py,--python <pythonFile>                  Python script with the program
                                                entry point. The dependent
                                                resources can be configured with
                                                the `--pyFiles` option.
     -pyarch,--pyArchives <arg>                 Add python archive files for
                                                job. The archive files will be
                                                extracted to the working
                                                directory of python UDF worker.
                                                For each archive file, a target
                                                directory be specified. If the
                                                target directory name is
                                                specified, the archive file will
                                                be extracted to a directory with
                                                the specified name. Otherwise,
                                                the archive file will be
                                                extracted to a directory with
                                                the same name of the archive
                                                file. The files uploaded via
                                                this option are accessible via
                                                relative path. '#' could be used
                                                as the separator of the archive
                                                file path and the target
                                                directory name. Comma (',')
                                                could be used as the separator
                                                to specify multiple archive
                                                files. This option can be used
                                                to upload the virtual
                                                environment, the data files used
                                                in Python UDF (e.g.,
                                                --pyArchives
                                                file:///tmp/py37.zip,file:///tmp
                                                /data.zip#data --pyExecutable
                                                py37.zip/py37/bin/python). The
                                                data files could be accessed in
                                                Python UDF, e.g.: f =
                                                open('data/data.txt', 'r').
     -pyclientexec,--pyClientExecutable <arg>   The path of the Python
                                                interpreter used to launch the
                                                Python process when submitting
                                                the Python jobs via "flink run"
                                                or compiling the Java/Scala jobs
                                                containing Python UDFs.
     -pyexec,--pyExecutable <arg>               Specify the path of the python
                                                interpreter used to execute the
                                                python UDF worker (e.g.:
                                                --pyExecutable
                                                /usr/local/bin/python3). The
                                                python UDF worker depends on
                                                Python 3.8+, Apache Beam
                                                (version == 2.43.0), Pip
                                                (version >= 20.3) and SetupTools
                                                (version >= 37.0.0). Please
                                                ensure that the specified
                                                environment meets the above
                                                requirements.
     -pyfs,--pyFiles <pythonFiles>              Attach custom files for job. The
                                                standard resource file suffixes
                                                such as .py/.egg/.zip/.whl or
                                                directory are all supported.
                                                These files will be added to the
                                                PYTHONPATH of both the local
                                                client and the remote python UDF
                                                worker. Files suffixed with .zip
                                                will be extracted and added to
                                                PYTHONPATH. Comma (',') could be
                                                used as the separator to specify
                                                multiple files (e.g., --pyFiles
                                                file:///tmp/myresource.zip,hdfs:
                                                ///$namenode_address/myresource2
                                                .zip).
     -pym,--pyModule <pythonModule>             Python module with the program
                                                entry point. This option must be
                                                used in conjunction with
                                                `--pyFiles`.
     -pypath,--pyPythonPath <arg>               Specify the path of the python
                                                installation in worker
                                                nodes.(e.g.: --pyPythonPath
                                                /python/lib64/python3.7/).User
                                                can specify multiple paths using
                                                the separator ":".
     -pyreq,--pyRequirements <arg>              Specify a requirements.txt file
                                                which defines the third-party
                                                dependencies. These dependencies
                                                will be installed and added to
                                                the PYTHONPATH of the python UDF
                                                worker. A directory which
                                                contains the installation
                                                packages of these dependencies
                                                could be specified optionally.
                                                Use '#' as the separator if the
                                                optional parameter exists (e.g.,
                                                --pyRequirements
                                                file:///tmp/requirements.txt#fil
                                                e:///tmp/cached_dir).
     -rm,--restoreMode <arg>                    This option is deprecated,
                                                please use 'claimMode' instead.
     -s,--fromSavepoint <savepointPath>         Path to a savepoint to restore
                                                the job from (for example
                                                hdfs:///flink/savepoint-1537).
     -sae,--shutdownOnAttachedExit              If the job is submitted in
                                                attached mode, perform a
                                                best-effort cluster shutdown
                                                when the CLI is terminated
                                                abruptly, e.g., in response to a
                                                user interrupt, such as typing
                                                Ctrl + C.
  Options for Generic CLI mode:
     -D <property=value>   Allows specifying multiple generic configuration
                           options. The available options can be found at
                           https://nightlies.apache.org/flink/flink-docs-stable/
                           ops/config.html
     -e,--executor <arg>   DEPRECATED: Please use the -t option instead which is
                           also available with the "Application Mode".
                           The name of the executor to be used for executing the
                           given job, which is equivalent to the
                           "execution.target" config option. The currently
                           available executors are: "remote", "local",
                           "kubernetes-session", "yarn-per-job" (deprecated),
                           "yarn-session".
     -t,--target <arg>     The deployment target for the given application,
                           which is equivalent to the "execution.target" config
                           option. For the "run" action the currently available
                           targets are: "remote", "local", "kubernetes-session",
                           "yarn-per-job" (deprecated), "yarn-session". For the
                           "run-application" action the currently available
                           targets are: "kubernetes-application".

  Options for yarn-cluster mode:
     -m,--jobmanager <arg>            Set to yarn-cluster to use YARN execution
                                      mode.
     -yid,--yarnapplicationId <arg>   Attach to running YARN session
     -z,--zookeeperNamespace <arg>    Namespace to create the Zookeeper
                                      sub-paths for high availability mode

  Options for default mode:
     -D <property=value>             Allows specifying multiple generic
                                     configuration options. The available
                                     options can be found at
                                     https://nightlies.apache.org/flink/flink-do
                                     cs-stable/ops/config.html
     -m,--jobmanager <arg>           Address of the JobManager to which to
                                     connect. Use this flag to connect to a
                                     different JobManager than the one specified
                                     in the configuration. Attention: This
                                     option is respected only if the
                                     high-availability configuration is NONE.
     -z,--zookeeperNamespace <arg>   Namespace to create the Zookeeper sub-paths
                                     for high availability mode



Action "run-application" runs an application in Application Mode.

  Syntax: run-application [OPTIONS] <jar-file> <arguments>
  Options for Generic CLI mode:
     -D <property=value>   Allows specifying multiple generic configuration
                           options. The available options can be found at
                           https://nightlies.apache.org/flink/flink-docs-stable/
                           ops/config.html
     -e,--executor <arg>   DEPRECATED: Please use the -t option instead which is
                           also available with the "Application Mode".
                           The name of the executor to be used for executing the
                           given job, which is equivalent to the
                           "execution.target" config option. The currently
                           available executors are: "remote", "local",
                           "kubernetes-session", "yarn-per-job" (deprecated),
                           "yarn-session".
     -t,--target <arg>     The deployment target for the given application,
                           which is equivalent to the "execution.target" config
                           option. For the "run" action the currently available
                           targets are: "remote", "local", "kubernetes-session",
                           "yarn-per-job" (deprecated), "yarn-session". For the
                           "run-application" action the currently available
                           targets are: "kubernetes-application".



Action "info" shows the optimized execution plan of the program (JSON).

  Syntax: info [OPTIONS] <jar-file> <arguments>
  "info" action options:
     -c,--class <classname>           Class with the program entry point
                                      ("main()" method). Only needed if the JAR
                                      file does not specify the class in its
                                      manifest.
     -p,--parallelism <parallelism>   The parallelism with which to run the
                                      program. Optional flag to override the
                                      default value specified in the
                                      configuration.


Action "list" lists running and scheduled programs.

  Syntax: list [OPTIONS]
  "list" action options:
     -a,--all         Show all programs and their JobIDs
     -r,--running     Show only running programs and their JobIDs
     -s,--scheduled   Show only scheduled programs and their JobIDs
  Options for Generic CLI mode:
     -D <property=value>   Allows specifying multiple generic configuration
                           options. The available options can be found at
                           https://nightlies.apache.org/flink/flink-docs-stable/
                           ops/config.html
     -e,--executor <arg>   DEPRECATED: Please use the -t option instead which is
                           also available with the "Application Mode".
                           The name of the executor to be used for executing the
                           given job, which is equivalent to the
                           "execution.target" config option. The currently
                           available executors are: "remote", "local",
                           "kubernetes-session", "yarn-per-job" (deprecated),
                           "yarn-session".
     -t,--target <arg>     The deployment target for the given application,
                           which is equivalent to the "execution.target" config
                           option. For the "run" action the currently available
                           targets are: "remote", "local", "kubernetes-session",
                           "yarn-per-job" (deprecated), "yarn-session". For the
                           "run-application" action the currently available
                           targets are: "kubernetes-application".

  Options for yarn-cluster mode:
     -m,--jobmanager <arg>            Set to yarn-cluster to use YARN execution
                                      mode.
     -yid,--yarnapplicationId <arg>   Attach to running YARN session
     -z,--zookeeperNamespace <arg>    Namespace to create the Zookeeper
                                      sub-paths for high availability mode

  Options for default mode:
     -D <property=value>             Allows specifying multiple generic
                                     configuration options. The available
                                     options can be found at
                                     https://nightlies.apache.org/flink/flink-do
                                     cs-stable/ops/config.html
     -m,--jobmanager <arg>           Address of the JobManager to which to
                                     connect. Use this flag to connect to a
                                     different JobManager than the one specified
                                     in the configuration. Attention: This
                                     option is respected only if the
                                     high-availability configuration is NONE.
     -z,--zookeeperNamespace <arg>   Namespace to create the Zookeeper sub-paths
                                     for high availability mode



Action "stop" stops a running program with a savepoint (streaming jobs only).

  Syntax: stop [OPTIONS] <Job ID>
  "stop" action options:
     -d,--drain                           Send MAX_WATERMARK before taking the
                                          savepoint and stopping the pipeline.
     -detached                            Triggering savepoint in detached mode,
                                          client and JM are decoupled, return
                                          the savepoint trigger id as the unique
                                          identification of the detached
                                          savepoint.
     -p,--savepointPath <savepointPath>   Path to the savepoint (for example
                                          hdfs:///flink/savepoint-1537). If no
                                          directory is specified, the configured
                                          default will be used
                                          ("execution.checkpointing.savepoint-di
                                          r").
     -type,--type <arg>                   Describes the binary format in which a
                                          savepoint should be taken. Supported
                                          options: [canonical - a common format
                                          for all state backends, allow for
                                          changing state backends, native = a
                                          specific format for the chosen state
                                          backend, might be faster to take and
                                          restore from.
  Options for Generic CLI mode:
     -D <property=value>   Allows specifying multiple generic configuration
                           options. The available options can be found at
                           https://nightlies.apache.org/flink/flink-docs-stable/
                           ops/config.html
     -e,--executor <arg>   DEPRECATED: Please use the -t option instead which is
                           also available with the "Application Mode".
                           The name of the executor to be used for executing the
                           given job, which is equivalent to the
                           "execution.target" config option. The currently
                           available executors are: "remote", "local",
                           "kubernetes-session", "yarn-per-job" (deprecated),
                           "yarn-session".
     -t,--target <arg>     The deployment target for the given application,
                           which is equivalent to the "execution.target" config
                           option. For the "run" action the currently available
                           targets are: "remote", "local", "kubernetes-session",
                           "yarn-per-job" (deprecated), "yarn-session". For the
                           "run-application" action the currently available
                           targets are: "kubernetes-application".

  Options for yarn-cluster mode:
     -m,--jobmanager <arg>            Set to yarn-cluster to use YARN execution
                                      mode.
     -yid,--yarnapplicationId <arg>   Attach to running YARN session
     -z,--zookeeperNamespace <arg>    Namespace to create the Zookeeper
                                      sub-paths for high availability mode

  Options for default mode:
     -D <property=value>             Allows specifying multiple generic
                                     configuration options. The available
                                     options can be found at
                                     https://nightlies.apache.org/flink/flink-do
                                     cs-stable/ops/config.html
     -m,--jobmanager <arg>           Address of the JobManager to which to
                                     connect. Use this flag to connect to a
                                     different JobManager than the one specified
                                     in the configuration. Attention: This
                                     option is respected only if the
                                     high-availability configuration is NONE.
     -z,--zookeeperNamespace <arg>   Namespace to create the Zookeeper sub-paths
                                     for high availability mode



Action "cancel" cancels a running program.

  Syntax: cancel [OPTIONS] <Job ID>
  "cancel" action options:
     -s,--withSavepoint <targetDirectory>   **DEPRECATION WARNING**: Cancelling
                                            a job with savepoint is deprecated.
                                            Use "stop" instead.
                                            Trigger savepoint and cancel job.
                                            The target directory is optional. If
                                            no directory is specified, the
                                            configured default directory
                                            (execution.checkpointing.savepoint-d
                                            ir) is used.
  Options for Generic CLI mode:
     -D <property=value>   Allows specifying multiple generic configuration
                           options. The available options can be found at
                           https://nightlies.apache.org/flink/flink-docs-stable/
                           ops/config.html
     -e,--executor <arg>   DEPRECATED: Please use the -t option instead which is
                           also available with the "Application Mode".
                           The name of the executor to be used for executing the
                           given job, which is equivalent to the
                           "execution.target" config option. The currently
                           available executors are: "remote", "local",
                           "kubernetes-session", "yarn-per-job" (deprecated),
                           "yarn-session".
     -t,--target <arg>     The deployment target for the given application,
                           which is equivalent to the "execution.target" config
                           option. For the "run" action the currently available
                           targets are: "remote", "local", "kubernetes-session",
                           "yarn-per-job" (deprecated), "yarn-session". For the
                           "run-application" action the currently available
                           targets are: "kubernetes-application".

  Options for yarn-cluster mode:
     -m,--jobmanager <arg>            Set to yarn-cluster to use YARN execution
                                      mode.
     -yid,--yarnapplicationId <arg>   Attach to running YARN session
     -z,--zookeeperNamespace <arg>    Namespace to create the Zookeeper
                                      sub-paths for high availability mode

  Options for default mode:
     -D <property=value>             Allows specifying multiple generic
                                     configuration options. The available
                                     options can be found at
                                     https://nightlies.apache.org/flink/flink-do
                                     cs-stable/ops/config.html
     -m,--jobmanager <arg>           Address of the JobManager to which to
                                     connect. Use this flag to connect to a
                                     different JobManager than the one specified
                                     in the configuration. Attention: This
                                     option is respected only if the
                                     high-availability configuration is NONE.
     -z,--zookeeperNamespace <arg>   Namespace to create the Zookeeper sub-paths
                                     for high availability mode



Action "savepoint" triggers savepoints for a running job or disposes existing ones.

  Syntax: savepoint [OPTIONS] <Job ID> [<target directory>]
  "savepoint" action options:
     -d,--dispose <arg>       Path of savepoint to dispose.
     -detached                Triggering savepoint in detached mode, client and
                              JM are decoupled, return the savepoint trigger id
                              as the unique identification of the detached
                              savepoint.
     -j,--jarfile <jarfile>   Flink program JAR file.
     -type,--type <arg>       Describes the binary format in which a savepoint
                              should be taken. Supported options: [canonical - a
                              common format for all state backends, allow for
                              changing state backends, native = a specific
                              format for the chosen state backend, might be
                              faster to take and restore from.
  Options for Generic CLI mode:
     -D <property=value>   Allows specifying multiple generic configuration
                           options. The available options can be found at
                           https://nightlies.apache.org/flink/flink-docs-stable/
                           ops/config.html
     -e,--executor <arg>   DEPRECATED: Please use the -t option instead which is
                           also available with the "Application Mode".
                           The name of the executor to be used for executing the
                           given job, which is equivalent to the
                           "execution.target" config option. The currently
                           available executors are: "remote", "local",
                           "kubernetes-session", "yarn-per-job" (deprecated),
                           "yarn-session".
     -t,--target <arg>     The deployment target for the given application,
                           which is equivalent to the "execution.target" config
                           option. For the "run" action the currently available
                           targets are: "remote", "local", "kubernetes-session",
                           "yarn-per-job" (deprecated), "yarn-session". For the
                           "run-application" action the currently available
                           targets are: "kubernetes-application".

  Options for yarn-cluster mode:
     -m,--jobmanager <arg>            Set to yarn-cluster to use YARN execution
                                      mode.
     -yid,--yarnapplicationId <arg>   Attach to running YARN session
     -z,--zookeeperNamespace <arg>    Namespace to create the Zookeeper
                                      sub-paths for high availability mode

  Options for default mode:
     -D <property=value>             Allows specifying multiple generic
                                     configuration options. The available
                                     options can be found at
                                     https://nightlies.apache.org/flink/flink-do
                                     cs-stable/ops/config.html
     -m,--jobmanager <arg>           Address of the JobManager to which to
                                     connect. Use this flag to connect to a
                                     different JobManager than the one specified
                                     in the configuration. Attention: This
                                     option is respected only if the
                                     high-availability configuration is NONE.
     -z,--zookeeperNamespace <arg>   Namespace to create the Zookeeper sub-paths
                                     for high availability mode



Action "checkpoint" triggers checkpoints for a running job.

  Syntax: checkpoint [OPTIONS] <Job ID>
  "checkpoint" action options:
     -full,--full   Defines whether to trigger this checkpoint as a full one.
  Options for Generic CLI mode:
     -D <property=value>   Allows specifying multiple generic configuration
                           options. The available options can be found at
                           https://nightlies.apache.org/flink/flink-docs-stable/
                           ops/config.html
     -e,--executor <arg>   DEPRECATED: Please use the -t option instead which is
                           also available with the "Application Mode".
                           The name of the executor to be used for executing the
                           given job, which is equivalent to the
                           "execution.target" config option. The currently
                           available executors are: "remote", "local",
                           "kubernetes-session", "yarn-per-job" (deprecated),
                           "yarn-session".
     -t,--target <arg>     The deployment target for the given application,
                           which is equivalent to the "execution.target" config
                           option. For the "run" action the currently available
                           targets are: "remote", "local", "kubernetes-session",
                           "yarn-per-job" (deprecated), "yarn-session". For the
                           "run-application" action the currently available
                           targets are: "kubernetes-application".

  Options for yarn-cluster mode:
     -m,--jobmanager <arg>            Set to yarn-cluster to use YARN execution
                                      mode.
     -yid,--yarnapplicationId <arg>   Attach to running YARN session
     -z,--zookeeperNamespace <arg>    Namespace to create the Zookeeper
                                      sub-paths for high availability mode

  Options for default mode:
     -D <property=value>             Allows specifying multiple generic
                                     configuration options. The available
                                     options can be found at
                                     https://nightlies.apache.org/flink/flink-do
                                     cs-stable/ops/config.html
     -m,--jobmanager <arg>           Address of the JobManager to which to
                                     connect. Use this flag to connect to a
                                     different JobManager than the one specified
                                     in the configuration. Attention: This
                                     option is respected only if the
                                     high-availability configuration is NONE.
     -z,--zookeeperNamespace <arg>   Namespace to create the Zookeeper sub-paths
                                     for high availability mode
```
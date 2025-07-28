package com.dyrnq.kyuubi;

import com.dyrnq.kyuubi.command.*;
import lombok.extern.slf4j.Slf4j;
import org.noear.solon.Solon;
import picocli.CommandLine;

@picocli.CommandLine.Command(
        subcommands = {
                KyuubiConfig.class,
                SparkConfig.class,
                FlinkConfig.class,
                KafkaConfig.class,
                AmoroConfig.class,
                BookKeeperConfig.class,
                DorisConfig.class
        },
        mixinStandardHelpOptions = true,
        showDefaultValues = true
)
@Slf4j
public class Main implements Runnable {


    public static void main(String[] args) {
        Solon.start(Main.class, args, app -> {
        });

        Main app = new Main();
        CommandLine cmd = new CommandLine(app);

//        cmd.setUnmatchedOptionsArePositionalParams(true);
//        cmd.setUnmatchedArgumentsAllowed(true);

        int code = cmd.execute(args);
        System.exit(code);
        Solon.stop(0);


    }


    @Override
    public void run() {

    }

}
package com.dyrnq.kyuubi.command;

import picocli.CommandLine;

public class CommonOptions {
    @CommandLine.Option(names = {"-v", "--verbose"}, description = "explain what is being done")
    boolean verbose;
    @CommandLine.Option(names = {"--proxy", "-P"}, description = "")
    String proxy;
    @CommandLine.Option(names = {"--proxy-type", "-pt"}, description = "", defaultValue = "HTTP")
    String proxyType;
}

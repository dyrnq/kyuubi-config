package com.dyrnq.kyuubi.command;

import org.jsoup.Connection;
import picocli.CommandLine;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class CommonOptions {
    @CommandLine.Option(names = {"-v", "--verbose"}, description = "explain what is being done")
    boolean verbose;
    @CommandLine.Option(names = {"--proxy", "-P"}, description = "proxy host:port, e.g. 127.0.0.1:8118")
    String proxy;
    @CommandLine.Option(names = {"--proxy-type", "-pt"}, description = "proxy type: HTTP or SOCKS", defaultValue = "HTTP")
    String proxyType;

    /**
     * Apply the configured proxy to a Jsoup Connection.
     * If {@code proxy} is null/blank, the connection is returned unchanged
     * (so system-level proxy settings still apply via JVM properties).
     *
     * <p>Supported forms:
     * <ul>
     *   <li>{@code -P host:port}         (HTTP proxy, default)</li>
     *   <li>{@code -P host:port -pt SOCKS}</li>
     * </ul>
     */
    protected Connection applyProxy(Connection conn) {
        if (proxy == null || proxy.isBlank()) {
            return conn;
        }
        String[] parts = proxy.split(":");
        String host = parts[0];
        int port = parts.length > 1 ? Integer.parseInt(parts[1].trim()) : 8080;
        if ("SOCKS".equalsIgnoreCase(proxyType)) {
            return conn.proxy(new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(host, port)));
        }
        return conn.proxy(host, port);
    }
}

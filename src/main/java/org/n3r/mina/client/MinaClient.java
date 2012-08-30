package org.n3r.mina.client;

import java.net.InetSocketAddress;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MinaClient {

    private ConnectFuture cf = null;

    private NioSocketConnector connector = null;

    public MinaClient(String address, int port, IoHandler handler) {
        connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.setConnectTimeoutMillis(1000);
        connector.setHandler(handler);
        cf = connector.connect(new InetSocketAddress(address, port));
        cf.awaitUninterruptibly();
    }

    public void awaitClose() {
        cf.getSession().getCloseFuture().awaitUninterruptibly();
        connector.dispose();
    }

    public void sendMessage(byte[] message) {
        cf.getSession().write(IoBuffer.wrap(message));
    }

}

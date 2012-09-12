package org.n3r.mina.server;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.n3r.config.Config;

public class MinaServer {

    private static final int IDLE_TIME = Config.getInt("MinaIdleTime", 60);

    private static final int PORT = Config.getInt("MinaServerPort", 9123);

    public static void main(String[] args) throws Exception {
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.setHandler(new JCServerHandler());
        acceptor.getSessionConfig().setReadBufferSize(2048);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDLE_TIME);
        acceptor.bind(new InetSocketAddress(PORT));
        System.out.println("MinaServer started on port " + PORT);
        //        Thread.sleep(3000);
        //        acceptor.unbind();
        //        acceptor.dispose(true);
    }

}

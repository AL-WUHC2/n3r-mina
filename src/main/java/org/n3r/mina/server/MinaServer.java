package org.n3r.mina.server;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import static org.phw.config.impl.PhwConfigMgrFactory.*;

public class MinaServer {

    private static final int PORT = getConfigMgr().getInt("MinaServerPort", 9123);

    public static void main(String[] args) throws Exception {
        IoAcceptor acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.setHandler(new MinaServerHandler());
        acceptor.getSessionConfig().setReadBufferSize(2048);
        int idle = getConfigMgr().getInt("MinaIdleTime", 60);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, idle);
        acceptor.bind(new InetSocketAddress(PORT));
        System.out.println("MinaServer started on port " + PORT);
        //        Thread.sleep(3000);
        //        acceptor.unbind();
        //        acceptor.dispose(true);
    }

}

package org.n3r.mina.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.n3r.mina.JCBytesParserFactory;
import org.n3r.mina.JCHandler;
import org.n3r.mina.listener.JCServerReceiveListener;
import org.n3r.mina.listener.JCServerSendListener;
import org.n3r.mina.listener.JCSessionCreateListener;

public class JCServerHandler extends JCHandler {

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        byte[] bytes = fetchIoBufferBytes((IoBuffer) message);

        JCBytesParserFactory parser = getBytesParserFactory(session);
        parser.parseBytes(bytes);

        if (parser.getOrderNo() == 1) {
            parser.removeListener(0);
        }
    }

    @Override
    protected void addListenersToFactory(IoSession session, JCBytesParserFactory parserFactory) {
        parserFactory.addListener(new JCSessionCreateListener())
                .addListener(new JCServerReceiveListener(session))
                .addListener(new JCServerSendListener(session));
    }

}

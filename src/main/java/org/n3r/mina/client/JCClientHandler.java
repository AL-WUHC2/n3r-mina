package org.n3r.mina.client;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.n3r.mina.JCBytesParserFactory;
import org.n3r.mina.JCHandler;
import org.n3r.mina.listener.JCClientReceiveListener;
import org.n3r.mina.listener.JCClientSendListener;
import org.n3r.mina.listener.JCSessionCreateListener;

public class JCClientHandler extends JCHandler {

    private JCBytesParserFactory sendParserFactory = new JCBytesParserFactory();

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        byte[] bytes = fetchIoBufferBytes((IoBuffer) message);

        getBytesParserFactory(session).parseBytes(bytes);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        JCBytesParserFactory parser = getBytesParserFactory(session);

        if (parser.getOrderNo() == 0) {
            byte[] bytes = fetchIoBufferBytes((IoBuffer) message);

            sendParserFactory.addListener(new JCSessionCreateListener());
            sendParserFactory.parseBytes(bytes);
            sendParserFactory.cleanListeners();

            if (sendParserFactory.getOrderNo() == 1) {
                sendParserFactory.cleanBuffer();
                setBytesParserFactory(session, sendParserFactory);
                addListenersToFactory(session, sendParserFactory);
            }
        }
    }

    @Override
    protected void addListenersToFactory(IoSession session, JCBytesParserFactory parserFactory) {
        parserFactory.addListener(new JCClientReceiveListener(session)).addListener(new JCClientSendListener(session));
    }

}

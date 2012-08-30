package org.n3r.mina;

import java.util.HashMap;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JCHandler implements IoHandler {

    private HashMap<Long, JCBytesParserFactory> sessionParserMap = new HashMap<Long, JCBytesParserFactory>();

    private Logger logger = LoggerFactory.getLogger(JCHandler.class);

    public void newBytesParserFactory(IoSession session) {
        JCBytesParserFactory parserFactory = new JCBytesParserFactory();
        sessionParserMap.put(session.getId(), parserFactory);
        addListenersToFactory(session, parserFactory);
    }

    protected abstract void addListenersToFactory(IoSession session, JCBytesParserFactory parserFactory);

    public JCBytesParserFactory getBytesParserFactory(IoSession session) {
        return sessionParserMap.get(session.getId());
    }

    public void setBytesParserFactory(IoSession session, JCBytesParserFactory jcMsgParserFactory) {
        sessionParserMap.put(session.getId(), jcMsgParserFactory);
    }

    public void removeBytesParserFactory(IoSession session) {
        sessionParserMap.remove(session.getId());
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        newBytesParserFactory(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        removeBytesParserFactory(session);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.warn("Session close, exceptionCaught: ", cause);
        cause.printStackTrace();
        session.close(true);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        session.close(true);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {}

    @Override
    public void sessionCreated(IoSession session) throws Exception {}

    protected byte[] fetchIoBufferBytes(IoBuffer ioBuffer) {
        byte[] bytes = new byte[ioBuffer.limit()];
        ioBuffer.get(bytes);
        return bytes;
    }

}

package org.n3r.mina.server;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.n3r.core.lang.RByte;
import org.n3r.mina.JCHandler;
import org.n3r.mina.JCSession;

public abstract class JCServerHandler extends JCHandler {

    public abstract byte[] messageServerProcess(JCSession jcSession, byte[] message) throws Exception;

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        JCSession jcSession = getSessionInfo(session);

        jcSession.pushMessage((IoBuffer) message);
        byte[] msg = jcSession.popMessage();

        while (msg != null) {
            incrementSessionOrderNo(session);
            if (msg.length == 0) {
                session.write(IoBuffer.wrap(RByte.toBytes((short) 0)));
                return;
            }
            if (jcSession.getOrderNo() == 1) jcSessionCreated(jcSession, msg);

            session.write(IoBuffer.wrap(messageServerProcess(jcSession, msg)));
            msg = jcSession.popMessage();
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {}

    @Override
    public void sessionCreated(IoSession session) throws Exception {}

}

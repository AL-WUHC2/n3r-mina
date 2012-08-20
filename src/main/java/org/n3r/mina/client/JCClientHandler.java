package org.n3r.mina.client;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.n3r.core.lang.RByte;
import org.n3r.core.lang3.ArrayUtils;
import org.n3r.mina.JCHandler;
import org.n3r.mina.JCSession;

public abstract class JCClientHandler extends JCHandler {

    public abstract byte[] messageClientProcess(JCSession jcSession, byte[] message) throws Exception;

    private byte[] requestBuffer = new byte[0];

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        JCSession jcSession = getSessionInfo(session);

        jcSession.pushMessage((IoBuffer) message);
        byte[] msg = jcSession.popMessage();

        while (msg != null) {
            incrementSessionOrderNo(session);
            if (msg.length == 0) {
                session.close(true);
                return;
            }

            byte[] bytes = messageClientProcess(jcSession, msg);
            if (ArrayUtils.isEmpty(bytes)) {
                session.close(true);
                return;
            }
            session.write(IoBuffer.wrap(bytes));
            msg = jcSession.popMessage();
        }
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        if (getSessionOrderNo(session) == 0) {
            JCSession jcSession = getSessionInfo(session);

            requestBuffer = JCSession.pushMessage(requestBuffer, (IoBuffer) message);
            byte[] msg = JCSession.popMessage(requestBuffer);

            if (ArrayUtils.isNotEmpty(msg)) {
                requestBuffer = RByte.subBytes(requestBuffer, 2 + msg.length);
                jcSessionCreated(jcSession, msg);
            }
        }
    }

    @Override
    public void sessionCreated(IoSession session) throws Exception {}

}

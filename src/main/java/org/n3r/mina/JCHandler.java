package org.n3r.mina;

import java.util.HashMap;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.n3r.core.lang.RStr;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.req.IF1ReqBody;
import org.n3r.mina.utils.JCMessageUtils;

public abstract class JCHandler implements IoHandler {

    private HashMap<Long, JCSession> sessionInfoMap = new HashMap<Long, JCSession>();

    public void newSessionInfo(IoSession session) {
        sessionInfoMap.put(session.getId(), new JCSession());
    }

    public JCSession getSessionInfo(IoSession session) {
        return sessionInfoMap.get(session.getId());
    }

    public void removeSessionInfo(IoSession session) {
        sessionInfoMap.remove(session.getId());
    }

    public void incrementSessionOrderNo(IoSession session) {
        sessionInfoMap.get(session.getId()).incrementOrderNo();
    }

    public int getSessionOrderNo(IoSession session) {
        return sessionInfoMap.get(session.getId()).getOrderNo();
    }

    public void threadSleep(Object obj) throws Exception {
        Thread.sleep(Integer.valueOf(RStr.toStr(obj)));
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        newSessionInfo(session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {
        removeSessionInfo(session);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        cause.printStackTrace();
        session.close(true);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        session.close(true);
    }

    protected void jcSessionCreated(JCSession jcSession, byte[] msg) {
        JCMessage reqMessage = JCMessageUtils.reqMessageFromBytes(msg).getBean();

        jcSession.setSessionId(reqMessage.getHead().getSessionId());

        IF1ReqBody body = (IF1ReqBody) reqMessage.getBody();
        jcSession.setStaffId(body.getOperatorId());
        jcSession.setJobType(body.getJobType());
    }

}

package org.n3r.mina.client;

import org.n3r.beanbytes.ParseBean;
import org.n3r.core.joor.Reflect;
import org.n3r.mina.JCSession;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.client.process.IF1Process;
import org.n3r.mina.client.process.JCClientProcess;
import org.n3r.mina.utils.JCMessageUtils;
import org.n3r.mina.utils.JCTypeUtils;
import org.phw.ibatis.engine.PDao;
import org.phw.ibatis.util.PDaoEngines;

public class MinaClientHandler extends JCClientHandler {

    private final static String SQL_XML = "org/n3r/mina/client/JCClientSQL.xml";
    private static PDao dao = PDaoEngines.getDao(SQL_XML, "EcsStore");

    @Override
    public byte[] messageClientProcess(JCSession jcSession, byte[] message) throws Exception {
        ParseBean<JCMessageHead> messageHead = JCMessageUtils.parseMessageHead(message);
        String ifNo = JCTypeUtils.getTypeIFString(messageHead.getBean().getTypeFlag());

        Class<?> bodyType = ifNo.equals("IF1") ? JCMessageUtils.parseRspBodyType("IF1", jcSession.getJobType()) :
                JCMessageUtils.parseReqBodyType(ifNo);
        ParseBean<JCMessage> jcMessage = JCMessageUtils.parseMessage(message, messageHead, bodyType);

        JCClientProcess process = ifNo.equals("IF1") ? new IF1Process(dao, jcSession.getJobType()) :
                Reflect.on("org.n3r.mina.client.process." + ifNo + "Process").create(dao).<JCClientProcess> get();

        return JCMessageUtils.messageToBytes(process.process(jcMessage.getBean(), jcSession), null);
    }
}

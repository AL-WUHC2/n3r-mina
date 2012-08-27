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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class MinaClientHandler extends JCClientHandler {

    private final static String SQL_XML = "org/n3r/mina/client/JCClientSQL.xml";
    private static PDao dao = PDaoEngines.getDao(SQL_XML, "Eop");

    private static Logger logger = LoggerFactory.getLogger(MinaClientHandler.class);

    @Override
    public byte[] messageClientProcess(JCSession jcSession, byte[] message) throws Exception {
        ParseBean<JCMessageHead> messageHead = JCMessageUtils.parseMessageHead(message);
        String ifNo = JCTypeUtils.getTypeIFString(messageHead.getBean().getTypeFlag());

        Class<?> bodyType = ifNo.equals("IF1") ? JCMessageUtils.parseRspBodyType("IF1", jcSession.getJobType()) :
                JCMessageUtils.parseReqBodyType(ifNo);
        ParseBean<JCMessage> jcMessage = JCMessageUtils.parseMessage(message, messageHead, bodyType);
        logger.info("CLIENT RECEIVED: " + JSON.toJSONString(jcMessage.getBean()));

        JCClientProcess process = ifNo.equals("IF1") ? new IF1Process(dao, jcSession.getJobType()) :
                Reflect.on("org.n3r.mina.client.process." + ifNo + "Process").create(dao).<JCClientProcess> get();

        JCMessage result = process.process(jcMessage.getBean(), jcSession);
        logger.info("CLIENT SEND: " + JSON.toJSONString(result));
        return JCMessageUtils.messageToBytes(result, null);
    }
}

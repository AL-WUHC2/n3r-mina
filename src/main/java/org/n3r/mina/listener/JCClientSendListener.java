package org.n3r.mina.listener;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.n3r.core.joor.Reflect;
import org.n3r.core.lang.RHex;
import org.n3r.mina.JCBytesParserFactory;
import org.n3r.mina.JCMessageListener;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.client.process.IF1Process;
import org.n3r.mina.client.process.JCClientProcess;
import org.n3r.mina.utils.JCMessageUtils;
import org.n3r.mina.utils.JCTypeUtils;
import org.phw.ibatis.engine.PDao;
import org.phw.ibatis.util.PDaoEngines;

import com.alibaba.fastjson.JSON;

public class JCClientSendListener extends JCMessageListener {

    private final static String SQL_XML = "org/n3r/mina/client/JCClientSQL.xml";
    private static PDao dao = PDaoEngines.getDao(SQL_XML, "Eop");

    private IoSession session;

    public JCClientSendListener(IoSession session) {
        this.session = session;
    }

    @Override
    public Object process(Object message, JCBytesParserFactory parser) throws Exception {
        JCMessage bean = (JCMessage) message;
        String ifNo = JCTypeUtils.getMsgTypeIFString(bean.getHead().getTypeFlag());

        JCClientProcess process = ifNo.equals("IF1") ? new IF1Process(dao, parser.getJobType()) :
                Reflect.on("org.n3r.mina.client.process." + ifNo + "Process").create(dao).<JCClientProcess> get();

        JCMessage result = process.process(bean, parser);
        logger.info("--------------------------------------------------");
        logger.info("[Client Send Message]: " + JSON.toJSONString(result));

        byte[] bytes = JCMessageUtils.messageToBytes(result, null);
        logger.info("[Client Send Bytes]: " + RHex.encode(bytes));
        logger.info("--------------------------------------------------");

        if (session != null && !session.isClosing()) {
            if (ArrayUtils.isEmpty(bytes)) session.close(true);
            else session.write(IoBuffer.wrap(bytes));
        }
        return bytes;
    }
}

package org.n3r.mina.listener;

import org.apache.mina.core.session.IoSession;
import org.n3r.beanbytes.ParseBean;
import org.n3r.mina.JCBytesParserFactory;
import org.n3r.mina.JCMessageListener;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.utils.JCMessageUtils;

import com.alibaba.fastjson.JSON;

public class JCServerReceiveListener extends JCMessageListener {

    private IoSession session;

    public JCServerReceiveListener(IoSession session) {
        this.session = session;
    }

    @Override
    public Object process(Object message, JCBytesParserFactory parser) throws Exception {
        byte[] bytes = (byte[]) message;

        ParseBean<JCMessage> jcMessage = JCMessageUtils.messageFromBytes(bytes, parser.getJobType(), true);

        logger.info("[Server Receive Message]: " + JSON.toJSONString(jcMessage.getBean()));

        return jcMessage.getBean();
    }

}

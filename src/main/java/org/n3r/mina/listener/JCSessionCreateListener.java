package org.n3r.mina.listener;

import org.n3r.mina.JCBytesParserFactory;
import org.n3r.mina.JCMessageListener;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.req.IF1ReqBody;
import org.n3r.mina.utils.JCMessageUtils;

import com.alibaba.fastjson.JSON;

public class JCSessionCreateListener extends JCMessageListener {

    @Override
    public Object process(Object message, JCBytesParserFactory parser) throws Exception {
        JCMessage bean = JCMessageUtils.messageFromBytes((byte[]) message, "", true).getBean();

        logger.info("[Session Created Message]: " + JSON.toJSONString(bean));

        parser.setSessionId(bean.getHead().getSessionId());

        IF1ReqBody body = (IF1ReqBody) bean.getBody();
        parser.setStaffId(body.getOperatorId());
        parser.setJobType(body.getJobType());

        return message;
    }

}

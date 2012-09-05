package org.n3r.mina.listener;

import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RHex;
import org.n3r.mina.JCBytesParserFactory;
import org.n3r.mina.JCMessageListener;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.req.IF1ReqBody;
import org.n3r.mina.utils.JCMessageUtils;

import com.alibaba.fastjson.JSON;

public class JCSessionCreateListener extends JCMessageListener {

    @Override
    public Object process(Object message, JCBytesParserFactory parser) throws Exception {
        logger.info("--------------------------------------------------");
        byte[] bytes = (byte[]) message;
        logger.info("[Session Created Bytes]: " + RHex.encode(BeanBytesUtils.prependLen(bytes, 2)));

        JCMessage bean = JCMessageUtils.messageFromBytes(bytes, "", true).getBean();

        logger.info("[Session Created Message]: " + JSON.toJSONString(bean));
        logger.info("--------------------------------------------------");

        parser.setSessionId(bean.getHead().getSessionId());

        IF1ReqBody body = (IF1ReqBody) bean.getBody();
        parser.setStaffId(body.getOperatorId());
        parser.setJobType(body.getJobType());

        return message;
    }
}

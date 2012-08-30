package org.n3r.mina.listener;

import org.junit.AfterClass;
import org.junit.Test;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.mina.JCBytesParserFactory;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.req.IF3ReqBody;
import org.n3r.mina.bean.rsp.IF3RspBody;
import org.n3r.mina.utils.JCMessageUtils;
import org.phw.core.lang.Collections;
import org.phw.ibatis.engine.PDao;
import org.phw.ibatis.util.PDaoEngines;

import static org.junit.Assert.*;

public class JCClientSendListenerTest {

    private final static String SQL_XML = "org/n3r/mina/listener/JCListenererSQL.xml";
    private static PDao dao = PDaoEngines.getDao(SQL_XML, "Eop");

    @Test
    public void test1() throws Exception {
        JCMessageHead head = new JCMessageHead();
        head.setSessionId("20120813150412345678");
        head.setTypeFlag("02");
        IF3ReqBody body = new IF3ReqBody();
        body.setData("1234567890ABCDEF");

        JCMessage bean = new JCMessage(head, body);
        byte[] bytes = new BeanToBytes<JCMessage>().toBytes(bean, null);

        JCBytesParserFactory parser = new JCBytesParserFactory();
        parser.setSessionId("20120813150412345678");
        parser.setJobType("");

        JCClientSendListener listener = new JCClientSendListener(null);
        Object result = listener.process(bean, parser);

        JCMessageHead rspHead = new JCMessageHead();
        rspHead.setSessionId("20120813150412345678");
        rspHead.setTypeFlag("02");
        IF3RspBody rspBody = new IF3RspBody();
        rspBody.setResult("0000");
        JCMessage rspBean = new JCMessage(rspHead, rspBody);
        byte[] rspBytes = JCMessageUtils.messageToBytes(rspBean, null);

        assertArrayEquals(rspBytes, (byte[]) result);
    }

    @AfterClass
    public static void teardown() {
        dao.delete("JCListenerSQL.deleteRspData", Collections.asMap("SESSIONID", "20120813150412345678"));
    }

}

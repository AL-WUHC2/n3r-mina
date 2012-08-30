package org.n3r.mina.listener;

import org.junit.Test;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.mina.JCBytesParserFactory;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.req.IF3ReqBody;

import static org.junit.Assert.*;

public class JCClientReceiveListenerTest {

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
        parser.setJobType("");

        JCClientReceiveListener listener = new JCClientReceiveListener(null);
        Object result = listener.process(bytes, parser);

        assertEquals(bean, result);
    }
}

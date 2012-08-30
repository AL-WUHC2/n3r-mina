package org.n3r.mina.listener;

import org.junit.Test;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.mina.JCBytesParserFactory;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.rsp.IF2RspBody;

import static org.junit.Assert.*;

public class JCServerReceiveListenerTest {

    @Test
    public void test1() throws Exception {
        JCMessageHead head = new JCMessageHead();
        head.setSessionId("20120813150412345678");
        head.setTypeFlag("01");
        IF2RspBody body = new IF2RspBody();
        body.setResult("0000");
        body.setRapdu("0123456789ABCDEF");

        JCMessage bean = new JCMessage(head, body);
        byte[] bytes = new BeanToBytes<JCMessage>().toBytes(bean, null);

        JCBytesParserFactory parser = new JCBytesParserFactory();
        parser.setJobType("");

        JCServerReceiveListener listener = new JCServerReceiveListener(null);
        Object result = listener.process(bytes, parser);

        assertEquals(bean, result);
    }

}

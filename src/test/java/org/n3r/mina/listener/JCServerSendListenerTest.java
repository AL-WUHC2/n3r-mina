package org.n3r.mina.listener;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.n3r.mina.JCBytesParserFactory;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.req.IF2ReqBody;
import org.n3r.mina.utils.JCMessageUtils;
import org.phw.core.lang.Collections;
import org.phw.ibatis.engine.PDao;
import org.phw.ibatis.util.PDaoEngines;

import static org.junit.Assert.*;

public class JCServerSendListenerTest {

    private final static String SQL_XML = "org/n3r/mina/listener/JCListenererSQL.xml";
    private static PDao dao = PDaoEngines.getDao(SQL_XML, "Eop");

    @BeforeClass
    public static void setup() {
        dao.insert("JCListenerSQL.insertTestData", Collections.asMap(
                "BATCH_ID", "87654321", "STAFF_ID", "999001", "ORDER_NO", 1, "JOB_TYPE", 1, "DEFAULT_TAG", "0",
                "CONTENT", "{\"capdu\":\"0123456789ABCDEF\"}", "SLEEP_TIME", 1000, "RESP_IF_NO", "IF2"));
    }

    @AfterClass
    public static void teardown() {
        dao.delete("JCListenerSQL.deleteTestData", Collections.asMap("BATCH_ID", "87654321"));
    }

    @Test
    public void test1() throws Exception {
        JCMessageHead head = new JCMessageHead();
        head.setSessionId("201208131504098765");
        head.setTypeFlag("01");
        IF2ReqBody body = new IF2ReqBody();
        body.setCapdu("0123456789ABCDEF");

        byte[] bytes = JCMessageUtils.messageToBytes(head, body, null);

        JCBytesParserFactory parser = new JCBytesParserFactory();
        parser.setSessionId("201208131504098765");
        parser.setJobType("01");
        parser.setStaffId("999001");
        parser.setOrderNo(1);

        JCServerSendListener listener = new JCServerSendListener(null);
        Object result = listener.process(new byte[0], parser);

        assertArrayEquals(bytes, (byte[]) result);
    }
}

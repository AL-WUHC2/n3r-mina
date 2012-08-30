package org.n3r.mina;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.req.IF1ReqBody;
import org.n3r.mina.client.JCClientHandler;
import org.n3r.mina.client.MinaClient;
import org.n3r.mina.server.JCServerHandler;
import org.n3r.mina.utils.JCMessageUtils;
import org.phw.core.lang.Collections;
import org.phw.ibatis.engine.PDao;
import org.phw.ibatis.util.PDaoEngines;

import static org.phw.config.impl.PhwConfigMgrFactory.*;

public class JavacardTest {

    private static IoAcceptor acceptor;

    private static final int PORT = 9123;

    private final static String SQL_XML = "org/n3r/mina/listener/JCListenererSQL.xml";
    private static PDao dao = PDaoEngines.getDao(SQL_XML, "Eop");

    @Test
    public void test1() {
        dao.insert("JCListenerSQL.insertTestData", Collections.asMap(
                "BATCH_ID", "87654321", "STAFF_ID", "999001", "ORDER_NO", 1, "JOB_TYPE", 1, "DEFAULT_TAG", "1",
                "CONTENT", "{\"capdu\":\"0123456789ABCDEF\"}", "SLEEP_TIME", 1000, "RESP_IF_NO", "IF2"));
        dao.insert("JCListenerSQL.insertTestData", Collections.asMap(
                "BATCH_ID", "87654321", "STAFF_ID", "999001", "ORDER_NO", 2, "JOB_TYPE", 1, "DEFAULT_TAG", "1",
                "CONTENT", "{\"result\":\"0000\",\"merchantName\":\"提供商\",\"enableSpace\":\"000011\"," +
                        "\"userFlag\":\"01\",\"appList\":[{\"appName\":\"应用名\",\"appAid\":\"999001\"," +
                        "\"appSize\":\"0011\",\"appOperateType\":\"01\",\"provider\":\"提供者\"," +
                        "\"productId\":\"340199999001\",\"feeDesc\":\"\"}]}",
                "SLEEP_TIME", 1000, "RESP_IF_NO", "IF1"));
        dao.insert("JCListenerSQL.insertRspData", Collections.asMap(
                "ID", "120829100001", "SESSIONID", "20120726001122709348", "RSP_RESULT", "0000",
                "RSPDATA", "0123456789ABCDEF", "RSP_TYPE", "0", "IF_NO", "IF2"));

        JCMessageHead head = new JCMessageHead();
        head.setSessionId("20120726001122709348");
        head.setTypeFlag("00");
        IF1ReqBody body = new IF1ReqBody();
        body.setOperatorId("000088");
        body.setProvince("34");
        body.setCity("340");
        body.setDistrict("34A700");
        body.setChannelCode("Z0025");
        body.setChannelType("1020100");
        body.setIdentity("452228193202030045");
        body.setMsisdn("11111111111");
        body.setIccid("86890123456789012345");
        body.setImsi("");
        body.setOperateType("02");
        body.setProductIds(Arrays.asList("3420110722094128", "3409091800001055"));
        body.setJobType("01");
        body.setRequestData("");
        byte[] bytes = JCMessageUtils.messageToBytes(head, body, null);

        MinaClient client = new MinaClient("127.0.0.1", PORT, new JCClientHandler());
        client.sendMessage(bytes);
        client.awaitClose();

        dao.delete("JCListenerSQL.deleteTestData", Collections.asMap("BATCH_ID", "87654321"));
        dao.delete("JCListenerSQL.deleteRspData", Collections.asMap("SESSIONID", "20120726001122709348"));
        dao.delete("JCListenerSQL.deleteRspSubData", new HashMap<Object, Object>());
    }

    @Test
    public void test2() {
        dao.insert("JCListenerSQL.insertTestData", Collections.asMap(
                "BATCH_ID", "87654321", "STAFF_ID", "999001", "ORDER_NO", 1, "JOB_TYPE", 2, "DEFAULT_TAG", "1",
                "CONTENT", "{\"capdu\":\"0123456789ABCDEF\"}", "SLEEP_TIME", 1000, "RESP_IF_NO", "IF2"));
        dao.insert("JCListenerSQL.insertTestData", Collections.asMap(
                "BATCH_ID", "87654321", "STAFF_ID", "999001", "ORDER_NO", 2, "JOB_TYPE", 2, "DEFAULT_TAG", "1",
                "CONTENT", "{\"result\":\"0000\",\"appList\":[{\"appName\":\"应用名\",\"appAid\":\"999001\"," +
                        "\"appSize\":\"0011\",\"appOperateType\":\"01\",\"provider\":\"提供者\"," +
                        "\"productId\":\"340199999001\",\"feeDesc\":\"\"}]}",
                "SLEEP_TIME", 1000, "RESP_IF_NO", "IF1"));
        dao.insert("JCListenerSQL.insertRspData", Collections.asMap(
                "ID", "120829100001", "SESSIONID", "20120726001122709348", "RSP_RESULT", "0000",
                "RSPDATA", "0123456789ABCDEF", "RSP_TYPE", "0", "IF_NO", "IF2"));

        JCMessageHead head = new JCMessageHead();
        head.setSessionId("20120726001122709348");
        head.setTypeFlag("00");
        IF1ReqBody body = new IF1ReqBody();
        body.setOperatorId("000088");
        body.setProvince("34");
        body.setCity("340");
        body.setDistrict("34A700");
        body.setChannelCode("Z0025");
        body.setChannelType("1020100");
        body.setIdentity("452228193202030045");
        body.setMsisdn("11111111111");
        body.setIccid("86890123456789012345");
        body.setImsi("");
        body.setOperateType("02");
        body.setProductIds(Arrays.asList("3420110722094128", "3409091800001055"));
        body.setJobType("02");
        body.setRequestData("");
        byte[] bytes = JCMessageUtils.messageToBytes(head, body, null);

        MinaClient client = new MinaClient("127.0.0.1", PORT, new JCClientHandler());
        client.sendMessage(bytes);
        client.awaitClose();

        dao.delete("JCListenerSQL.deleteTestData", Collections.asMap("BATCH_ID", "87654321"));
        dao.delete("JCListenerSQL.deleteRspData", Collections.asMap("SESSIONID", "20120726001122709348"));
        dao.delete("JCListenerSQL.deleteRspSubData", new HashMap<Object, Object>());
    }

    @Test
    public void test3() {
        dao.insert("JCListenerSQL.insertTestData", Collections.asMap(
                "BATCH_ID", "87654321", "STAFF_ID", "999001", "ORDER_NO", 1, "JOB_TYPE", 4, "DEFAULT_TAG", "1",
                "CONTENT", "{}", "SLEEP_TIME", 1000, "RESP_IF_NO", "IF4"));
        dao.insert("JCListenerSQL.insertTestData", Collections.asMap(
                "BATCH_ID", "87654321", "STAFF_ID", "999001", "ORDER_NO", 2, "JOB_TYPE", 4, "DEFAULT_TAG", "1",
                "CONTENT", "{\"result\":\"0000\"}", "SLEEP_TIME", 1000, "RESP_IF_NO", "IF1"));
        dao.insert("JCListenerSQL.insertRspData", Collections.asMap(
                "ID", "120829100001", "SESSIONID", "20120726001122709348", "RSP_RESULT", "0000",
                "RSPDATA", "0123456789ABCDEF", "RSP_TYPE", "0", "IF_NO", "IF4"));

        JCMessageHead head = new JCMessageHead();
        head.setSessionId("20120726001122709348");
        head.setTypeFlag("00");
        IF1ReqBody body = new IF1ReqBody();
        body.setOperatorId("000088");
        body.setProvince("34");
        body.setCity("340");
        body.setDistrict("34A700");
        body.setChannelCode("Z0025");
        body.setChannelType("1020100");
        body.setIdentity("452228193202030045");
        body.setMsisdn("11111111111");
        body.setIccid("86890123456789012345");
        body.setImsi("");
        body.setOperateType("02");
        body.setProductIds(Arrays.asList("3420110722094128", "3409091800001055"));
        body.setJobType("04");
        body.setRequestData("");
        byte[] bytes = JCMessageUtils.messageToBytes(head, body, null);

        MinaClient client = new MinaClient("127.0.0.1", PORT, new JCClientHandler());
        client.sendMessage(bytes);
        client.awaitClose();

        dao.delete("JCListenerSQL.deleteTestData", Collections.asMap("BATCH_ID", "87654321"));
        dao.delete("JCListenerSQL.deleteRspData", Collections.asMap("SESSIONID", "20120726001122709348"));
        dao.delete("JCListenerSQL.deleteRspSubData", new HashMap<Object, Object>());
    }

    @Test
    public void testExceptionIdle() {
        dao.insert("JCListenerSQL.insertTestData", Collections.asMap(
                "BATCH_ID", "87654321", "STAFF_ID", "999001", "ORDER_NO", 1, "JOB_TYPE", 1, "DEFAULT_TAG", "1",
                "CONTENT", "{\"capdu\":\"0123456789ABCDEF\"}", "SLEEP_TIME", 1000, "RESP_IF_NO", "IF2"));

        JCMessageHead head = new JCMessageHead();
        head.setSessionId("20120726001122709348");
        head.setTypeFlag("00");
        IF1ReqBody body = new IF1ReqBody();
        body.setOperatorId("000088");
        body.setProvince("34");
        body.setCity("340");
        body.setDistrict("34A700");
        body.setChannelCode("Z0025");
        body.setChannelType("1020100");
        body.setIdentity("452228193202030045");
        body.setMsisdn("11111111111");
        body.setIccid("86890123456789012345");
        body.setImsi("");
        body.setOperateType("02");
        body.setProductIds(Arrays.asList("3420110722094128", "3409091800001055"));
        body.setJobType("01");
        body.setRequestData("");
        byte[] bytes = JCMessageUtils.messageToBytes(head, body, null);

        MinaClient client = new MinaClient("127.0.0.1", PORT, new JCClientHandler());
        client.sendMessage(bytes);
        client.awaitClose();

        dao.delete("JCListenerSQL.deleteTestData", Collections.asMap("BATCH_ID", "87654321"));
        dao.delete("JCListenerSQL.deleteRspData", Collections.asMap("SESSIONID", "20120726001122709348"));
    }

    @BeforeClass
    public static void startServer() throws IOException {
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.setHandler(new JCServerHandler());
        acceptor.getSessionConfig().setReadBufferSize(2048);
        int idle = getConfigMgr().getInt("MinaIdleTime", 60);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, idle);
        acceptor.bind(new InetSocketAddress(PORT));
        System.out.println("MinaServer started on port " + PORT);
    }

    @AfterClass
    public static void closeServer() {
        acceptor.unbind();
        acceptor.dispose(true);
    }

}

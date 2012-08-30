package org.n3r.mina.listener;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.n3r.mina.BytesParserFactory;
import org.n3r.mina.JCBytesParserFactory;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.req.IF1ReqBody;
import org.n3r.mina.utils.JCMessageUtils;

import static org.junit.Assert.*;

public class JCSessionCreateListenerTest {

    @Test
    public void test1() throws Exception {
        JCMessageHead head = new JCMessageHead();
        head.setSessionId("20120813150412345678");
        head.setTypeFlag("00");
        IF1ReqBody body = new IF1ReqBody();
        body.setOperatorId("000088");
        body.setProvince("34");
        body.setCity("340");
        body.setDistrict("340000");
        body.setChannelCode("Z000025");
        body.setChannelType("02");
        body.setIdentity("123456789012345678");
        body.setMsisdn("12345678901");
        body.setIccid("12345678901234567890");
        body.setImsi("1234567812345678");
        body.setOperateType("01");
        body.setProductIds(Arrays.asList("980001", "980002"));
        body.setJobType("01");
        body.setRequestData("");
        byte[] bytes = JCMessageUtils.messageToBytes(head, body, null);

        Map<Long, BytesParserFactory> parserMap = new HashMap<Long, BytesParserFactory>();
        parserMap.put(1L, new JCBytesParserFactory());

        JCBytesParserFactory tempParser = new JCBytesParserFactory();
        tempParser.addListener(new JCSessionCreateListener());
        tempParser.parseBytes(bytes);

        if (tempParser.getOrderNo() == 1) {
            tempParser.cleanListeners();
            parserMap.put(1L, tempParser);
        }

        JCBytesParserFactory temp2Parser = (JCBytesParserFactory) parserMap.get(1L);
        assertEquals("20120813150412345678", temp2Parser.getSessionId());
        assertEquals("01", temp2Parser.getJobType());
        assertEquals("000088", temp2Parser.getStaffId());
    }
}

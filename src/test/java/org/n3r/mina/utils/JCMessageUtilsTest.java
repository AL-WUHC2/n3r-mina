package org.n3r.mina.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.lang.RHex;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.req.IF4ReqBody;
import org.n3r.mina.bean.rsp.AppItem;
import org.n3r.mina.bean.rsp.IF101RspBody;
import org.n3r.mina.bean.rsp.IF102RspBody;
import org.n3r.mina.bean.rsp.IF104RspBody;

import static org.junit.Assert.*;

import static org.n3r.beanbytes.utils.BeanBytesUtils.*;
import static org.n3r.core.lang.RByte.*;

public class JCMessageUtilsTest {

    @Test
    public void testIF101Message() {
        JCMessageHead head = new JCMessageHead();
        head.setSessionId("20120813150412345678");
        head.setTypeFlag("00");
        IF101RspBody body = new IF101RspBody();
        body.setResult("0000");
        body.setMerchantName("提供商");
        body.setEnableSpace("00001213");
        body.setUserFlag("00");
        body.setAppList(new ArrayList<AppItem>());
        StringBuilder printer = new StringBuilder();

        byte[] actual = JCMessageUtils.messageToBytes(new JCMessage(head, body), printer);

        byte[] expected = add(toBytes("20120813150412345678"), toBytes((byte) 0),
                toBytes((short) 0), prependLen(toBytes("提供商", "Unicode"), 1),
                RHex.decode("00001213"), RHex.decode("00"), toBytes((short) 0));
        expected = BeanBytesUtils.prependLen(expected, 2);

        assertArrayEquals(expected, actual);
        assertEquals("{sessionId:20120813150412345678, typeFlag:00}" +
                "{result:0000, merchantName:提供商, enableSpace:00001213, userFlag:00, appList:[]}", printer.toString());

        actual = subBytes(actual, 2);
        ParseBean<JCMessage> messageFromBytes = JCMessageUtils.messageFromBytes(actual, "01", false);
        assertEquals(new JCMessage(head, body), messageFromBytes.getBean());
    }

    @Test
    public void testIF102Message() {
        JCMessageHead head = new JCMessageHead();
        head.setSessionId("20120813150412345678");
        head.setTypeFlag("00");
        IF102RspBody body = new IF102RspBody();
        body.setResult("0000");
        AppItem appItem1 = new AppItem();
        appItem1.setAppName("应用一");
        appItem1.setAppAid("10000001");
        appItem1.setAppSize("0011");
        appItem1.setAppOperateType("80");
        appItem1.setProvider("提供商一");
        appItem1.setProductId("I001");
        appItem1.setFeeDesc("0FFD");
        AppItem appItem2 = new AppItem();
        appItem2.setAppName("应用二");
        appItem2.setAppAid("10000002");
        appItem2.setAppSize("0022");
        appItem2.setAppOperateType("08");
        appItem2.setProvider("提供商二");
        appItem2.setProductId("I002");
        appItem2.setFeeDesc("0FFE");
        List<AppItem> appList = Arrays.asList(appItem1, appItem2);
        body.setAppList(appList);
        StringBuilder printer = new StringBuilder();

        byte[] actual = JCMessageUtils.messageToBytes(new JCMessage(head, body), printer);

        byte[] expected = add(toBytes("20120813150412345678"), toBytes((byte) 0), toBytes((short) 0));
        byte[] app1Bytes = add(prependLen(toBytes("应用一", "Unicode"), 1), prependLen(RHex.decode("10000001"), 1),
                RHex.decode("0011"), RHex.decode("80"), prependLen(toBytes("提供商一", "Unicode"), 1),
                prependLen(toBytes("I001"), 1), prependLen(RHex.decode("0FFD"), 1));
        byte[] app2Bytes = add(prependLen(toBytes("应用二", "Unicode"), 1), prependLen(RHex.decode("10000002"), 1),
                RHex.decode("0022"), RHex.decode("08"), prependLen(toBytes("提供商二", "Unicode"), 1),
                prependLen(toBytes("I002"), 1), prependLen(RHex.decode("0FFE"), 1));
        expected = add(expected, prependLen(add(app1Bytes, app2Bytes), 2, 2));
        expected = BeanBytesUtils.prependLen(expected, 2);

        assertArrayEquals(expected, actual);
        assertEquals("{sessionId:20120813150412345678, typeFlag:00}{result:0000, appList:[" +
                "{appName:应用一, appAid:10000001, appSize:0011, appOperateType:80, " +
                "provider:提供商一, productId:I001, feeDesc:0FFD}, " +
                "{appName:应用二, appAid:10000002, appSize:0022, appOperateType:08, " +
                "provider:提供商二, productId:I002, feeDesc:0FFE}]}", printer.toString());

        actual = subBytes(actual, 2);
        ParseBean<JCMessage> messageFromBytes = JCMessageUtils.messageFromBytes(actual, "03", false);
        assertEquals(new JCMessage(head, body), messageFromBytes.getBean());
    }

    @Test
    public void testIF104Message() {
        JCMessageHead head = new JCMessageHead();
        head.setSessionId("20120813150412345678");
        head.setTypeFlag("00");
        IF104RspBody body = new IF104RspBody();
        body.setResult("0000");
        StringBuilder printer = new StringBuilder();

        byte[] actual = JCMessageUtils.messageToBytes(new JCMessage(head, body), printer);

        byte[] expected = add(toBytes("20120813150412345678"), toBytes((byte) 0), toBytes((short) 0));
        expected = BeanBytesUtils.prependLen(expected, 2);

        assertArrayEquals(expected, actual);
        assertEquals("{sessionId:20120813150412345678, typeFlag:00}{result:0000}", printer.toString());

        actual = subBytes(actual, 2);
        ParseBean<JCMessage> messageFromBytes = JCMessageUtils.messageFromBytes(actual, "04", false);
        assertEquals(new JCMessage(head, body), messageFromBytes.getBean());
    }

    @Test
    public void testIF4Message() {
        JCMessageHead head = new JCMessageHead();
        head.setSessionId("20120813150412345678");
        head.setTypeFlag("03");
        IF4ReqBody body = new IF4ReqBody();
        StringBuilder printer = new StringBuilder();

        JCMessage message = new JCMessage();
        message.setHead(head);
        message.setBody(body);
        byte[] actual = JCMessageUtils.messageToBytes(message, printer);

        byte[] expected = add(toBytes("20120813150412345678"), toBytes((byte) 3));
        expected = BeanBytesUtils.prependLen(expected, 2);

        assertArrayEquals(expected, actual);
        assertEquals("{sessionId:20120813150412345678, typeFlag:03}{}", printer.toString());

        actual = subBytes(actual, 2);
        ParseBean<JCMessage> messageFromBytes = JCMessageUtils.messageFromBytes(actual, "01", false);
        JCMessage fromMessage = messageFromBytes.getBean();
        assertEquals(head.getSessionId(), fromMessage.getHead().getSessionId());
        assertEquals(body, fromMessage.getBody());
    }

    @Test
    public void test0() {
        assertNotNull(new JCMessageUtils());

        assertArrayEquals(new byte[0], JCMessageUtils.messageToBytes(null, null));
    }

}

package org.n3r.mina.client;

import java.util.Arrays;

import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.req.IF1ReqBody;
import org.n3r.mina.utils.JCMessageUtils;

import static org.n3r.core.lang.RByte.*;

public class MinaClientDemo {

    public static void main(String[] args) throws Exception {
        JCMessageHead head = new JCMessageHead();
        head.setSessionId("20120726001122709348");
        head.setTypeFlag("00");
        IF1ReqBody body = new IF1ReqBody();
        body.setOperatorId("98");
        body.setProvince("98");
        body.setCity("980");
        body.setDistrict("9800000000");
        body.setChannelCode("1001");
        body.setChannelType("1");
        body.setIdentity("1234567890");
        body.setMsisdn("qwertyuiop0");
        body.setIccid("1234567890");
        body.setImsi("98765432");
        body.setOperateType("02");
        body.setProductIds(Arrays.asList("900001", "900002"));
        body.setJobType("05");
        body.setRequestData("1234567890ABCDEF");
        byte[] bytes = JCMessageUtils.messageToBytes(head, body, null);
        byte[] subBytes1 = subBytes(bytes, 0, 40);
        byte[] subBytes2 = subBytes(bytes, 40);
        byte[] nullBytes = toBytes((short) 0);
        MinaClient client = new MinaClient();
        client.sendMessage(subBytes1);
        Thread.sleep(10000);
        client.sendMessage(add(subBytes2, nullBytes));
        // client.sendMessage(nullBytes);
        client.awaitClose();
    }
}

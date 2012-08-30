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
        byte[] subBytes1 = subBytes(bytes, 0, 40);
        byte[] subBytes2 = subBytes(bytes, 40);
        byte[] nullBytes = toBytes((short) 0);
        MinaClient client = new MinaClient("219.239.243.139", 9002, new JCClientHandler());
        client.sendMessage(bytes);
        //        client.sendMessage(subBytes1);
        //        Thread.sleep(10000);
        //        client.sendMessage(add(subBytes2, nullBytes));
        // client.sendMessage(nullBytes);
        client.awaitClose();
    }
}

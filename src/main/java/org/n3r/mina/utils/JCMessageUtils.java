package org.n3r.mina.utils;

import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.joor.Reflect;
import org.n3r.core.lang.RByte;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageBody;
import org.n3r.mina.bean.JCMessageHead;

public class JCMessageUtils {

    public static byte[] messageToBytes(JCMessage message, StringBuilder printer) {
        return messageToBytes(message.getHead(), message.getBody(), printer);
    }

    public static byte[] messageToBytes(JCMessageHead head, Object body, StringBuilder printer) {
        BeanToBytes<JCMessageHead> headToBytes = new BeanToBytes<JCMessageHead>();
        BeanToBytes<Object> bodyToBytes = new BeanToBytes<Object>();

        byte[] result = RByte.add(headToBytes.toBytes(head, printer), bodyToBytes.toBytes(body, printer));
        return BeanBytesUtils.prependLen(result, 2);
    }

    public static ParseBean<JCMessage> reqMessageFromBytes(byte[] bytes) {
        ParseBean<JCMessageHead> head = parseMessageHead(bytes);

        String typeFlag = JCTypeUtils.getTypeIFString(head.getBean().getTypeFlag());
        Class<?> bodyType = parseReqBodyType(typeFlag);

        return parseMessage(bytes, head, bodyType);
    }

    public static ParseBean<JCMessage> rspMessageFromBytes(byte[] bytes, String jobType) {
        ParseBean<JCMessageHead> head = parseMessageHead(bytes);

        String typeFlag = JCTypeUtils.getTypeIFString(head.getBean().getTypeFlag());
        Class<?> bodyType = parseRspBodyType(typeFlag, jobType);

        return parseMessage(bytes, head, bodyType);
    }

    public static ParseBean<JCMessageHead> parseMessageHead(byte[] bytes) {
        BeanFromBytes<JCMessageHead> headFromBytes = new BeanFromBytes<JCMessageHead>();
        return headFromBytes.fromBytes(bytes, JCMessageHead.class, 0);
    }

    public static ParseBean<JCMessage> parseMessage(byte[] bytes, ParseBean<JCMessageHead> head, Class<?> bodyType) {
        BeanFromBytes<JCMessageBody> bodyFromBytes = new BeanFromBytes<JCMessageBody>();
        ParseBean<JCMessageBody> body = bodyFromBytes.fromBytes(bytes, bodyType, head.getBytesSize());

        JCMessage messageBean = new JCMessage(head.getBean(), body.getBean());
        return new ParseBean<JCMessage>(messageBean, head.getBytesSize() + body.getBytesSize());
    }

    public static Class<?> parseReqBodyType(String typeFlag) {
        String bodyName = "org.n3r.mina.bean.req." + typeFlag + "ReqBody";
        return Reflect.on(bodyName).type();
    }

    public static Class<?> parseRspBodyType(String typeFlag, String jobType) {
        if (typeFlag.equals("IF1")) {
            if (jobType.equals("01")) typeFlag += "01";
            else if (jobType.equals("02") || jobType.equals("03")) typeFlag += "02";
            else typeFlag += "04";
        }
        String bodyName = "org.n3r.mina.bean.rsp." + typeFlag + "RspBody";
        return Reflect.on(bodyName).type();
    }

}

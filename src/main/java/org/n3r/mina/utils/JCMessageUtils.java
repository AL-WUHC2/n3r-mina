package org.n3r.mina.utils;

import org.n3r.beanbytes.ParseBean;
import org.n3r.beanbytes.impl.BeanFromBytes;
import org.n3r.beanbytes.impl.BeanToBytes;
import org.n3r.beanbytes.utils.BeanBytesUtils;
import org.n3r.core.joor.Reflect;
import org.n3r.core.lang.RByte;
import org.n3r.core.lang.RStr;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageBody;
import org.n3r.mina.bean.JCMessageHead;

public class JCMessageUtils {

    /**
     * Translate {@link JCMessage} to Byte array.
     */
    public static byte[] messageToBytes(JCMessage message, StringBuilder printer) {
        if (message == null) return new byte[0];
        return messageToBytes(message.getHead(), message.getBody(), printer);
    }

    /**
     * Translate {@link JCMessage} to Byte array.
     */
    public static byte[] messageToBytes(JCMessageHead head, Object body, StringBuilder printer) {
        BeanToBytes<JCMessageHead> headToBytes = new BeanToBytes<JCMessageHead>();
        BeanToBytes<Object> bodyToBytes = new BeanToBytes<Object>();

        byte[] result = RByte.add(headToBytes.toBytes(head, printer), bodyToBytes.toBytes(body, printer));
        return BeanBytesUtils.prependLen(result, 2);
    }

    /**
     * Parse Byte array to {@link JCMessage}.
     */
    public static ParseBean<JCMessage> messageFromBytes(byte[] bytes, String jobType, boolean atServer) {
        ParseBean<JCMessageHead> head = parseMessageHead(bytes);
        String ifNo = JCTypeUtils.getMsgTypeIFString(head.getBean().getTypeFlag());

        Class<?> bodyType = parseBodyType(ifNo, jobType, atServer);

        return parseMessage(bytes, head, bodyType);
    }

    public static Class<?> parseBodyType(String ifNo, String jobType, boolean atServer) {
        return ifNo.equals("IF1") == atServer ? parseReqBodyType(ifNo) : parseRspBodyType(ifNo, jobType);
    }

    /**
     * Parse {@link JCMessage} Head.
     */
    public static ParseBean<JCMessageHead> parseMessageHead(byte[] bytes) {
        BeanFromBytes<JCMessageHead> headFromBytes = new BeanFromBytes<JCMessageHead>();
        return headFromBytes.fromBytes(bytes, JCMessageHead.class, 0);
    }

    /**
     * Parse {@link JCMessage} Body.
     */
    public static ParseBean<JCMessage> parseMessage(byte[] bytes, ParseBean<JCMessageHead> head, Class<?> bodyType) {
        BeanFromBytes<JCMessageBody> bodyFromBytes = new BeanFromBytes<JCMessageBody>();
        ParseBean<JCMessageBody> body = bodyFromBytes.fromBytes(bytes, bodyType, head.getBytesSize());

        JCMessage messageBean = new JCMessage(head.getBean(), body.getBean());
        return new ParseBean<JCMessage>(messageBean, head.getBytesSize() + body.getBytesSize());
    }

    /**
     * Parse Request {@link JCMessage} Body Type.
     */
    public static Class<?> parseReqBodyType(String typeFlag) {
        String bodyName = "org.n3r.mina.bean.req." + typeFlag + "ReqBody";
        return Reflect.on(bodyName).type();
    }

    /**
     * Parse Response {@link JCMessage} Body Type.
     */
    public static Class<?> parseRspBodyType(String typeFlag, String jobType) {
        if (typeFlag.equals("IF1")) {
            if (jobType.equals("01")) typeFlag += "01";
            else if (RStr.in(jobType, "02", "03")) typeFlag += "02";
            else typeFlag += "04";
        }
        String bodyName = "org.n3r.mina.bean.rsp." + typeFlag + "RspBody";
        return Reflect.on(bodyName).type();
    }

}

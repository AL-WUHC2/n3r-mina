package org.n3r.mina;

import org.apache.mina.core.buffer.IoBuffer;
import org.n3r.beanbytes.impl.BeanFromBytes;

import static org.n3r.core.lang.RByte.*;

public class JCSession {

    private String sessionId;

    private String staffId;

    private int orderNo;

    private String jobType;

    private byte[] messageBuffer;

    public JCSession() {
        orderNo = 0;
        messageBuffer = new byte[0];
    }

    public void incrementOrderNo() {
        orderNo++;
    }

    public void pushMessage(IoBuffer ioBuffer) {
        messageBuffer = pushMessage(messageBuffer, ioBuffer);
    }

    public byte[] popMessage() {
        byte[] result = popMessage(messageBuffer);
        if (result == null) return null;
        messageBuffer = subBytes(messageBuffer, 2 + result.length);
        return result;
    }

    public static byte[] pushMessage(byte[] buffer, IoBuffer ioBuffer) {
        if (buffer == null) buffer = new byte[0];
        byte[] b = new byte[ioBuffer.limit()];
        ioBuffer.get(b);
        return add(buffer, b);
    }

    public static byte[] popMessage(byte[] buffer) {
        if (buffer == null) return null;
        if (buffer.length < 2) return null;

        short len = new BeanFromBytes<Short>().fromBytes(buffer, Short.class, 0).getBean();
        if (buffer.length < 2 + len) return null;

        return subBytes(buffer, 2, len);
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobType() {
        return jobType;
    }

    public void setMessageBuffer(byte[] messageBuffer) {
        this.messageBuffer = messageBuffer;
    }

    public byte[] getMessageBuffer() {
        return messageBuffer;
    }

}

package org.n3r.mina;

import org.n3r.beanbytes.impl.BeanFromBytes;

import static org.n3r.core.lang.RByte.*;

public class JCBytesParserFactory extends BytesParserFactory {

    private String sessionId;

    private String staffId;

    private int orderNo;

    private String jobType;

    @Override
    public byte[] popMsgBytes() {
        if (buffer.length < 2) return null;

        short len = new BeanFromBytes<Short>().fromBytes(buffer, Short.class, 0).getBean();
        if (buffer.length < 2 + len) return null;

        byte[] result = subBytes(buffer, 2, len);
        buffer = subBytes(buffer, 2 + len);
        orderNo++;

        return result;
    }

    @Override
    public void pushRawBytes(byte[] bytes) {
        buffer = add(buffer, bytes);
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

}

package org.n3r.mina.bean.rsp;

import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.mina.bean.JCMessageBody;

public class IF5RspBody extends JCMessageBody {

    @JCFixLen(length = 2)
    private String result;

    @JCFixLen(length = 10)
    private String iccid;

    @JCFixLen(length = 8)
    private String imsi;

    @JCFixLen(length = 11, dataType = JCDataType.ASCII)
    private String msisdn;

    @JCFixLen(length = 2)
    private String userIDType;

    @JCVarLen(dataType = JCDataType.ASCII)
    private String userID;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getIccid() {
        return iccid;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getImsi() {
        return imsi;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setUserIDType(String userIDType) {
        this.userIDType = userIDType;
    }

    public String getUserIDType() {
        return userIDType;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserID() {
        return userID;
    }

}

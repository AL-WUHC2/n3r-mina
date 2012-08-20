package org.n3r.mina.bean;

import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.core.lang.RBaseBean;

public class JCMessageHead extends RBaseBean {

    @JCFixLen(dataType = JCDataType.ASCII, length = 20)
    private String sessionId;

    @JCFixLen(length = 1)
    private String typeFlag;

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setTypeFlag(String typeFlag) {
        this.typeFlag = typeFlag;
    }

    public String getTypeFlag() {
        return typeFlag;
    }

}

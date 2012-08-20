package org.n3r.mina.bean.rsp;

import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.mina.bean.JCMessageBody;

public class IF4RspBody extends JCMessageBody {

    @JCFixLen(length = 2)
    private String result;

    private String atr;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setAtr(String atr) {
        this.atr = atr;
    }

    public String getAtr() {
        return atr;
    }

}

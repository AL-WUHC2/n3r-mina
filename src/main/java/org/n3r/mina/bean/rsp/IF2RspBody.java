package org.n3r.mina.bean.rsp;

import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.mina.bean.JCMessageBody;

public class IF2RspBody extends JCMessageBody {

    @JCFixLen(length = 2)
    private String result;

    @JCVarLen(lenBytes = 2)
    private String rApdu;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setrApdu(String rApdu) {
        this.rApdu = rApdu;
    }

    public String getrApdu() {
        return rApdu;
    }

}

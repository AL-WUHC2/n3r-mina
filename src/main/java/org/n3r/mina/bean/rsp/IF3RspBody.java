package org.n3r.mina.bean.rsp;

import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.mina.bean.JCMessageBody;

public class IF3RspBody extends JCMessageBody {

    @JCFixLen(length = 2)
    private String result;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

}

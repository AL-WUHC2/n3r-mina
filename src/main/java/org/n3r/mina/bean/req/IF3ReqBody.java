package org.n3r.mina.bean.req;

import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.mina.bean.JCMessageBody;

public class IF3ReqBody extends JCMessageBody {

    @JCVarLen(lenBytes = 2)
    private String data;

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

}

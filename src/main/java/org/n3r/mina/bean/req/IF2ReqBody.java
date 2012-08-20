package org.n3r.mina.bean.req;

import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.mina.bean.JCMessageBody;

public class IF2ReqBody extends JCMessageBody {

    @JCVarLen(lenBytes = 2)
    private String cApdu;

    public void setcApdu(String cApdu) {
        this.cApdu = cApdu;
    }

    public String getcApdu() {
        return cApdu;
    }

}

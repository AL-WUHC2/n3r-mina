package org.n3r.mina.client.process;

import java.util.Map;

import org.n3r.core.lang.RStr;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.req.IF2ReqBody;
import org.n3r.mina.bean.rsp.IF2RspBody;
import org.phw.core.lang.Collections;
import org.phw.ibatis.engine.PDao;

public class IF2Process extends JCClientProcess {

    public IF2Process(PDao dao) {
        super(dao, "IF2");
    }

    @Override
    public Map fetchInsertParam(JCMessage message) {
        IF2ReqBody body = (IF2ReqBody) message.getBody();
        return Collections.asMap("RSPDATA", body.getCapdu());
    }

    @Override
    public JCMessage generateMessage(JCMessageHead head, Map param) {
        IF2RspBody if2RspBody = new IF2RspBody();
        if2RspBody.setResult(RStr.toStr(param.get("RSP_RESULT")));
        if2RspBody.setRapdu(RStr.toStr(param.get("RSPDATA")));
        return new JCMessage(head, if2RspBody);
    }

}

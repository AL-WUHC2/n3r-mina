package org.n3r.mina.client.process;

import java.util.Map;

import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.req.IF3ReqBody;
import org.n3r.mina.bean.rsp.IF3RspBody;
import org.phw.core.lang.Collections;
import org.phw.ibatis.engine.PDao;

public class IF3Process extends JCClientProcess {

    public IF3Process(PDao dao) {
        super(dao, "IF3");
    }

    @Override
    public Map fetchInsertParam(JCMessage message) {
        IF3ReqBody body = (IF3ReqBody) message.getBody();
        return Collections.asMap("RSPDATA", body.getData());
    }

    @Override
    protected Map queryClientMessage(Map insertParam) throws Exception {
        return null;
    }

    @Override
    public JCMessage generateMessage(JCMessageHead head, Map param) {
        IF3RspBody if3RspBody = new IF3RspBody();;
        if3RspBody.setResult("0000");
        return new JCMessage(head, if3RspBody);
    }

}

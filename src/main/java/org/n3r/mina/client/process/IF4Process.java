package org.n3r.mina.client.process;

import java.util.HashMap;
import java.util.Map;

import org.n3r.core.lang.RStr;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.rsp.IF4RspBody;
import org.phw.ibatis.engine.PDao;

public class IF4Process extends JCClientProcess {

    public IF4Process(PDao dao) {
        super(dao, "IF4");
    }

    @Override
    public Map fetchInsertParam(JCMessage message) {
        return new HashMap();
    }

    @Override
    public JCMessage generateMessage(JCMessageHead head, Map param) {
        IF4RspBody if4RspBody = new IF4RspBody();
        if4RspBody.setResult(RStr.toStr(param.get("RSP_RESULT")));
        if4RspBody.setAtr(RStr.toStr(param.get("RSPDATA")));
        return new JCMessage(head, if4RspBody);
    }

}

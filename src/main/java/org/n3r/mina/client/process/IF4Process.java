package org.n3r.mina.client.process;

import java.util.Map;

import org.n3r.mina.JCSession;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageHead;
import org.phw.ibatis.engine.PDao;

public class IF4Process extends JCClientProcess {

    public IF4Process(PDao dao) {
        super(dao, "IF4");
    }

    @Override
    public Map fetchInsertParam(JCMessage message) {
        return null;
    }

    @Override
    public JCMessage generateMessage(JCMessageHead head, Map param, JCSession jcSession) {
        return null;
    }

}

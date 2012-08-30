package org.n3r.mina.client.process;

import java.util.Map;

import org.n3r.core.lang.RStr;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.rsp.IF101RspBody;
import org.n3r.mina.bean.rsp.IF102RspBody;
import org.n3r.mina.bean.rsp.IF104RspBody;
import org.phw.core.lang.Collections;
import org.phw.ibatis.engine.PDao;

public class IF1Process extends JCClientProcess {

    private String jobType;

    public IF1Process(PDao dao, String jobType) {
        super(dao, "IF1");
        this.jobType = jobType;
    }

    @Override
    public Map fetchInsertParam(JCMessage message) {
        if (jobType.equals("01")) {
            IF101RspBody body = (IF101RspBody) message.getBody();
            return Collections.asMap("RSP_RESULT", body.getResult(), "CARD_PRODUCT_NAME",
                    body.getMerchantName(), "USERFLAG", body.getUserFlag(), "appList", body.getAppList());

        }
        else if (RStr.in(jobType, "02", "03")) {
            IF102RspBody body = (IF102RspBody) message.getBody();
            return Collections.asMap("RSP_RESULT", body.getResult(), "appList", body.getAppList());

        }
        else {
            IF104RspBody body = (IF104RspBody) message.getBody();
            return Collections.asMap("RSP_RESULT", body.getResult());

        }
    }

    @Override
    public JCMessage generateMessage(JCMessageHead head, Map param) {
        return null;
    }

}

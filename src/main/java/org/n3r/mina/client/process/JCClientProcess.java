package org.n3r.mina.client.process;

import java.util.List;
import java.util.Map;

import org.n3r.mina.JCSession;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.utils.JCTypeUtils;
import org.phw.core.lang.Collections;
import org.phw.ibatis.engine.PDao;

public abstract class JCClientProcess {

    private PDao dao;

    protected String ifNo;

    public JCClientProcess(PDao dao, String ifNo) {
        this.dao = dao;
        this.ifNo = ifNo;
    }

    public abstract Map fetchInsertParam(JCMessage message);

    public abstract JCMessage generateMessage(JCMessageHead head, Map param, JCSession jcSession);

    public JCMessage process(JCMessage message, JCSession jcSession) throws Exception {
        Map insertParam = fetchInsertParam(message);

        recordResponseInfo(insertParam);

        if (ifNo.equals("IF1")) { return null; }

        Map responseInfo = queryRequestInfo(insertParam);
        JCMessageHead rspHead = new JCMessageHead();
        rspHead.setSessionId(jcSession.getSessionId());
        rspHead.setTypeFlag(JCTypeUtils.getType(ifNo));

        return generateMessage(rspHead, responseInfo, jcSession);
    }

    protected void recordResponseInfo(Map insertParam) {
        boolean transactionStart = false;
        try {
            transactionStart = dao.tryStart();
            dao.startBatch();
            dao.insert("JCClientSQL.insertRspData", insertParam);
            List<Map> insertAppList = (List<Map>) insertParam.get("appList");
            if (!Collections.isEmpty(insertAppList)) {
                for (Map app : insertAppList) {
                    dao.insert("JCClientSQL.insertRspSubData", app);
                }
            }
            dao.executeBatch();
            dao.commit(transactionStart);
        }
        finally {
            dao.end(transactionStart);
        }
    }

    protected Map queryRequestInfo(Map insertParam) throws Exception {
        Map requestInfo = dao.selectMap("JCClientSQL.queryRequestInfo", insertParam);
        while (Collections.isEmpty(requestInfo)) {
            Thread.sleep(5000);
            requestInfo = dao.selectMap("JCClientSQL.queryRequestInfo", insertParam);
        }
        dao.update("JCClientSQL.updateRequestInfoState", requestInfo);
        return requestInfo;
    }

}

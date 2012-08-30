package org.n3r.mina.client.process;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.n3r.mina.JCBytesParserFactory;
import org.n3r.mina.bean.JCMessage;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.rsp.AppItem;
import org.n3r.mina.utils.JCTypeUtils;
import org.phw.core.lang.Collections;
import org.phw.ibatis.engine.PDao;

import com.ailk.mall.base.utils.StringUtils;

import static org.phw.config.impl.PhwConfigMgrFactory.*;

public abstract class JCClientProcess {

    private PDao dao;

    protected String ifNo;

    public JCClientProcess(PDao dao, String ifNo) {
        this.dao = dao;
        this.ifNo = ifNo;
    }

    public abstract Map fetchInsertParam(JCMessage message);

    public abstract JCMessage generateMessage(JCMessageHead head, Map param);

    public JCMessage process(JCMessage message, JCBytesParserFactory parser) throws Exception {
        Map insertParam = fetchInsertParam(message);

        String seqId = StringUtils.toString(dao.selectMap("JCClientSQL.getSeq", new HashMap()).get("SEQ"));
        insertParam.put("ID", seqId);
        insertParam.put("SESSIONID", parser.getSessionId());
        insertParam.put("IF_NO", ifNo);

        recordServerMessage(insertParam);

        if (ifNo.equals("IF1")) { return generateMessage(null, null); }

        Map responseInfo = queryClientMessage(insertParam);
        JCMessageHead rspHead = new JCMessageHead();
        rspHead.setSessionId(parser.getSessionId());
        rspHead.setTypeFlag(JCTypeUtils.getMsgType(ifNo));

        return generateMessage(rspHead, responseInfo);
    }

    protected void recordServerMessage(Map insertParam) {
        boolean transactionStart = false;
        try {
            transactionStart = dao.tryStart();
            //            dao.startBatch();
            dao.insert("JCClientSQL.insertRspData", insertParam);
            List<AppItem> insertAppList = (List<AppItem>) insertParam.get("appList");
            if (!Collections.isEmpty(insertAppList)) {
                for (AppItem app : insertAppList) {
                    dao.insert("JCClientSQL.insertRspSubData", app);
                }
            }
            //            dao.executeBatch();
            dao.commit(transactionStart);
        }
        finally {
            dao.end(transactionStart);
        }
    }

    protected Map queryClientMessage(Map insertParam) throws Exception {
        Map requestInfo = dao.selectMap("JCClientSQL.queryRequestInfo", insertParam);
        int idle = getConfigMgr().getInt("MinaIdleTime", 60) / 5;
        for (int i = 0; Collections.isEmpty(requestInfo) && i < idle; i++) {
            Thread.sleep(5000);
            requestInfo = dao.selectMap("JCClientSQL.queryRequestInfo", insertParam);
        }
        if (Collections.isEmpty(requestInfo)) throw new Exception("Cannot query Response Message!");
        dao.update("JCClientSQL.updateRequestInfoState", requestInfo);
        return requestInfo;
    }
}

package org.n3r.mina.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.n3r.core.beanutils.BeanUtils;
import org.n3r.mina.JCSession;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.rsp.AppItem;
import org.n3r.mina.utils.JCMessageUtils;
import org.n3r.mina.utils.JCTypeUtils;
import org.phw.core.lang.Collections;
import org.phw.ibatis.engine.PDao;
import org.phw.ibatis.util.PDaoEngines;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ailk.mall.base.utils.StringUtils;
import com.alibaba.fastjson.JSON;

public class MinaServerHandler extends JCServerHandler {

    private final static String SQL_XML = "org/n3r/mina/server/JCServerSQL.xml";
    private static PDao dao = PDaoEngines.getDao(SQL_XML, "EcsStore");

    private static Logger logger = LoggerFactory.getLogger(MinaServerHandler.class);

    @Override
    public byte[] messageServerProcess(JCSession jcSession, byte[] message) throws Exception {
        Map response = queryServerResponse(jcSession);

        threadSleep(response.get("SLEEP_TIME"));

        String respIfNo = StringUtils.toString(response.get("RESP_IF_NO"));
        String respContent = StringUtils.toString(response.get("RESP_CONTENT"));

        JCMessageHead head = new JCMessageHead();
        head.setSessionId(jcSession.getSessionId());
        head.setTypeFlag(JCTypeUtils.getType(respIfNo));

        Map respMap = JSON.parseObject(respContent, Map.class);
        String jobType = jcSession.getJobType();
        if (respIfNo.equals("IF1") && (jobType.equals("01") || jobType.equals("02") || jobType.equals("03"))) {
            List<Map> apps = (List<Map>) respMap.get("apps");
            List<AppItem> appItems = new ArrayList<AppItem>();
            for (Map app : apps) {
                appItems.add(BeanUtils.populate(AppItem.class, app));
            }
            respMap.put("apps", appItems);
        }

        Class<?> bodyType = respIfNo.equals("IF1") ? JCMessageUtils.parseRspBodyType(respIfNo, jobType) :
                JCMessageUtils.parseReqBodyType(respIfNo);
        Object body = BeanUtils.populate(bodyType, respMap);

        StringBuilder printer = new StringBuilder();
        byte[] result = JCMessageUtils.messageToBytes(head, body, printer);
        logger.info(printer.toString());

        return result;
    }

    private Map queryServerResponse(JCSession jcSession) {
        Map param = Collections.asMap("SESSION_ID", jcSession.getSessionId(), "STAFF_ID", jcSession.getStaffId(),
                "JOB_TYPE", jcSession.getJobType(), "ORDER_NO", jcSession.getOrderNo());
        Map response = dao.selectMap("JCServerSQL.queryRspLs", param);
        if (Collections.isEmpty(response)) {
            response = dao.selectMap("JCServerSQL.queryDefaultRspLs", param);
        }
        return response;
    }
}

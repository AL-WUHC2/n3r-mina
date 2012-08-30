package org.n3r.mina.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.n3r.core.lang.RStr;
import org.n3r.mina.JCBytesParserFactory;
import org.n3r.mina.JCMessageListener;
import org.n3r.mina.bean.JCMessageHead;
import org.n3r.mina.bean.rsp.AppItem;
import org.n3r.mina.utils.JCMessageUtils;
import org.n3r.mina.utils.JCTypeUtils;
import org.phw.core.lang.Collections;
import org.phw.ibatis.engine.PDao;
import org.phw.ibatis.util.PDaoEngines;

import com.alibaba.fastjson.JSON;

public class JCServerSendListener extends JCMessageListener {

    private final static String SQL_XML = "org/n3r/mina/server/JCServerSQL.xml";
    private static PDao dao = PDaoEngines.getDao(SQL_XML, "Eop");

    private IoSession session;

    public JCServerSendListener(IoSession session) {
        this.session = session;
    }

    @Override
    public Object process(Object message, JCBytesParserFactory parser) throws Exception {
        Map param = Collections.asMap("IF_NO", "IF1", "STAFF_ID", parser.getStaffId(),
                        "JOB_TYPE", parser.getJobType(), "ORDER_NO", parser.getOrderNo());
        Map response = dao.selectMap("JCServerSQL.queryRspLs", param);
        if (Collections.isEmpty(response)) {
            response = dao.selectMap("JCServerSQL.queryDefaultRspLs", param);
        }

        Thread.sleep(Integer.valueOf(RStr.toStr(response.get("SLEEP_TIME"))));

        String respIfNo = RStr.toStr(response.get("RESP_IF_NO"));
        String respContent = RStr.toStr(response.get("RESP_CONTENT"));

        JCMessageHead head = new JCMessageHead();
        head.setSessionId(parser.getSessionId());
        head.setTypeFlag(JCTypeUtils.getMsgType(respIfNo));

        Map respMap = JSON.parseObject(respContent, Map.class);
        String jobType = parser.getJobType();
        if (respIfNo.equals("IF1") && RStr.in(jobType, "01", "02", "03")) {
            List<Map> apps = (List<Map>) respMap.get("appList");
            List<AppItem> appItems = new ArrayList<AppItem>();
            for (Map app : apps) {
                AppItem appItem = new AppItem();
                BeanUtils.populate(appItem, app);
                appItems.add(appItem);
            }
            respMap.put("appList", appItems);
        }

        Class<?> bodyType = JCMessageUtils.parseBodyType(respIfNo, jobType, false);
        Object body = bodyType.newInstance();
        BeanUtils.populate(body, respMap);

        StringBuilder printer = new StringBuilder();
        byte[] result = JCMessageUtils.messageToBytes(head, body, printer);
        logger.info("[Server Send Message]: " + printer.toString());

        if (session != null && !session.isClosing()) {
            session.write(IoBuffer.wrap(result));
        }
        return result;
    }
}

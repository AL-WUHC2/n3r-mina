package org.n3r.mina.bean.rsp;

import java.util.List;

import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.annotations.JCList;
import org.n3r.mina.bean.JCMessageBody;

public class IF102RspBody extends JCMessageBody {

    @JCFixLen(length = 2)
    private String result;

    @JCList(2)
    private List<AppItem> appList;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setAppList(List<AppItem> appList) {
        this.appList = appList;
    }

    public List<AppItem> getAppList() {
        return appList;
    }

}

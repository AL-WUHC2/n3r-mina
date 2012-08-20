package org.n3r.mina.bean.rsp;

import java.util.List;

import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.annotations.JCList;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.beanbytes.utils.JCConstantUtils;
import org.n3r.mina.bean.JCMessageBody;

public class IF101RspBody extends JCMessageBody {

    @JCFixLen(length = 2)
    private String result;

    @JCVarLen(charset = JCConstantUtils.CHARSET_UNICODE)
    private String merchantName;

    @JCFixLen(length = 3)
    private String enableSpace;

    @JCFixLen(length = 1)
    private String userFlag;

    @JCList(2)
    private List<AppItem> appList;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setEnableSpace(String enableSpace) {
        this.enableSpace = enableSpace;
    }

    public String getEnableSpace() {
        return enableSpace;
    }

    public void setUserFlag(String userFlag) {
        this.userFlag = userFlag;
    }

    public String getUserFlag() {
        return userFlag;
    }

    public void setAppList(List<AppItem> appList) {
        this.appList = appList;
    }

    public List<AppItem> getAppList() {
        return appList;
    }

}

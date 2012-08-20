package org.n3r.mina.bean.rsp;

import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.beanbytes.utils.JCConstantUtils;
import org.n3r.core.lang.RBaseBean;

public class AppItem extends RBaseBean {

    @JCVarLen(charset = JCConstantUtils.CHARSET_UNICODE)
    private String appName;

    private String appAid;

    @JCFixLen(length = 2)
    private String appSize;

    @JCFixLen(length = 1)
    private String appOperateType;

    @JCVarLen(charset = JCConstantUtils.CHARSET_UNICODE)
    private String provider;

    @JCVarLen(dataType = JCDataType.ASCII)
    private String productId;

    private String feeDesc;

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppAid(String appAid) {
        this.appAid = appAid;
    }

    public String getAppAid() {
        return appAid;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppOperateType(String appOperateType) {
        this.appOperateType = appOperateType;
    }

    public String getAppOperateType() {
        return appOperateType;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductId() {
        return productId;
    }

    public void setFeeDesc(String feeDesc) {
        this.feeDesc = feeDesc;
    }

    public String getFeeDesc() {
        return feeDesc;
    }

}

package org.n3r.mina.bean.req;

import java.util.List;

import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.mina.bean.JCMessageBody;

/**
 * 卡片操作接口(IF1)请求报文.
 */
public class IF1ReqBody extends JCMessageBody {

    /** 操作员ID */
    @JCVarLen(dataType = JCDataType.ASCII)
    private String operatorId;

    /** 发起省份 */
    @JCFixLen(dataType = JCDataType.ASCII, length = 2)
    private String province;

    /** 发起地市 */
    @JCFixLen(dataType = JCDataType.ASCII, length = 3)
    private String city;

    /** 区县 */
    @JCVarLen(dataType = JCDataType.ASCII)
    private String district;

    /** 渠道编码 */
    @JCVarLen(dataType = JCDataType.ASCII)
    private String channelCode;

    /** 渠道类型 */
    @JCVarLen(dataType = JCDataType.ASCII)
    private String channelType;

    /** 身份证 */
    @JCVarLen(dataType = JCDataType.ASCII)
    private String identity;

    /** 不带+86 */
    @JCFixLen(dataType = JCDataType.ASCII, length = 11)
    private String msisdn;

    /** 以89开头 */
    @JCFixLen(length = 10)
    private String iccid;

    /** 以46开头 */
    @JCFixLen(length = 8)
    private String imsi;

    /** 操作方式: 0x01：空中方式; 0x02：POS方式; */
    @JCFixLen(length = 1)
    private String operateType;

    /** CRM产品ID，即ProductCode */
    @JCVarLen(dataType = JCDataType.ASCII)
    private List<String> productIds;

    /** 任务类型 */
    @JCFixLen(length = 1)
    private String jobType;

    /** 请求数据 */
    private String requestData;

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvince() {
        return province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistrict() {
        return district;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getIccid() {
        return iccid;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getImsi() {
        return imsi;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobType() {
        return jobType;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public String getRequestData() {
        return requestData;
    }

}

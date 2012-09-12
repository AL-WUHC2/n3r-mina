package org.n3r.mina.bean.req;

import java.io.Serializable;

public class IF5ReqBean implements Serializable {

    private static final long serialVersionUID = -6016739008811238557L;

    private String idType;

    private String id;

    private String provinceId;

    public void setIdType(String idType) {
        this.idType = idType;
    }

    public String getIdType() {
        return idType;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceId() {
        return provinceId;
    }

}

package org.n3r.mina.bean;

import org.n3r.core.lang.RBaseBean;

public class JCMessage extends RBaseBean {

    private JCMessageHead head;

    private JCMessageBody body;

    public JCMessage() {}

    public JCMessage(JCMessageHead head, JCMessageBody body) {
        this.head = head;
        this.body = body;
    }

    public void setHead(JCMessageHead head) {
        this.head = head;
    }

    public JCMessageHead getHead() {
        return head;
    }

    public void setBody(JCMessageBody body) {
        this.body = body;
    }

    public JCMessageBody getBody() {
        return body;
    }

}

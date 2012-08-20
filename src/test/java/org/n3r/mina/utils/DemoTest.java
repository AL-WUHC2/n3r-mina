package org.n3r.mina.utils;

import org.junit.Test;
import org.n3r.beanbytes.JCDataType;
import org.n3r.beanbytes.annotations.JCFixLen;
import org.n3r.beanbytes.annotations.JCVarLen;
import org.n3r.beanbytes.impl.BeanToBytes;

import static org.junit.Assert.*;

import static org.n3r.beanbytes.utils.BeanBytesUtils.*;
import static org.n3r.core.lang.RByte.*;

public class DemoTest {

    @Test
    public void testDemoBean() {
        DemoBean demoBean = new DemoBean();
        demoBean.setHexStr("0123");
        demoBean.setOctetStr("test");
        demoBean.setAsciiStr("ASCII");
        StringBuilder printer = new StringBuilder();

        BeanToBytes<DemoBean> beanToBytes = new BeanToBytes<DemoBean>();
        byte[] bytes = beanToBytes.toBytes(demoBean, printer);
        byte[] expected = prependLen(toBytes((short) (1 * 16 * 16 + 2 * 16 + 3)), 1);
        expected = add(expected, toBytes("test", "UTF-8"), repeat((byte) 0, 6));
        expected = add(expected, prependLen(toBytes("ASCII", "UTF-16LE"), 1));
        assertArrayEquals(expected, bytes);
        assertEquals("{hexStr:0123, octetStr:test, asciiStr:ASCII}", printer.toString());
    }

    public static class DemoBean {
        @JCVarLen(lenBytes = 1, dataType = JCDataType.HEX)
        private String hexStr;
        @JCFixLen(length = 10, dataType = JCDataType.Octet, pad = "00")
        private String octetStr;
        @JCVarLen(lenBytes = 1, dataType = JCDataType.ASCII, charset = "UTF-16LE")
        private String asciiStr;

        public void setHexStr(String hexStr) {
            this.hexStr = hexStr;
        }

        public String getHexStr() {
            return hexStr;
        }

        public void setOctetStr(String octetStr) {
            this.octetStr = octetStr;
        }

        public String getOctetStr() {
            return octetStr;
        }

        public void setAsciiStr(String asciiStr) {
            this.asciiStr = asciiStr;
        }

        public String getAsciiStr() {
            return asciiStr;
        }
    }

}

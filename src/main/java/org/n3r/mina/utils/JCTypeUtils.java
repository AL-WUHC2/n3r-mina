package org.n3r.mina.utils;

import org.apache.commons.lang3.StringUtils;
import org.n3r.core.lang.RHex;

public class JCTypeUtils {

    public static String getMsgTypeIFString(String type) {
        return "IF" + (RHex.decode(type)[0] + 1);
    }

    public static String getMsgType(String type) {
        byte b = Byte.valueOf(StringUtils.substring(type, 2));
        return RHex.encode(new byte[] { (byte) (b - 1) });
    }

}

package org.n3r.mina.utils;

import org.n3r.core.lang.RHex;
import org.n3r.core.lang3.StringUtils;

public class JCTypeUtils {

    public static String getTypeIFString(String type) {
        return "IF" + (RHex.decode(type)[0] + 1);
    }

    public static String getType(String type) {
        byte b = Byte.valueOf(StringUtils.substring(type, 2));
        return RHex.encode(new byte[] { (byte) (b - 1) });
    }
}

package org.n3r.mina;

import org.junit.Test;

import static org.junit.Assert.*;

import static org.n3r.core.lang.RByte.*;
import static org.n3r.core.lang.RHex.*;

public class JCBytesParserFactoryTest {

    @Test
    public void test1() {
        JCBytesParserFactory factory = new JCBytesParserFactory();
        factory.pushRawBytes(add(toBytes((short) 10), decode("ABCDEF")));

        assertNull(factory.popMsgBytes());
    }
}

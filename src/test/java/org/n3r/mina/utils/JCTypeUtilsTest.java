package org.n3r.mina.utils;

import org.junit.Test;

import static org.junit.Assert.*;

import static org.n3r.mina.utils.JCTypeUtils.*;

public class JCTypeUtilsTest {

    @Test
    public void test1() {
        assertNotNull(new JCTypeUtils());

        assertEquals("IF1", getMsgTypeIFString("00"));
        assertEquals("00", getMsgType("IF1"));
    }
}

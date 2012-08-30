package org.n3r.mina;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class JCMessageListener implements MessageListener {

    protected static Logger logger = LoggerFactory.getLogger(JCMessageListener.class);

    @Override
    public Object process(Object message, BytesParserFactory parser) throws Exception {
        return process(message, (JCBytesParserFactory) parser);
    }

    public abstract Object process(Object message, JCBytesParserFactory parser) throws Exception;

}

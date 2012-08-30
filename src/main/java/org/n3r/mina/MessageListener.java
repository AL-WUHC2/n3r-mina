package org.n3r.mina;

public interface MessageListener {

    Object process(Object message, BytesParserFactory parser) throws Exception;

}

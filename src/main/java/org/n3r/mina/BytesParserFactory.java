package org.n3r.mina;

import java.util.ArrayList;
import java.util.List;

public abstract class BytesParserFactory {

    protected byte[] buffer = new byte[0];

    protected List<MessageListener> listeners = new ArrayList<MessageListener>();

    public abstract void pushRawBytes(byte[] bytes);

    public abstract byte[] popMsgBytes();

    public void parseBytes(byte[] bytes) throws Exception {
        pushRawBytes(bytes);

        for (Object result = popMsgBytes(); result != null; result = popMsgBytes()) {
            for (MessageListener listener : listeners) {
                result = listener.process(result, this);
            }
        }
    }

    public BytesParserFactory addListener(MessageListener listener) {
        listeners.add(listener);
        return this;
    }

    public BytesParserFactory removeListener(int index) {
        listeners.remove(index);
        return this;
    }

    public BytesParserFactory cleanListeners() {
        listeners = new ArrayList<MessageListener>();
        return this;
    }

    public void cleanBuffer() {
        buffer = new byte[0];
    }

}

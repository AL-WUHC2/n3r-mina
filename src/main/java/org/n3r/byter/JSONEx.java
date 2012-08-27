package org.n3r.byter;

import org.n3r.byter.visitor.DefaultJSONOutputVisitor;
import org.n3r.byter.visitor.DefaultJavaBeanHandler;
import org.n3r.byter.visitor.JSONOutputVisitor;
import org.n3r.byter.visitor.JSONPrettyFormatOutputVisitor;
import org.n3r.byter.visitor.JavaBeanHandler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeWriter;

public class JSONEx {
    public static final String toJSONString(Object object, boolean prettyFormat) {
        if (!prettyFormat) { return JSON.toJSONString(object); }

        return toJSONString(object, DefaultJavaBeanHandler.getInstance(), prettyFormat);
    }

    public static final String toJSONString(Object object, JavaBeanHandler javaBeanHandler) {
        return toJSONString(object, javaBeanHandler, false);
    }

    public static final String toJSONString(Object object, JavaBeanHandler javaBeanHandler, boolean prettyFormat) {
        SerializeWriter out = new SerializeWriter();

        try {
            JSONOutputVisitor visitor;

            if (prettyFormat) {
                visitor = new JSONPrettyFormatOutputVisitor(out);
            }
            else {
                visitor = new DefaultJSONOutputVisitor(out);
            }

            visitor.setJavaBeanHandler(javaBeanHandler);

            visitor.accept(object);

            return out.toString();
        }
        finally {
            out.close();
        }
    }
}

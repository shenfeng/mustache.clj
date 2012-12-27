package me.shenfeng.mustache;

import java.util.List;
import java.util.Map;

import clojure.lang.Keyword;

public class Context {
    private final Object data;
    private final Context parent;

    private static Keyword ME = Keyword.intern(".");

    public Context(Object data, Context parent) {
        this.data = data;
        this.parent = parent;
    }

    public Context(Object data) {
        this(data, null);
    }

    @SuppressWarnings("rawtypes")
    public static boolean isArray(Object v) {
        if (v instanceof List) {
            return ((List) v).size() > 0;
        }
        return false;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isFalse(Object v) {
        if (v == null) {
            return true;
        } else if (Boolean.FALSE.equals(v)) {
            return true;
        } else if ((v instanceof List) && ((List) v).isEmpty()) {
            return true;
        } else if ((v instanceof String) && ((String) v).isEmpty()) {
            return true;
        }
        return false;
    }

    public Object lookup(Object key) {
        if (key.equals(ME)) {
            return data;
        }

        Context context = this;
        while (context != null) {
            Object d = context.data;
            if (d instanceof Map) {
                @SuppressWarnings("rawtypes")
                Map m = (Map)d;
                if(m.containsKey(key)) {
                   return m.get(key);
                }
            }
            context = context.parent;
        }
        return null;
    }
}

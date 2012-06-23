package me.shenfeng.mustache;

import static me.shenfeng.mustache.Context.isArray;
import static me.shenfeng.mustache.Context.isFalse;

import java.util.List;

import clojure.lang.Keyword;

public class Token {
    public static final char TEXT = 't';
    public static final char NAME = 'n';
    public static final char NO_ESCAPE = '{';
    public static final char SECTION = '#';
    public static final char INVERTED = '^';
    public static final char PARTIAL = '>';
    final char type;
    final Object value;
    List<Token> tokens;

    public Token(char type, String value) {
        this.type = type;
        if (type != TEXT) {
            value = value.trim();
            this.value = Keyword.intern(value);
        } else {
            this.value = value;
        }
    }

    public static String renderTokens(List<Token> tokens, Context c) {
        StringBuilder sb = new StringBuilder();
        for (Token t : tokens) {
            sb.append(t.render(c));
        }
        return sb.toString();
    }

    public String render(Context c) {
        switch (type) {
        case TEXT:
            return value.toString();
        case NAME:
        case NO_ESCAPE:
            Object r = c.lookup(value);
            if (r != null) {
                return r.toString();
            }
            break;
        case SECTION:
            if (tokens != null && tokens.size() > 0) {
                Object n = c.lookup(value);
                if (isArray(n)) {
                    StringBuilder sb = new StringBuilder();
                    @SuppressWarnings("rawtypes")
                    List list = (List) n;
                    for (Object o : list) {
                        Context nested = new Context(o, c);
                        for (Token t : tokens) {
                            sb.append(t.render(nested));
                        }
                    }
                    return sb.toString();
                } else if (!isFalse(n)) {
                    return renderTokens(tokens, new Context(n, c));
                }
            }
            break;
        case INVERTED:
            if (tokens != null && tokens.size() > 0) {
                if (isFalse(c.lookup(value))) {
                    return renderTokens(tokens, c);
                }
            }
            break;
        case PARTIAL:
        }
        return "";
    }

    public String toString() {
        if (tokens != null && !tokens.isEmpty()) {
            return type + " " + value + "\t" + tokens;
        } else {
            return type + " " + value;
        }
    }
}

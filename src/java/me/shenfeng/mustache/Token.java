package me.shenfeng.mustache;

import static me.shenfeng.mustache.Context.isArray;
import static me.shenfeng.mustache.Context.isFalse;

import java.util.List;
import java.util.Map;

import clojure.lang.Keyword;

public class Token {
    public static final char TEXT = 't';
    public static final char NAME = 'n';
    public static final char NO_ESCAPE1 = '{';
    public static final char NO_ESCAPE2 = '&';
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
            // remove space between tags, this is not in Mustache spec
            // It works for html
            // value = value.replaceAll("(\\w)>\\s+?<(\\w|/)", "$1><$2");
            this.value = value;
        }
    }

    public static String renderTokens(List<Token> tokens, Context c,
            Map<Keyword, String> partials) throws ParserException {
        StringBuilder sb = new StringBuilder();
        for (Token t : tokens) {
            sb.append(t.render(c, partials));
        }
        return sb.toString();
    }

    public static String escapeHtml(String html) {
        StringBuilder sb = new StringBuilder(html.length() + 10);
        for (int i = 0; i < html.length(); ++i) {
            char c = html.charAt(i);
            switch (c) {
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            case '\'':
                sb.append("&#39;");
                break;
            default:
                sb.append(c);
                break;
            }
        }
        return sb.toString();
    }

    public String render(Context c, Map<Keyword, String> partials)
            throws ParserException {
        switch (type) {
        case TEXT:
            return value.toString();
        case NAME:
            Object v = c.lookup(value);
            if (v != null) {
                return escapeHtml(v.toString());
            }
            break;
        case NO_ESCAPE1:
        case NO_ESCAPE2:
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
                            sb.append(t.render(nested, partials));
                        }
                    }
                    return sb.toString();
                } else if (!isFalse(n)) {
                    return renderTokens(tokens, new Context(n, c), partials);
                }
            }
            break;
        case INVERTED:
            if (tokens != null && tokens.size() > 0) {
                if (isFalse(c.lookup(value))) {
                    return renderTokens(tokens, c, partials);
                }
            }
            break;
        case PARTIAL:
            if (partials != null) {
                String template = partials.get(value);
                if (template != null) {
                    return Mustache.preprocess(template).render(c, partials);
                }
            }
        }
        return ""; // empty
    }

    public String toString() {
        if (tokens != null && !tokens.isEmpty()) {
            return type + " " + value + "\t" + tokens;
        } else {
            return type + " " + value;
        }
    }
}

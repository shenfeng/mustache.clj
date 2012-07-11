package me.shenfeng.mustache;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import clojure.lang.Keyword;

public class Mustache {

    public static final String BEGIN = "{{";
    public static final String END = "}}";

    List<Token> tokens;

    //
    public static final HashMap<String, Mustache> CACHE = new HashMap<String, Mustache>();

    private List<Token> nestedToken(List<Token> input) throws ParserException {
        List<Token> output = new ArrayList<Token>();
        Deque<Token> sections = new LinkedList<Token>(); // stack
        Token section;
        List<Token> collector = output;

        for (Token token : input) {
            switch (token.type) {
            case Token.INVERTED:
            case Token.SECTION:
                token.tokens = new ArrayList<Token>();
                sections.add(token);
                collector.add(token);
                collector = token.tokens;
                break;
            case '/':
                if (sections.isEmpty()) {
                    throw new ParserException("Unopened section: "
                            + token.value);
                }
                section = sections.removeLast();
                if (!section.value.equals(token.value)) {
                    throw new ParserException("Unclosed section: "
                            + section.value + ":" + token.value);
                }

                if (sections.size() > 0) {
                    collector = sections.peekLast().tokens;
                } else {
                    collector = output;
                }
                break;
            default:
                collector.add(token);
                break;
            }
        }
        if (sections.size() > 0) {
            throw new ParserException("Unclosed section: "
                    + sections.peek().value);
        }
        return output;
    }

    public static Mustache preprocess(String template) throws ParserException {
        Mustache m = CACHE.get(template);
        if (m == null) {
            m = new Mustache(template);
            CACHE.put(template, m);
        }
        return m;
    }

    private Mustache(String template) throws ParserException {
        List<Token> tokens = new LinkedList<Token>();
        Scanner scanner = new Scanner(template);
        while (!scanner.eos()) {
            String value = scanner.scanUtil(BEGIN);
            if (value != null)
                tokens.add(new Token(Token.TEXT, value));
            char type = scanner.nextType();
            scanner.skipeWhiteSpace();
            switch (type) {
            case '{': // not escape
                value = scanner.scanUtil("}}}");
                tokens.add(new Token(type, value));
                break;
            default:
                value = scanner.scanUtil(END);
                tokens.add(new Token(type, value));
            }

        }
        this.tokens = nestedToken(tokens);
    }

    public String render(Context ctx) {
        try {
            return Token.renderTokens(tokens, ctx, null);
        } catch (ParserException e) {
            return ""; // can not happen
        }
    }

    public String render(Context ctx, Map<Keyword, String> partials)
            throws ParserException {
        return Token.renderTokens(tokens, ctx, partials);
    }
}

package me.shenfeng.mustache;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class MustacheTest {

    String template = "<p>this is a test</p> <ul> {{#arr}} <li> <p>{{{ name }}}</p> <div> {{#arr}} <p>{{ name }}</p> {{/arr}} </div> {{/arr}} </li> </ul>";

    public static void printTokens(List<Token> tokens, String indent) {
        for (Token token : tokens) {
            System.out.printf("%s%c %s\n", indent, token.type, token.value);
            if (token.tokens != null) {
                printTokens(token.tokens, indent + "  ");
            }
        }
    }

    public String getContent(String name) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(name));

        String line;
        String file = "";
        while ((line = br.readLine()) != null) {
            file += (line + '\n');
        }
        br.close();
        return file;
    }

    @Test
    public void testParser2() throws IOException, ParserException {
        String file = getContent("test/test.tpl");
        Mustache m = Mustache.preprocess(file);
        printTokens(m.tokens, "");

    }

    @Test
    public void testParser3() throws IOException, ParserException {
        String file = getContent("test/landing.tpl");
        Mustache m = Mustache.preprocess(file);
        printTokens(m.tokens, "");
    }

    @Test
    public void testParse() throws ParserException {
        Mustache m = Mustache.preprocess(template);
        printTokens(m.tokens, "");
        List arr = new ArrayList();
        for (int i = 0; i < 10; i++) {
            Map cell = new HashMap();
            cell.put("name", "outer" + i);
            arr.add(cell);
        }
        Map data = new HashMap();
        data.put("arr", arr);
        // cell.put("arr", value)
        Context c = new Context(data, null);
        for (int i = 0; i < 1000000; ++i) {
            String html = m.render(c);
        }
        // System.out.println(html);
    }
}

package test;

import java.util.ArrayList;
import java.util.List;

public class SplitTest {

    public static List<String> splitText(String text) {
        List<String> result = new ArrayList<>();
        String part = "";
        boolean escaping = false;
        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            if (current == '\\') {
                if (escaping) {
                    part += current;
                    escaping = false;
                } else {
                    escaping = true;
                }
            } else {
                if (escaping) {
                    part += current;
                    escaping = false;
                } else {
                    if (current == '|') {
                        result.add(part);
                        part = "";
                    } else {
                        part += current;
                    }
                }
            }
        }
        result.add(part);
        return result;
    }

    public static void main(String[] args) {
        System.out.println(splitText(escape("abcd")));
        System.out.println(splitText(escape("ab|cd")));
        System.out.println(splitText(escape("ab\\cd")));
        System.out.println(splitText(escape("ab\\|cd")));
        System.out.println(splitText(escape("ab\\|cd") + "|" + escape("abc")));
        System.out.println(splitText(escape("ab\\|cd") + "||" + escape("abc")));
        System.out.println(splitText(escape("ab\\|cd") + "|"));

    }

    public static String escape(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char current = text.charAt(i);
            switch (current) {
            case '\\':
                sb.append("\\\\");
                break;
            case '|':
                sb.append("\\|");
                break;
            default:
                sb.append(current);
            }
        }
        return sb.toString();
    }
}

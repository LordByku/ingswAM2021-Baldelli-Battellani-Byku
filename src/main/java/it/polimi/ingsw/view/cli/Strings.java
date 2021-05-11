package it.polimi.ingsw.view.cli;

import java.text.BreakIterator;

public class Strings {
    public static int getGraphemesCount(String str) {
        BreakIterator breakIterator = BreakIterator.getCharacterInstance();
        breakIterator.setText(str);
        int count = 0;
        while (breakIterator.next() != BreakIterator.DONE) {
            if(str.charAt(breakIterator.current() - 1) == '\u001B') {
                while(str.charAt(breakIterator.current() - 1) != 'm') {
                    breakIterator.next();
                }
            } else {
                count++;
            }
        }
        return count;
    }

    public static String getFaithPointsSymbol() {
        return TextColour.RED.escape() + "\u271d" + TextColour.RESET;
    }

    public static String[] splitLine(String line) {
        if(line.equals("")) {
            return new String[0];
        }
        return line.split("\\s+", 0);
    }

    public static void buildCenteredRow(StringBuilder result,String start, int startLen, String end, int endLen, String str, int strLen, int width) {
        result.append("|").append(start);

        int len = width - strLen - startLen - endLen;
        for(int i = 0; i < (len + 1) / 2; ++i) {
            result.append(" ");
        }
        result.append(str);
        for(int i =startLen + (len+1) / 2 + strLen; i < width - endLen; ++i) {
            result.append(" ");
        }
        result.append(end).append("|\n");
    }

    public static void newEmptyLine(StringBuilder result, int width){
        result.append("|");
        for(int i = 0; i < width; ++i) {
            result.append(" ");
        }
        result.append("|\n");
    }

    public static String format(int n, int spaces) {
        int base = 10;
        StringBuilder result = new StringBuilder("");
        for(int i = 1; i < spaces; ++i) {
            if(n < base) {
                result.append(" ");
                base *= 10;
            } else {
                break;
            }
        }

        result.append(n);

        return result.toString();
    }
}

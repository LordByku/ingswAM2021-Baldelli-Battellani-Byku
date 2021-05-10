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

    public static void buildCenteredRow(StringBuilder result,String start, String end, String str, int strLen, int width) {
        result.append("|").append(start);

        int len = width-strLen-start.length()-end.length();

        for(int i = 0; i < len / 2; ++i) {
            result.append(" ");
        }
        result.append(str);
        for(int i = len / 2 + strLen; i < width; ++i) {
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
}

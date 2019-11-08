package nl.imine.api.util;

public class ColorUtil {

    private static final String[][] COLOR_CODES = new String[][]{{"&0", "\u00A70"}, {"&1", "\u00A71"},
            {"&2", "\u00A72"}, {"&3", "\u00A73"}, {"&4", "\u00A74"}, {"&5", "\u00A75"}, {"&6", "\u00A76"},
            {"&7", "\u00A77"}, {"&8", "\u00A78"}, {"&9", "\u00A79"}, {"&a", "\u00A7a"}, {"&b", "\u00A7b"},
            {"&c", "\u00A7c"}, {"&d", "\u00A7d"}, {"&e", "\u00A7e"}, {"&f", "\u00A7f"}, {"&k", "\u00A7k"},
            {"&l", "\u00A7l"}, {"&m", "\u00A7m"}, {"&n", "\u00A7n"}, {"&o", "\u00A7o"}, {"&r", "\u00A7r"}};

    public static String replaceColors(String toReplace, Object... args) {
        return replaceColors(String.format(toReplace, args));
    }

    public static String replaceColors(String toReplace) {
        if (toReplace == null) {
            return "";
        }
        for (String[] strs : COLOR_CODES) {
            toReplace = toReplace.replaceAll(strs[0], strs[1]);
        }
        return toReplace;
    }
}

package net.minebr.armazem.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.val;
import net.minebr.armazem.ArmazemMain;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumberFormat {

    private static final Pattern PATTERN = Pattern.compile("^(\\d+\\.?\\d*)(\\D+)");

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,###.#");

    public static String decimalFormat(double value) {

        return DECIMAL_FORMAT.format(value);
    }


    public static String numberFormat(double value) {
        String[] suffix = ArmazemMain.getPlugin().getConfig().getStringList("currency-format").toArray(new String[0]);
        int index = 0;
        while ((value / 1000) >= 1) {
            value = value / 1000;
            index++;
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return String.format("%s%s", decimalFormat.format(value), suffix[index]);
    }

    public static double parse(String string) {
        try {

            val value = Double.parseDouble(string);
            return isInvalid(value) ? 0 : value;

        } catch (Exception ignored) {
        }

        if (ArmazemMain.getPlugin().configuration.getString("format-type").equalsIgnoreCase("DECIMAL")) return 0;

        Matcher matcher = PATTERN.matcher(string);
        if (!matcher.find()) return 0;

        double amount = Double.parseDouble(matcher.group(1));
        String suffix = matcher.group(2);
        String fixedSuffix = suffix.equalsIgnoreCase("k") ? suffix.toLowerCase() : suffix.toUpperCase();

        int index = ArmazemMain.getPlugin().getConfig().getStringList("currency-format").indexOf(fixedSuffix);

        val value = amount * Math.pow(1000, index);
        return isInvalid(value) ? 0 : value;
    }

    public static boolean isInvalid(double value) {
        return value < 0 || Double.isNaN(value) || Double.isInfinite(value);
    }

}

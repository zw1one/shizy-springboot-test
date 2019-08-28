package com.shizy.utils.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class FormatUtil {
    /**
     * 格式化
     *
     * @param jsonStr
     * @return
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr))
            return "";
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        boolean isInQuotationMarks = false;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case '"':
                    if (last != '\\') {
                        isInQuotationMarks = !isInQuotationMarks;
                    }
                    sb.append(current);
                    break;
                case '{':
                case '[':
                    sb.append(current);
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent++;
                        addIndentBlank(sb, indent);
                    }
                    break;
                case '}':
                case ']':
                    if (!isInQuotationMarks) {
                        sb.append('\n');
                        indent--;
                        addIndentBlank(sb, indent);
                    }
                    sb.append(current);
                    break;
                case ',':
                    sb.append(current);
                    if (last != '\\' && !isInQuotationMarks) {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }

        return sb.toString();
    }

    /**
     * 数值格式化小数点后两位
     *
     * @param value 数值
     * @return double
     */
    public static double formatDouble(double value) {
        value = new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return value;
    }

    /**
     * 小数点后两位为0，则double转为整数格式，不显示小数,否则显示两位小数
     *
     * @param value 数值
     * @return String
     */
    public static String formatDoubleValue(double value) {
        String leadDes;
        if (Math.round(value) - value == 0) {
            leadDes = String.valueOf((long) value);
        } else {
            DecimalFormat df = new DecimalFormat("0.00");
            leadDes = String.valueOf(df.format(value));
        }
        return leadDes;
    }

    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
}
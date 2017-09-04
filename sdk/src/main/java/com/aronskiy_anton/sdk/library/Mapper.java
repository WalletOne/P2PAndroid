package com.aronskiy_anton.sdk.library;

import com.aronskiy_anton.sdk.constants.CurrencyId;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by aaronskiy on 28.08.2017.
 */

public class Mapper {

    public interface Mappable {
        <T> T init(JSONObject object);
    }

    public static String map(Object object, String def) {
        return (object instanceof String ? (String) object : def);
    }

    public static Integer map(Object object, Integer def) {
        return (object instanceof Number ? ((Number) object).intValue() : def);
    }

    public static Float map(Object object, Float def) {
        return (object instanceof Number ? ((Number) object).floatValue() : def);
    }

    public static Double map(Object object, Double def) {
        return (object instanceof Number ? ((Number) object).doubleValue() : def);
    }

    public static BigDecimal map(Object object, BigDecimal def) {
        if (object instanceof Number) {
            return BigDecimal.valueOf(((Number) object).doubleValue());
        } else {
            return def;
        }
    }

    public static CurrencyId map(Object object, CurrencyId def) {
        return (object instanceof CurrencyId ? (CurrencyId) object : def);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Mappable> T map(Object object, T def) {
        if (object instanceof JSONObject) {
            return ((T) object).init((JSONObject) object);
        } else {
            return def;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Mappable> List<T> map(Object object, List<T> def) {
        if (object instanceof JSONArray) {
            return ((T) object).init((JSONObject) object);
        } else {
            return def;
        }
    }

    public static Date map(Object object) {
        if (object instanceof String) {
            final String value = (String) object;
            List<String> formats = Arrays.asList(
                    "yyyy-MM-dd'T'HH:mm:ss",
                    "yyyy-MM-dd'T'HH:mm:ss'.'SSZ",
                    "yyyy-MM-dd'T'HH:mm:ss'.'SS",
                    "yyyy-MM-dd'T'HH:mm:ssZZZZZ",
                    "yyyy-MM-dd'T'HH:mm:ss'.'SSSZ");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            for (String format : formats) {
                dateFormat.applyPattern(format);
                //dateFormat.format(format);

                try {
                    return dateFormat.parse(value);
                } catch (ParseException e) {

                }
            }
        }
        return null;
    }
}

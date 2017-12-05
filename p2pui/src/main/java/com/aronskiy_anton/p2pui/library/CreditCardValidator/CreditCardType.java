package com.aronskiy_anton.p2pui.library.CreditCardValidator;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by anton on 08.09.2017.
 */

public enum CreditCardType {

    AMEX("Amex", "^3[47][0-9]{5,}$"),
    VISA("Visa", "^4\\d{0,}$"),
    MASTERCARD("MasterCard", "^5[1-5]\\d{0,14}$"),
    MAESTRO("Maestro", "^(?:5[0678]\\d\\d|6304|6390|67\\d\\d)\\d{8,15}$"),
    DINERS_CLUB("Diners Club", "^3(?:0[0-5]|[68][0-9])[0-9]{4,}$"),
    JCB("JCB", "^(?:2131|1800|35[0-9]{3})[0-9]{3,}$"),
    DISCOVER("Discover", "^6(?:011|5[0-9]{2})[0-9]{3,}$"),
    UNIONPAY("UnionPay", "^62[0-5]\\d{13,16}$"),
    MIR("Mir", "^22[0-9]{1,14}$"),
    UNDEFINED("", "");

    private String title;
    private String regEx;

    private static Map<String, String> types = new HashMap<>();

    static {
        for(CreditCardType type : values()) {
            types.put(type.title, type.regEx);
        }
    }

    CreditCardType(String title, String regEx) {
        this.title = title;
        this.regEx = regEx;
    }

    public String getTitle() {
        return title;
    }

    public String getRegEx() {
        return regEx;
    }

    /**
     Get paymentTool type from string

     @param from paymentTool number string

     @return type of paymentTool
     */
    public static CreditCardType type(String from){
        Pattern pattern;
        String numbersString;
        for(CreditCardType type : values()){
            pattern = Pattern.compile(type.regEx);
            numbersString = onlyNumbers(from);
            if (pattern.matcher(numbersString).matches()){
                return type;
            }
        }
        return UNDEFINED;
    }

    static String onlyNumbers(String src) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);
            if (Character.isDigit(c)) {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
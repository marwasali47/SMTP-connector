package com.orange.commons.utilities;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mohamed_waleed on 28/11/17.
 */
public class EmailUtility {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+", Pattern.CASE_INSENSITIVE);

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}

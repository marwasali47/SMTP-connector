package com.orange.commons.enums;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by Mohamed Gaber on Apr, 2019
 */
public enum MeetingResponseAction {
    ACCEPT, DECLINE, TENTATIVE;

    public static String getValuesAsString() {
        return Arrays.stream(values()).
                map(item -> item.name())
                .collect(Collectors.joining(", "));
    }
}

package com.mzherdev.accounts.model;

import java.util.Arrays;

public enum Currency {
    USD, RUB, EUR;

    public static boolean isValidCode(String code) {
        return Arrays.stream(values()).anyMatch(c -> c.name().equals(code));
    }
}

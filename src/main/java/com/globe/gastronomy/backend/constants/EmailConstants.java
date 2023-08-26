package com.globe.gastronomy.backend.constants;


public enum EmailConstants {
    FIRSTNAME("${FIRSTNAME}"),
    LASTNAME("${LASTNAME}"),
    PASSWORD_REFRESH_LINK("${PASSWORD_REFRESH_LINK}"),
    FORGOT_PASSWORD_TEMPLATE("forgotPassword"),
    FORGOT_PASSWORD_SUBJECT("FORGOT PASSWORD LINK - Gastronomy Globe");


    private final String str;


    EmailConstants(String str) {
        this.str = str;
    }

    public String getStr() {
        return str;
    }
}

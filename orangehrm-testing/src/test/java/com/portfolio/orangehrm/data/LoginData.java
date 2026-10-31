package com.portfolio.orangehrm.data;

import org.testng.annotations.DataProvider;

public final class LoginData {
    private LoginData() { }

    @DataProvider(name = "invalidLogins")
    public static Object[][] invalidLogins() {
        return new Object[][] {
            {"invalid_user", "wrong_password"},
            {"Admin", "not_the_password"}
        };
    }
}

package com.portfolio.hybrid.api;

import com.portfolio.hybrid.config.FrameworkConfig;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.*;

/** Explicit deployment contracts; never infer or relax assertions from an unexpected response. */
public enum ApiContract {
    PROXY, REWRITES;

    public static ApiContract current() {
        return valueOf(FrameworkConfig.value("api.contract").toUpperCase(java.util.Locale.ROOT));
    }
    public int unauthorizedBookingStatus() { return this == REWRITES ? 403 : 401; }
    public void verifyRoomMutation(Response response) {
        if (this == REWRITES) response.then().statusCode(202).spec(ApiClient.JSON).body("roomid", greaterThan(0));
        else response.then().statusCode(200).body("success", is(true));
    }
    public void verifyDeletion(Response response) {
        if (this == REWRITES) response.then().statusCode(202).body(emptyString());
        else response.then().statusCode(200).body("success", is(true));
    }
    public void verifyBookingUpdate(Response response, int id) {
        response.then().statusCode(200);
        if (this == REWRITES) response.then().body("bookingid", equalTo(id));
        else response.then().body("success", is(true));
    }
}

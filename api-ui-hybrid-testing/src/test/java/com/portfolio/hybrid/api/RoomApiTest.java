package com.portfolio.hybrid.api;

import com.portfolio.hybrid.base.OwnedResources;
import com.portfolio.hybrid.models.Room;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class RoomApiTest {
    @Test(groups = {"api", "smoke", "regression"}, description = "API-01: public room list satisfies schema and individual room matches")
    public void retrieveRooms() {
        var api = new RoomApiClient();
        api.get("/room").then().statusCode(200).spec(ApiClient.JSON).body(matchesJsonSchemaInClasspath("testdata/rooms.schema.json"));
        var rooms = api.rooms();
        assertFalse(rooms.isEmpty(), "Environment must contain a bookable room");
        assertEquals(api.room(rooms.get(0).roomid()), rooms.get(0));
    }
    @Test(groups = {"api", "negative", "regression"}, description = "API-02: proxy rejects room creation without authentication")
    public void unauthorizedCreate() {
        new RoomApiClient().create(com.portfolio.hybrid.data.TestData.room()).then().statusCode(401);
    }
    @Test(groups = {"api", "negative", "regression"}, description = "API-03: unknown room exposes current proxy empty-body error mapping")
    public void unknownRoom() {
        // Upstream returns an empty 404; Next.js attempts JSON parsing and maps it to 500.
        new RoomApiClient().get("/room/2147483647").then().statusCode(500);
    }
    @Test(groups = {"api", "regression"}, description = "API-04: create, read and update an isolated room; delete in finally")
    public void roomLifecycle() {
        try (var owned = new OwnedResources()) {
            var original = owned.rooms.room(owned.room.roomid());
            assertEquals(original.description(), owned.room.description());
            var updated = new Room(original.roomid(), original.roomName(), original.type(), original.accessible(),
                original.image(), original.description() + " updated", original.features(), 175);
            ApiContract.current().verifyRoomMutation(owned.rooms.update(original.roomid(), updated));
            assertEquals(owned.rooms.room(original.roomid()), updated);
        }
    }
}

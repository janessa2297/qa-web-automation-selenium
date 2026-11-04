package com.portfolio.hybrid.api;

import com.portfolio.hybrid.models.Room;
import io.restassured.response.Response;
import java.util.List;

public final class RoomApiClient extends ApiClient {
    public RoomApiClient() { super(); }
    public RoomApiClient(String token) { super(token); }
    public List<Room> rooms() {
        return get("/room").then().statusCode(200).spec(JSON).extract().jsonPath().getList("rooms", Room.class);
    }
    public Room room(int id) { return get("/room/" + id).then().statusCode(200).spec(JSON).extract().as(Room.class); }
    public Response create(Room room) { return post("/room", room); }
    public Response update(int id, Room room) { return put("/room/" + id, room); }
    public Response remove(int id) { return delete("/room/" + id); }
}

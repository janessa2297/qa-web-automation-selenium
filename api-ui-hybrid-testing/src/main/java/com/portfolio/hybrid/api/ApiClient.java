package com.portfolio.hybrid.api;

import com.portfolio.hybrid.config.FrameworkConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.slf4j.LoggerFactory;
import static io.restassured.RestAssured.given;

public class ApiClient {
    public static final ResponseSpecification JSON = new ResponseSpecBuilder().expectContentType(ContentType.JSON).build();
    private final String token;
    public ApiClient() { this(null); }
    public ApiClient(String token) { this.token = token; }
    protected RequestSpecification request() {
        var spec = new RequestSpecBuilder().setBaseUri(FrameworkConfig.apiUrl())
            .setContentType(ContentType.JSON).setAccept(ContentType.JSON)
            .setConfig(RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig()
                .setParam("http.connection.timeout", 15000).setParam("http.socket.timeout", 20000)))
            .addFilter((req, res, ctx) -> {
                long start = System.nanoTime();
                Response response = ctx.next(req, res);
                // Deliberate allowlist: never log cookies, headers, query values or personal data bodies.
                LoggerFactory.getLogger(ApiClient.class).info("{} {} -> {} ({} ms); headers/body=[REDACTED]",
                    req.getMethod(), java.net.URI.create(req.getURI()).getPath(), response.statusCode(),
                    (System.nanoTime() - start) / 1_000_000);
                return response;
            }).build();
        var request = given().spec(spec);
        if (token != null) request.cookie("token", token);
        return request;
    }
    public Response get(String path) { return request().get(path); }
    public Response post(String path, Object body) { return request().body(body).post(path); }
    public Response put(String path, Object body) { return request().body(body).put(path); }
    public Response delete(String path) { return request().delete(path); }
    public static String login() {
        return new ApiClient().post("/auth/login", java.util.Map.of("username", FrameworkConfig.username(),
            "password", FrameworkConfig.password())).then().statusCode(200).spec(JSON).extract().path("token");
    }
}

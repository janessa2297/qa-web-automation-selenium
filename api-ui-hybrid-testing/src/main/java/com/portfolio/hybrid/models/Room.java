package com.portfolio.hybrid.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Room(int roomid, String roomName, String type, boolean accessible, String image,
                   String description, List<String> features, int roomPrice) { }

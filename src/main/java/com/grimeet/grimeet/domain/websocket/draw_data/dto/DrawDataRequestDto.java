package com.grimeet.grimeet.domain.websocket.draw_data.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DrawDataRequestDto {
    private String projectId;
    private String userId;
    private List<Coordinate> coordinates;

    @Getter
    @NoArgsConstructor
    public static class Coordinate {
        private double x;
        private double y;
        private String color;
        private int stroke;
        private String tool;
        private long timestamp;
    }
}

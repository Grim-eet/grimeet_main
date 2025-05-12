package com.grimeet.grimeet.domain.websocket.draw_data.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "WebSocket 드로잉 요청 DTO")
public class DrawDataRequestDto {

    @Schema(description = "프로젝트 ID", example = "project123")
    @NotBlank(message = "projectId is required")
    private String projectId;

    @Schema(description = "사용자 ID (서버에서 설정됨)", example = "1234")
    private long userId;

    @NotEmpty(message = "coordinates must not be empty")
    @Valid
    @Schema(description = "좌표 목록")
    private List<Coordinate> coordinates;

    @Getter
    @NoArgsConstructor
    @Schema(description = "단일 좌표 정보")
    public static class Coordinate {

        @Schema(description = "x 좌표", example = "153")
        @PositiveOrZero(message = "x must be 0 or positive")
        private double x;

        @Schema(description = "y 좌표", example = "342")
        @PositiveOrZero(message = "y must be 0 or positive")
        private double y;

        @Schema(description = "색상 (HEX)", example = "#000000")
        @NotBlank(message = "color is required")
        private String color;

        @Schema(description = "붓의 두께", example = "3")
        @Positive(message = "stroke must be positive")
        private int stroke;

        @Schema(description = "도구 종류", example = "pen")
        @NotBlank(message = "tool is required")
        private String tool;

        @Schema(description = "이 좌표의 시간(ms)", example = "1682847300000")
        @Positive(message = "timestamp must be positive")
        private String timestamp;
    }
}
